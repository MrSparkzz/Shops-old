package net.sparkzz.shops.command;

import net.sparkzz.shops.Shops;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

/**
 * @author Brendon Butler
 */
public class Commands {

	// consumer commands

	private static CommandSpec buy = CommandSpec.builder()
			.description(Text.of("Buy items from a shop"))
			.permission("shops.cmd.buy")
			.arguments(GenericArguments.onlyOne(GenericArguments.integer(Text.of("item-index"))),
												GenericArguments.integer(Text.of("quantity")))
			.executor(new BuyCommand())
			.build();

	private static CommandSpec sell = CommandSpec.builder()
			.description(Text.of("Sell items to a shop"))
			.permission("shops.cmd.sell")
			.arguments(GenericArguments.onlyOne(GenericArguments.integer(Text.of("item-index"))),
					GenericArguments.integer(Text.of("quantity")))
			.executor(new SellCommand())
			.build();


	private static CommandSpec info = CommandSpec.builder()
			.permission("shops.cmd.info")
			.description(Text.of("Get shop info"))
			.executor(new InfoCommand()).build();
/*
	// shop owner commands

	private static CommandSpec construct = CommandSpec.builder()
			.permission("shops.cmd.construct")
			.description(Text.of("Create your own shop"))
			.executor(new ShopCommand()).build();

	private static CommandSpec destroy = CommandSpec.builder()
			.permission("shops.cmd.destroy")
			.description(Text.of("Destroy your shop"))
			.executor(new ShopCommand()).build();
*/

	// admin commands

	public static void register(Shops plugin) {
		Sponge.getCommandManager().register(plugin, info, "shops");
		Sponge.getCommandManager().register(plugin, buy, "buy");
		Sponge.getCommandManager().register(plugin, sell, "sell");
	}
}