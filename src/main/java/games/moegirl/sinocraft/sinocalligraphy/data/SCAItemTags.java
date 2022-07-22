package games.moegirl.sinocraft.sinocalligraphy.data;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocore.SinoCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SCAItemTags {
    public static final TagKey<Item> INKS = ItemTags.create(new ResourceLocation(SinoCalligraphy.MODID, "inks"));
    public static final TagKey<Item> FAN = ItemTags.create(new ResourceLocation(SinoCalligraphy.MODID, "fan"));

    public static final TagKey<Item> GARLIC = ItemTags.create(new ResourceLocation("noceat", "flames"));
}
