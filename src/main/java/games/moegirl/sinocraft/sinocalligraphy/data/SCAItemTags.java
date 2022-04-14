package games.moegirl.sinocraft.sinocalligraphy.data;

import games.moegirl.sinocraft.sinocore.SinoCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SCAItemTags {
    public static final TagKey<Item> INKS= ItemTags.create(new ResourceLocation(SinoCore.MODID, "inks"));
}
