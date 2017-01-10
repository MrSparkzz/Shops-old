package net.sparkzz.shops;

import com.google.inject.Inject;
import net.sparkzz.shops.command.Commands;
import net.sparkzz.shops.config.Config;
import net.sparkzz.shops.config.YamlConfig;
import net.sparkzz.shops.event.TransactionListener;
import net.sparkzz.shops.shop.Shop;
import net.sparkzz.shops.util.IMS;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.UUID;

/**
 * @author Brendon Butler
 */
@Plugin(id = "shops", name = "Shops", version = "0.3.6-ALPHA", description = "Command Based Shops", authors = {"MrSparkzz"})
public class Shops {

	private static EconomyService economy;

	// TODO: replace with shop player is in
	private static Shop shop = new Shop("Test Shop", UUID.randomUUID());

	private Config defaultConfig, shopsConfig;

	@Inject
	private Logger logger;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private Path defaultConfiguration;

	@Inject
	@DefaultConfig(sharedRoot = false)
	private Path defaultConfigDir;

	@Listener
	public void onChangeServiceProvider(ChangeServiceProviderEvent event) {
		if (event.getService().equals(EconomyService.class)) {
			economy = (EconomyService) event.getNewProviderRegistration().getProvider();
		}
	}

	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		new IMS(economy);

		shop.add(ItemTypes.COOKED_PORKCHOP, 10, -1, new BigDecimal(5), new BigDecimal(2.5));
		shop.deposit(new BigDecimal(1000));
	}

	// TODO: REMOVE
	public static Shop getDefaultShop() {
		return shop;
	}

	@Listener
	public void onServerStop(GameStoppingServerEvent event) {
		shopsConfig.save();
	}

	@Listener
	public void preInit(GamePreInitializationEvent event) {
		Sponge.getEventManager().registerListeners(this, new TransactionListener());
	}

	@Listener
	public void init(GameInitializationEvent event) {
		Commands.register(this);

		shopsConfig = new YamlConfig(defaultConfigDir, "shops");
	}

	public static EconomyService getEconomy() {
		return economy;
	}

	public Logger getLogger() {
		return logger;
	}

	public Path getConfig() {
		return defaultConfiguration;
	}

	public Path getDefaultConfigDir() {
		return defaultConfigDir;
	}
}