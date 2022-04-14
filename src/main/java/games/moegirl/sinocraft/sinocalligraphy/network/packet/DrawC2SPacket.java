package games.moegirl.sinocraft.sinocalligraphy.network.packet;

import games.moegirl.sinocraft.sinocalligraphy.gui.BrushGuiScreen;
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
import java.util.Collections;
import java.util.function.Supplier;

public class DrawC2SPacket extends PacketBase {
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

            var name = BrushGuiScreen.PIXELS_TAG_NAME;
            var size = BrushGuiScreen.CANVAS_SIZE;


            CompoundTag nbt = output.getOrCreateTag();
            if (nbt.contains(name)) {
                if (pos.x * size + pos.y < (size * size)) {
                    nbt.getByteArray(name)[pos.x * size + pos.y] = color;
                    // Todo: Add brush effect.
                }

                output.setTag(nbt);
                container.setFilled(output);
            } else {
                if (color != 0) {
                    nbt.putByteArray(name, new byte[size * size]);
                }
                // Throw white draw with empty.
            }
        }
    }
}

