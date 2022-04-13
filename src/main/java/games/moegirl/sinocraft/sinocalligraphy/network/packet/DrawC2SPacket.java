package games.moegirl.sinocraft.sinocalligraphy.network.packet;

import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocore.network.PacketBase;
import games.moegirl.sinocraft.sinocore.utility.render.XYPointInt;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.function.Supplier;

public class DrawC2SPacket extends PacketBase {
    public static final int CANVAS_SIZE = 32;

    private XYPointInt pos;
    private byte color;

    public DrawC2SPacket(XYPointInt pointIn, byte colorIn) {
        pos = pointIn;
        color = colorIn;
    }

    public DrawC2SPacket(FriendlyByteBuf buffer) {
        pos = new XYPointInt(buffer.readInt(), buffer.readInt());
        color = buffer.readByte();
    }

    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeInt(pos.x);
        buffer.writeInt(pos.y);
        buffer.writeByte(color);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var context = supplier.get();

        if (context.getDirection() != NetworkDirection.PLAY_TO_SERVER) {
            return; // qyl27: Throw it.
        }

        var sender = context.getSender();

        if (sender == null) {
            return; // qyl27: Throw it.
        }

        if (sender.containerMenu instanceof BrushMenu) {
            BrushMenu container = (BrushMenu) sender.containerMenu;
            ItemStack paper = container.getPaper();
            ItemStack ink = container.getInk();

            if (paper.isEmpty() || ink.isEmpty()) {
                return;
            }

            ItemStack output = container.getFilled();

            if (output.isEmpty()) {
                output = new ItemStack(SCAItems.XUAN_PAPER.get());
            }

            CompoundTag nbt = output.getOrCreateTag();
            if (!nbt.contains("pixels")) {
                nbt.putByteArray("pixels", new byte[CANVAS_SIZE * CANVAS_SIZE]);
            }

            if (pos.x * 32 + pos.y < 1024) {
                nbt.getByteArray("pixels")[pos.x * CANVAS_SIZE + pos.y] = color;
                // Todo: Add brush effect.
            }

            if (color == 0) {
                if (Arrays.equals(nbt.getByteArray("pixels"), new byte[CANVAS_SIZE * CANVAS_SIZE])) {
                    return;
                }
            }

            output.setTag(nbt);
            container.setFilled(output);
        }
    }
}

