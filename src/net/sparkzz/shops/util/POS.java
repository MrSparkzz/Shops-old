package net.sparkzz.shops.util;

import net.sparkzz.shops.shop.Shop;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;

import java.math.BigDecimal;
import java.util.Optional;

import static net.sparkzz.shops.util.TransactionEvent.Reason.*;
import static net.sparkzz.shops.util.TransactionEvent.Status.*;
import static net.sparkzz.shops.util.TransactionEvent.TransactionType.*;

/**
 * Point of Sale system
 *
 * @author Brendon Butler
 */
public class POS {

	// TODO: store transaction history

	public static UniqueAccount retrievePlayerAccount(Player player, EconomyService economy) {
		Optional<UniqueAccount> optionalAcc = economy.getOrCreateAccount(player.getUniqueId());

		if (!optionalAcc.isPresent()) {
			player.sendMessage(Messenger.ERROR_ACCESSING_ACC);
			return null;
		}

		return optionalAcc.get();
	}

	public static boolean purchase(Player player, Shop shop, ItemStack item, UniqueAccount account, BigDecimal amount, EconomyService economy) {
		ResultType purchaseResult = account.withdraw(economy.getDefaultCurrency(), amount, Cause.source(IMS.class).build()).getResult();
		shop.deposit(amount);

		if (purchaseResult == ResultType.SUCCESS) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, item, SALE, SUCCESS, Cause.source(account).build()));
			return true;
		} else if (purchaseResult == ResultType.ACCOUNT_NO_FUNDS)
			Sponge.getEventManager().post(new TransactionEvent(shop, player, item, SALE, INTERRUPTED, INSUFFICIENT_FUNDS, Cause.source(account).build()));
		else if (purchaseResult == ResultType.FAILED)
			Sponge.getEventManager().post(new TransactionEvent(shop, player, item, SALE, FAILED, OTHER, Cause.source(account).build()));
		return false;
	}

	public static boolean refund(Player player, Shop shop, ItemStack item, UniqueAccount account, BigDecimal amount, EconomyService economy) {
		ResultType refundResult = account.deposit(economy.getDefaultCurrency(), amount, Cause.source(IMS.class).build()).getResult();
		shop.withdraw(amount);

		if (refundResult == ResultType.SUCCESS) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, item, REFUND, SUCCESS, Cause.source(account).build()));
			return true;
		} else if (refundResult == ResultType.ACCOUNT_NO_SPACE)
			Sponge.getEventManager().post(new TransactionEvent(shop, player, item, REFUND, INTERRUPTED, INVALID_ACC_BAL, Cause.source(account).build()));
		else if (refundResult == ResultType.FAILED)
			Sponge.getEventManager().post(new TransactionEvent(shop, player, item, REFUND, FAILED, OTHER, Cause.source(account).build()));
		return false;
	}

	public static boolean sell(Player player, Shop shop, ItemStack item, UniqueAccount account, BigDecimal amount, EconomyService economy) {
		ResultType saleResult = account.deposit(economy.getDefaultCurrency(), amount, Cause.source(IMS.class).build()).getResult();
		if (!shop.hasInfiniteFunds())
			shop.withdraw(amount);

		if (saleResult == ResultType.SUCCESS) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, item, SALE_TO_SHOP, SUCCESS, Cause.source(account).build()));
			return true;
		} else if (saleResult == ResultType.ACCOUNT_NO_SPACE)
			Sponge.getEventManager().post(new TransactionEvent(shop, player, item, SALE_TO_SHOP, INTERRUPTED, INVALID_ACC_BAL, Cause.source(account).build()));
		else if (saleResult == ResultType.FAILED)
			Sponge.getEventManager().post(new TransactionEvent(shop, player, item, SALE_TO_SHOP, FAILED, OTHER, Cause.source(account).build()));
		return false;
	}
}