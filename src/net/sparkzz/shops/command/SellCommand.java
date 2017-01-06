package net.sparkzz.shops.command;

import net.sparkzz.shops.Shops;
import net.sparkzz.shops.util.IMS;
import net.sparkzz.shops.util.Messenger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

/**
 * @author Brendon Butler
 */
public class SellCommand implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
		if (!(source instanceof Player)) {
			source.sendMessage(Messenger.CMD_ONLY_PLAYERS);
			return CommandResult.success();
		}

		Player player = (Player) source;
		Integer itemIndex = context.<Integer>getOne("item-index").get(),
				quantity = context.<Integer>getOne("quantity").get();

		IMS.sell(Shops.getDefaultShop(), player, Shops.getDefaultShop().getItemById(itemIndex), quantity);

		return CommandResult.success();
	}
}
