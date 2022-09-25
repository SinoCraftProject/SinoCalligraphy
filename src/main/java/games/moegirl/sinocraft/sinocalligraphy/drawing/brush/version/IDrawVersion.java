package games.moegirl.sinocraft.sinocalligraphy.drawing.brush.version;

import games.moegirl.sinocraft.sinocalligraphy.drawing.brush.storage.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public interface IDrawVersion {
    public boolean isLatest();
    public DrawType getType();

    public IDrawStorage newDraw();

    public boolean matches(CompoundTag tag);
    public boolean matches(FriendlyByteBuf buf);
    public boolean matches(String str);

    public IDrawStorage from(CompoundTag tag);
    public IDrawStorage from(FriendlyByteBuf buf);
    public IDrawStorage from(String str);

    public IDrawStorage upgrade(IDrawStorage drawStorage);
}
