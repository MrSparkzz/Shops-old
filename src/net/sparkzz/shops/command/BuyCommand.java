package net.sparkzz.shops.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;

/**
 * @author Brendon Butler
 */
public class BuyCommand implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
		if (!args.hasAny("item")) return CommandResult.success();

		ItemType itemType = args.<ItemType>getOne("item").get();
		//int quantity = args.<Integer>getOne("quantity").get();

		source.sendMessage(Text.of("ItemType: ", itemType));

		return CommandResult.success();
	}
}
