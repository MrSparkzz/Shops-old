package net.sparkzz.shops;

import com.google.inject.Inject;
import net.sparkzz.shops.command.Commands;
import net.sparkzz.shops.shop.Shop;
import net.sparkzz.shops.util.IMS;
import net.sparkzz.shops.event.TransactionListener;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
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
import java.util.UUID;

/**
 * @author Brendon Butler
 */
@Plugin(id = "shops", name = "Shops", version = "0.3.6-ALPHA", description = "Command Based Shops", authors = {"MrSparkzz"})
public class Shops {

	private static EconomyService economy;

	// TODO: replace with shop player is in
	private static Shop shop = new Shop("Test Shop", UUID.randomUUID());

	@Inject
	private Logger logger;

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
		// server stopping
	}

	@Listener
	public void preInit(GamePreInitializationEvent event) {
		Sponge.getEventManager().registerListeners(this, new TransactionListener());
	}

	@Listener
	public void init(GameInitializationEvent event) {
		Commands.register(this);
	}

	public static EconomyService getEconomy() {
		return economy;
	}

	public Logger getLogger() {
		return logger;
	}
}