package games.moegirl.sinocraft.sinocalligraphy.network.packet;

import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocore.network.PacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SaveFailedS2CPacket extends PacketBase {

    private final Reason reason;

    public static SaveFailedS2CPacket unknownScreen() {
        return new SaveFailedS2CPacket(Reason.UnknownScreen);
    }

    public static SaveFailedS2CPacket noPaper() {
        return new SaveFailedS2CPacket(Reason.NoPaper);
    }

    public static SaveFailedS2CPacket noInk() {
        return new SaveFailedS2CPacket(Reason.NoInk);
    }

    public SaveFailedS2CPacket(Reason reason) {
        this.reason = reason;
    }

    public SaveFailedS2CPacket(FriendlyByteBuf buf) {
        this.reason = buf.readEnum(Reason.class);
    }

    @Override
    public void serialize(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeEnum(reason);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player player = net.minecraft.client.Minecraft.getInstance().player;
            if (player.containerMenu instanceof BrushMenu container) {
                container.gui.handleSaveResult(reason);
            }
            context.setPacketHandled(true);
        });
    }

    public enum Reason {
        Succeed, UnknownScreen, NoPaper, NoInk
    }
}
