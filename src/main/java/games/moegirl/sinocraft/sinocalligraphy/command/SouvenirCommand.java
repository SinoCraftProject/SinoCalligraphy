package games.moegirl.sinocraft.sinocalligraphy.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;

public class SouvenirCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("sinocalligraphy")
                .then(literal("souvenir")
                        .then(argument("id", ResourceLocationArgument.id())
                                .executes(SouvenirCommand::sendSouvenir))));
    }

    public static int sendSouvenir(CommandContext<CommandSourceStack> context) {


        return Command.SINGLE_SUCCESS;
    }
}
