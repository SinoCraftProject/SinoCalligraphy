package games.moegirl.sinocraft.sinocalligraphy.network.packet;

import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocore.network.PacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CSaveSucceed extends PacketBase {

    public S2CSaveSucceed() {
    }

    public S2CSaveSucceed(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void serialize(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player player = net.minecraft.client.Minecraft.getInstance().player;
            if (player.containerMenu instanceof BrushMenu container) {
                container.gui.handleSaveResult(S2CSaveFailed.Info.Succeed);
            }
            context.setPacketHandled(true);
        });
    }
}
