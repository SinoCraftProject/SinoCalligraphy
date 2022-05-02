package games.moegirl.sinocraft.sinocalligraphy.network.packet;

import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocalligraphy.item.FilledXuanPaper;
import games.moegirl.sinocraft.sinocalligraphy.network.SCANetworks;
import games.moegirl.sinocraft.sinocore.network.PacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DrawSaveC2SPacket extends PacketBase {

    private final byte[] draw;

    public DrawSaveC2SPacket(byte[] draw) {
        this.draw = draw;
    }

    public DrawSaveC2SPacket(FriendlyByteBuf buf) {
        super(buf);
        this.draw = buf.readByteArray();
    }

    @Override
    public void serialize(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeByteArray(draw);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender.containerMenu instanceof BrushMenu container) {
                if (container.getPaper().isEmpty()) {
                    SCANetworks.send(SaveFailedS2CPacket.noPaper(), sender);
                    return;
                }
                if (container.getInk().isEmpty()) {
                    SCANetworks.send(SaveFailedS2CPacket.noInk(), sender);
                    return;
                }
                Player player = net.minecraft.client.Minecraft.getInstance().player;
                assert player != null;
                ItemStack filled = FilledXuanPaper.draw(draw, player.getDisplayName());
                container.setFilled(filled);
                SCANetworks.send(new SaveSuccessS2CClient(), sender);
            } else {
                SCANetworks.send(SaveFailedS2CPacket.unknownScreen(), sender);
            }
            context.setPacketHandled(true);
        });
    }
}
