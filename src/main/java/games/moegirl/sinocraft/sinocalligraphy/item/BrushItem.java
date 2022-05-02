package games.moegirl.sinocraft.sinocalligraphy.item;

import games.moegirl.sinocraft.sinocalligraphy.gui.provider.BrushMenuProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class BrushItem extends Item {
    public BrushItem() {
        super(new Properties()
                .tab(SCACreativeTab.CALLIGRAPHY)
                .durability(127)
                .setNoRepair());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var item = player.getItemInHand(hand);

        // qyl27: The argument "limitedTag" is true in general.
        // lq2007: writeItem equals to writeItemStack(item, true)
        if (!level.isClientSide) {
            NetworkHooks.openGui((ServerPlayer) player, new BrushMenuProvider(item),
                    (FriendlyByteBuf byteBuf) -> byteBuf.writeItem(item));
        }

        return InteractionResultHolder.success(item);
    }
}
