package games.moegirl.sinocraft.sinocalligraphy.gui.texture;

import games.moegirl.sinocraft.sinocore.api.command.CommandRegister;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class DebugCommand {

    public static final CommandRegister REGISTRY = new CommandRegister();

    public static final String CMD = REGISTRY.register("sinocore", builder -> builder
            .then("reload")
            .then("tex_map_name", ResourceLocationArgument.id())
            .suggests((context, b) -> {
                TextureParser.NAMES
                        .stream()
                        .map(ResourceLocation::toString)
                        .forEach(b::suggest);
                return b.buildFuture();
            })
            .execute(context -> {
                ResourceLocation name = ResourceLocationArgument.getId(context, "tex_map_name");
                TextureMap map = TextureParser.MAPS.get(name);
                if (map == null) {
                    context.getSource().sendFailure(new TextComponent("Not found texture map named " + name));
                } else {
                    map.reload();
                    context.getSource().sendSuccess(new TextComponent("Reload succeed"), false);
                }
            }));
}
