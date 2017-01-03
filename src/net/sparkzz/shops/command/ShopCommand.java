package net.sparkzz.shops.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

/**
 * @author Brendon Butler
 */
public class ShopCommand implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {

		return CommandResult.success();
	}
}