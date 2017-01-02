package net.sparkzz.shops.command;

import net.sparkzz.shops.Shops;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

/**
 * @author Brendon Butler
 */
public class Commands {

	private static CommandSpec shops = CommandSpec.builder().description(Text.of("Shops Command")).permission("shops.cmd.info").executor(new ShopsCommand()).build();

	public static void register(Shops plugin) {
		Sponge.getCommandManager().register(plugin, shops, "shops", "shop");
	}
}