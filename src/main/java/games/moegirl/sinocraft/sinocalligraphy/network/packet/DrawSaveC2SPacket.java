package games.moegirl.sinocraft.sinocalligraphy.network.packet;

import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocalligraphy.network.SCANetworks;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawHolder;
import games.moegirl.sinocraft.sinocore.network.PacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DrawSaveC2SPacket extends PacketBase {

    private final DrawHolder holder;
    private final int button;

    public DrawSaveC2SPacket(DrawHolder holder, int button) {
        this.holder = holder;
        this.button = button;
    }

    public DrawSaveC2SPacket(FriendlyByteBuf buf) {
        button = buf.readByte();
        holder = DrawHolder.parse(buf).orElseThrow();
    }

    @Override
    public void serialize(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeByte(button);
        holder.version().write(holder, friendlyByteBuf);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender.containerMenu instanceof BrushMenu container) {
                if (container.getPaper().isEmpty()) {
                    SCANetworks.send(SaveFailedS2CPacket.noPaper(button), sender);
                    return;
                }
                if (container.getInk().isEmpty()) {
                    SCANetworks.send(SaveFailedS2CPacket.noInk(button), sender);
                    return;
                }
                ItemStack filled = new ItemStack(SCAItems.FILLED_XUAN_PAPER.get());
                holder.version().write(holder, filled.getOrCreateTag());
                container.setFilled(filled);
                SCANetworks.send(new SaveSuccessS2CClient(button), sender);
            } else {
                SCANetworks.send(SaveFailedS2CPacket.unknownScreen(button), sender);
            }
            context.setPacketHandled(true);
        });
    }
}
