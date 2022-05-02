package games.moegirl.sinocraft.sinocalligraphy.network.packet;

import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocore.network.PacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CSaveFailed extends PacketBase {

    private final Info info;

    public static S2CSaveFailed unknownScreen() {
        return new S2CSaveFailed(Info.UnknownScreen);
    }

    public static S2CSaveFailed noPaper() {
        return new S2CSaveFailed(Info.NoPaper);
    }

    public static S2CSaveFailed noInk() {
        return new S2CSaveFailed(Info.NoInk);
    }

    public S2CSaveFailed(Info info) {
        this.info = info;
    }

    public S2CSaveFailed(FriendlyByteBuf buf) {
        this.info = buf.readEnum(Info.class);
    }

    @Override
    public void serialize(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeEnum(info);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player player = net.minecraft.client.Minecraft.getInstance().player;
            if (player.containerMenu instanceof BrushMenu container) {
                container.gui.handleSaveResult(info);
            }
            context.setPacketHandled(true);
        });
    }

    public enum Info {
        Succeed, UnknownScreen, NoPaper, NoInk
    }
}
