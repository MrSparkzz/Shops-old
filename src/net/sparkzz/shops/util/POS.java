package net.sparkzz.shops.util;

import net.sparkzz.shops.shop.Shop;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.math.BigDecimal;
import java.util.Optional;

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
			player.sendMessage(Text.of("%sError accessing account!", TextColors.RED));
			return null;
		}

		return optionalAcc.get();
	}

	public static boolean purchase(Player player, Shop shop, UniqueAccount account, BigDecimal amount, EconomyService economy) {
		ResultType purchaseResult = account.withdraw(economy.getDefaultCurrency(), amount, Cause.source(IMS.class).build()).getResult();
		shop.deposit(amount);

		if (purchaseResult == ResultType.SUCCESS)
			return true;
		else if (purchaseResult == ResultType.ACCOUNT_NO_FUNDS)
			player.sendMessage(Text.of("%sInsufficient Funds!", TextColors.RED));
		else if (purchaseResult == ResultType.FAILED)
			player.sendMessage(Text.of("%sAn error occurred while processing your transaction!", TextColors.RED));
		return false;
	}

	public static boolean refund(Player player, Shop shop, UniqueAccount account, BigDecimal amount, EconomyService economy) {
		ResultType refundResult = account.deposit(economy.getDefaultCurrency(), amount, Cause.source(IMS.class).build()).getResult();
		shop.withdraw(amount);

		if (refundResult == ResultType.SUCCESS)
			return true;
		else if (refundResult == ResultType.ACCOUNT_NO_SPACE)
			player.sendMessage(Text.of("%sInsufficient account space!", TextColors.RED));
		else if (refundResult == ResultType.FAILED)
			player.sendMessage(Text.of("%sAn error occurred while processing your return!", TextColors.RED));
		return false;
	}

	public static boolean sell(Player player, Shop shop, UniqueAccount account, BigDecimal amount, EconomyService economy) {
		ResultType saleResult = account.deposit(economy.getDefaultCurrency(), amount, Cause.source(IMS.class).build()).getResult();
		shop.withdraw(amount);

		if (saleResult == ResultType.SUCCESS)
			return true;
		else if (saleResult == ResultType.ACCOUNT_NO_SPACE)
			player.sendMessage(Text.of("%sInsufficient account space!", TextColors.RED));
		else if (saleResult == ResultType.FAILED)
			player.sendMessage(Text.of("%sAn error occurred while processing your sale!", TextColors.RED));
		return false;
	}
}