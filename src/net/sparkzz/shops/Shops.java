package net.sparkzz.shops;

import com.google.inject.Inject;
import net.sparkzz.shops.command.Commands;
import net.sparkzz.shops.util.IMS;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;

/**
 * @author Brendon Butler
 */
@Plugin(id = "shops", name = "Shops", version = "0.1.4-ALPHA", description = "Command Based Shops", authors = {"MrSparkzz"})
public class Shops {

	private EconomyService economy;

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
	}

	@Listener
	public void onServerStop(GameStoppingServerEvent event) {
		// server stopping
	}

	@Listener
	public void preInit(GamePreInitializationEvent event) {
		logger.info("working..");
	}

	@Listener
	public void init(GameInitializationEvent event) {
		Commands.register(this);
	}

	public EconomyService getEconomy() {
		return economy;
	}

	public Logger getLogger() {
		return logger;
	}
}