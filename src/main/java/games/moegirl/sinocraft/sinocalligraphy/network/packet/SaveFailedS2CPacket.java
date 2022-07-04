package games.moegirl.sinocraft.sinocalligraphy.network.packet;

import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocalligraphy.utility.NetworkHelper;
import games.moegirl.sinocraft.sinocore.network.PacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SaveFailedS2CPacket extends PacketBase {

    private final Reason reason;
    private final int button;

    public static SaveFailedS2CPacket unknownScreen(int button) {
        return new SaveFailedS2CPacket(Reason.UnknownScreen, button);
    }

    public static SaveFailedS2CPacket noPaper(int button) {
        return new SaveFailedS2CPacket(Reason.NoPaper, button);
    }

    public static SaveFailedS2CPacket noInk(int button) {
        return new SaveFailedS2CPacket(Reason.NoInk, button);
    }

    public SaveFailedS2CPacket(Reason reason, int button) {
        this.reason = reason;
        this.button = button;
    }

    public SaveFailedS2CPacket(FriendlyByteBuf buf) {
        this.reason = buf.readEnum(Reason.class);
        this.button = buf.readByte();
    }

    @Override
    public void serialize(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeEnum(reason);
        friendlyByteBuf.writeByte(button);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            NetworkHelper.onClientHandleDraw(reason, button);
            context.setPacketHandled(true);
        });
    }

    public enum Reason {
        Succeed, UnknownScreen, NoPaper, NoInk
    }
}
