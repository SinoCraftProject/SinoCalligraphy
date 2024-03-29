package games.moegirl.sinocraft.sinocalligraphy.network.packet;

import games.moegirl.sinocraft.sinocalligraphy.utility.NetworkHelper;
import games.moegirl.sinocraft.sinocore.network.PacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SaveSuccessS2CPacket extends PacketBase {

    private final int button;

    public SaveSuccessS2CPacket(int button) {
        this.button = button;
    }

    public SaveSuccessS2CPacket(FriendlyByteBuf buf) {
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
            NetworkHelper.onClientHandleDraw(SaveFailedS2CPacket.Reason.Succeed, button);
            context.setPacketHandled(true);
        });
    }
}
