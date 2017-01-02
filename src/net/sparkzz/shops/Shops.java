package net.sparkzz.shops;

import com.google.inject.Inject;
import net.sparkzz.shops.command.Commands;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

/**
 * @author Brendon Butler
 */
@Plugin(id = "shops", name = "Shops", version = "0.1.2-ALPHA", description = "Command Based Shops", authors = {"MrSparkzz"})
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

	@Listener
	public void init(GameInitializationEvent event) {
		Commands.register(this);
	}

	public Logger getLogger() {
		return logger;
	}
}