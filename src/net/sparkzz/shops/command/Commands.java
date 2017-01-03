package net.sparkzz.shops.command;

import net.sparkzz.shops.Shops;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

/**
 * @author Brendon Butler
 */
public class Commands {

	// consumer commands

	private static CommandSpec buy = CommandSpec.builder()
			.permission("shops.cmd.buy")
			.description(Text.of("Purchase items from a shop"))
			.arguments(
					GenericArguments.optional(GenericArguments.catalogedElement(Text.of("item"), CatalogTypes.ITEM_TYPE))
			)
			.executor(new BuyCommand()).build();

	private static CommandSpec sell = CommandSpec.builder()
			.permission("shops.cmd.sell")
			.description(Text.of("Sell items to a shop"))
			.executor(new ShopCommand()).build();

	private static CommandSpec info = CommandSpec.builder()
			.permission("shops.cmd.info")
			.description(Text.of("Get shop info"))
			.executor(new ShopCommand()).build();

	// shop owner commands

	private static CommandSpec construct = CommandSpec.builder()
			.permission("shops.cmd.construct")
			.description(Text.of("Create your own shop"))
			.executor(new ShopCommand()).build();

	private static CommandSpec destroy = CommandSpec.builder()
			.permission("shops.cmd.destroy")
			.description(Text.of("Destroy your shop"))
			.executor(new ShopCommand()).build();

	// admin commands

	private static CommandSpec shop = CommandSpec.builder()
			.permission("shops.cmd.info")
			.description(Text.of("Shop Command"))
			.child(buy, "buy", "b")
			.child(sell, "sell", "s")
			.executor(new ShopCommand()).build();

	public static void register(Shops plugin) {
		Sponge.getCommandManager().register(plugin, shop, "shop");
	}
}