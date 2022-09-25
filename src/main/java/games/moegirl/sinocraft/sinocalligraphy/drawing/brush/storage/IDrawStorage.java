package games.moegirl.sinocraft.sinocalligraphy.drawing.brush.storage;

import games.moegirl.sinocraft.sinocalligraphy.drawing.brush.version.IDrawVersion;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public interface IDrawStorage {
    public IDrawVersion getVersion();

    public CompoundTag write(CompoundTag tag);
    public FriendlyByteBuf write(FriendlyByteBuf buf);
    public String write();

    public IDrawStorage read(CompoundTag tag);
    public IDrawStorage read(FriendlyByteBuf buf);
    public IDrawStorage read();

    public DrawAuthor getAuthor();
    public void setAuthor(DrawAuthor author);

    public byte[] getData();
    public void setData(byte[] bytes);
}
