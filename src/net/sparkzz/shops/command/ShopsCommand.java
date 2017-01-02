package net.sparkzz.shops.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

/**
 * @author Brendon Butler
 */
public class ShopsCommand implements CommandExecutor {


	@Override
	public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
		source.sendMessage(Text.of("Working!"));
		return CommandResult.success();
	}
}