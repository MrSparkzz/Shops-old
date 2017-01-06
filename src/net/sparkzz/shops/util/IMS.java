package net.sparkzz.shops.util;

import net.sparkzz.shops.shop.Shop;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.Optional;

import static net.sparkzz.shops.util.Messenger.YOU_PURCHASED;
import static net.sparkzz.shops.util.Messenger.YOU_SOLD;
import static net.sparkzz.shops.util.TransactionEvent.Reason.*;
import static net.sparkzz.shops.util.TransactionEvent.Status.INTERRUPTED;
import static net.sparkzz.shops.util.TransactionEvent.TransactionType.SALE;
import static net.sparkzz.shops.util.TransactionEvent.TransactionType.SALE_TO_SHOP;

/**
 * Inventory Management System
 *
 * @author Brendon Butler
 */
public class IMS {

	private static EconomyService economy;

	public IMS(EconomyService economyService) {
		economy = economyService;
	}

	// purchase from shop
	public static boolean purchase(Shop shop, Player player, ItemType item, int quantity) {
		ItemStack itemStack = ItemStack.builder().itemType(item).quantity(quantity).build();

		if (getAvailableSpace((PlayerInventory) player.getInventory(), item) < quantity) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, itemStack, SALE, INTERRUPTED, INSUFFICIENT_INV_SPACE, Cause.source(player.getInventory()).build()));
			return false;
		}

		UniqueAccount account = POS.retrievePlayerAccount(player, economy);

		if (account == null) return false;

		BigDecimal totalPrice = shop.getSalePrice(item).multiply(new BigDecimal(quantity));

		if (!POS.purchase(player, shop, itemStack, account, totalPrice, economy)) return false;

		if (!shop.hasInfiniteStock()) {
			if (shop.getRemaining(item) < quantity) {
				Sponge.getEventManager().post(new TransactionEvent(shop, player, itemStack, SALE, INTERRUPTED, INSUFFICIENT_STOCK, Cause.source(account).build()));
				POS.refund(player, shop, itemStack, account, totalPrice, economy);
				return false;
			}
			shop.remove(item, quantity);
		}

		player.getInventory().offer(itemStack);
		player.sendMessage(Messenger.format(Text.of(YOU_PURCHASED,
				quantity, (quantity != 1 ? Messenger.pluralize(item.getName()) : item.getName()), economy.getDefaultCurrency().format(totalPrice))));
		return true;
	}

	// sell to shop
	public static boolean sell(Shop shop, Player player, ItemType item, int quantity) {
		ItemStack itemStack = ItemStack.builder().itemType(item).quantity(quantity).build();

		if (!player.getInventory().contains(itemStack)) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, itemStack, SALE_TO_SHOP, INTERRUPTED, INSUFFICIENT_STOCK, Cause.source(shop).build()));
			return false;
		}

		if (shop.getDesiredAmount(item) == -1) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, itemStack, SALE_TO_SHOP, INTERRUPTED, NOT_BUYING, Cause.source(shop).build()));
			return false;
		}

		if (shop.getDesiredAmount(item) < quantity) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, itemStack, SALE_TO_SHOP, INTERRUPTED, MORE_THAN_DESIRED, Cause.source(shop).build()));
			return false;
		}

		UniqueAccount account = POS.retrievePlayerAccount(player, economy);

		if (account == null) return false;

		BigDecimal totalPrice = shop.getBuyPrice(item).multiply(new BigDecimal(quantity));

		if (shop.getBalance().compareTo(totalPrice) < 0 && !shop.hasInfiniteFunds()) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, itemStack, SALE_TO_SHOP, INTERRUPTED, INSUFFICIENT_FUNDS, Cause.source(account).build()));
			return false;
		}

		if (!POS.sell(player, shop, itemStack, account, totalPrice, economy));

		// TODO: remove item from player inventory

		if (!shop.hasInfiniteStock())
			shop.add(item, quantity);

		// TODO: regex information
		player.sendMessage(Messenger.format(Text.of(YOU_SOLD,
				quantity, (quantity != 1 ? Messenger.pluralize(item.getName()) : item.getName()), economy.getDefaultCurrency().format(totalPrice))));
		return true;
	}

	public static int getAvailableSpace(PlayerInventory inventory, ItemType item) {
		ItemStack currentItemStack;
		int total = 0;

		for (Inventory slot : inventory.getMain().slots()) {
			Optional<ItemStack> slotContents = slot.peek();

			if (slotContents.isPresent()) {
				currentItemStack = slotContents.get();
				if (currentItemStack.getItem().getType() == item)
					total += currentItemStack.getMaxStackQuantity() - currentItemStack.getQuantity();
			} else {
				total += item.getMaxStackQuantity();
			}
		}
		return total;
	}
}