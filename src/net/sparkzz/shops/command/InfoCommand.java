package net.sparkzz.shops.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * @author Brendon Butler
 */
public class InfoCommand implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
		source.sendMessage(Text.builder().color(TextColors.DARK_AQUA).append(Text.of("===[ Shops ]===")).build());
		source.sendMessage(Text.builder().color(TextColors.GREEN).append(Text.of("Created By: MrSparkzz")).build());
		source.sendMessage(Text.builder().color(TextColors.GREEN).append(Text.of("Version: 1.0")).build());
		source.sendMessage(Text.builder().color(TextColors.DARK_AQUA).append(Text.of("===[ Shops ]===")).build());

		return CommandResult.success();
	}
}
