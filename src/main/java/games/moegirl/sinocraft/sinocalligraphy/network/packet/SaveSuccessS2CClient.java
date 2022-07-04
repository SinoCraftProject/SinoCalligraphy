package games.moegirl.sinocraft.sinocalligraphy.network.packet;

import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocore.network.PacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SaveSuccessS2CClient extends PacketBase {

    private final int button;

    public SaveSuccessS2CClient(int button) {
        this.button = button;
    }

    public SaveSuccessS2CClient(FriendlyByteBuf buf) {
        super(buf);
        button = buf.readByte();
    }

    @Override
    public void serialize(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeByte(button);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player.containerMenu instanceof BrushMenu container) {
                container.gui.handleSaveResult(SaveFailedS2CPacket.Reason.Succeed, button);
            }
            context.setPacketHandled(true);
        });
    }
}
