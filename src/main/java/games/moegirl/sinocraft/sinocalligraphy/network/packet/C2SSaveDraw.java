package games.moegirl.sinocraft.sinocalligraphy.network.packet;

import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocalligraphy.item.XuanPaperItem;
import games.moegirl.sinocraft.sinocalligraphy.network.SCANetworks;
import games.moegirl.sinocraft.sinocore.network.PacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SSaveDraw extends PacketBase {

    private final byte[] draw;

    public C2SSaveDraw(byte[] draw) {
        this.draw = draw;
    }

    public C2SSaveDraw(FriendlyByteBuf buf) {
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
                    SCANetworks.send(S2CSaveFailed.noPaper(), sender);
                    return;
                }
                if (container.getInk().isEmpty()) {
                    SCANetworks.send(S2CSaveFailed.noInk(), sender);
                    return;
                }
                Player player = net.minecraft.client.Minecraft.getInstance().player;
                assert player != null;
                ItemStack filled = XuanPaperItem.createDraw(draw, player.getDisplayName());
                container.setFilled(filled);
                SCANetworks.send(new S2CSaveSucceed(), sender);
            } else {
                SCANetworks.send(S2CSaveFailed.unknownScreen(), sender);
            }
            context.setPacketHandled(true);
        });
    }
}
