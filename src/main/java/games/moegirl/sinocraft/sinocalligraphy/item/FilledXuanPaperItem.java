package games.moegirl.sinocraft.sinocalligraphy.item;

import games.moegirl.sinocraft.sinocalligraphy.client.SCAClient;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class FilledXuanPaperItem extends Item {
    public FilledXuanPaperItem() {
        super(new Properties()
                .tab(SCACreativeTab.CALLIGRAPHY)
                .stacksTo(1)
                .setNoRepair());
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return SCAClient.getBEWLR();
            }
        });
    }
}
