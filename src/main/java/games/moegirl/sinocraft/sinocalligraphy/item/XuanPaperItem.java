package games.moegirl.sinocraft.sinocalligraphy.item;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.client.XuanPaperRenderer;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawHolder;
import games.moegirl.sinocraft.sinocalligraphy.utility.XuanPaperType;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class XuanPaperItem extends Item {
    private XuanPaperType type;

    public XuanPaperItem(XuanPaperType typeIn) {
        super(new Properties()
                .stacksTo(64)
                .setNoRepair()
                .tab(SCACreativeTab.CALLIGRAPHY));

        type = typeIn;
    }

    public XuanPaperType getType() {
        return type;
    }
}
