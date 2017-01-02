package net.sparkzz.shops;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

/**
 * @author Brendon Butler
 */
@Plugin(id = "shops", name = "Shops", version = "0.1.0-ALPHA")
public class Shops {

	@Inject
	private Logger logger;

	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		// server started
	}

	@Listener
	public void onServerStop(GameStoppingServerEvent event) {
		// server stopping
	}

	@Listener
	public void preInit(GamePreInitializationEvent event) {
		logger.info("working..");
	}

	public Logger getLogger() {
		return logger;
	}
}