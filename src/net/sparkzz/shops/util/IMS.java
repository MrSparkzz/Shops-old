package net.sparkzz.shops.util;

import net.sparkzz.shops.event.TransactionEvent;
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
import org.spongepowered.api.text.format.TextColors;

import java.math.BigDecimal;
import java.util.Optional;

import static net.sparkzz.shops.event.TransactionEvent.Status.SUCCESS;
import static net.sparkzz.shops.event.TransactionEvent.TransactionType.REFUND;
import static net.sparkzz.shops.util.Messenger.YOU_PURCHASED;
import static net.sparkzz.shops.util.Messenger.YOU_SOLD;
import static net.sparkzz.shops.event.TransactionEvent.Reason.*;
import static net.sparkzz.shops.event.TransactionEvent.Status.INTERRUPTED;
import static net.sparkzz.shops.event.TransactionEvent.TransactionType.SALE;
import static net.sparkzz.shops.event.TransactionEvent.TransactionType.SALE_TO_SHOP;

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

	public static boolean removeItemFromPlayer(PlayerInventory inventory, ItemStack itemStack) {
		Inventory items = inventory.query(itemStack);

		if (items.peek(itemStack.getQuantity()).filter(stack -> stack.getQuantity() == itemStack.getQuantity()).isPresent())
			items.poll(itemStack.getQuantity());
		else return false;
		return true;
	}

	// purchase from shop
	public static void purchase(Shop shop, Player player, ItemType item, int quantity) {
		ItemStack itemStack = ItemStack.builder().itemType(item).quantity(quantity).build();

		if (getAvailableSpace((PlayerInventory) player.getInventory(), item) < quantity) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, itemStack, SALE, INTERRUPTED, INSUFFICIENT_INV_SPACE, Cause.source(player.getInventory()).build()));
			return;
		}

		UniqueAccount account = POS.retrievePlayerAccount(player);

		if (account == null) return;

		BigDecimal totalPrice = shop.getSalePrice(item).multiply(new BigDecimal(quantity));

		// TODO: streamline so player/shop HAS to have sufficient funds & stock
		if (!POS.purchase(player, shop, itemStack, account, totalPrice, economy)) return;

		if (shop.getRemaining(item) < quantity && shop.getRemaining(item) != -1) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, itemStack, SALE, INTERRUPTED, INSUFFICIENT_STOCK, Cause.source(account).build()));
			POS.refund(player, shop, itemStack, account, totalPrice, economy);
			return;
		}
		shop.remove(item, quantity);

		player.getInventory().offer(itemStack);
		player.sendMessage(Text.builder().color(TextColors.GREEN).append(Text.of(String.format("You have purchased %s %s for %s!",
				quantity, item.getName(), economy.getDefaultCurrency().format(totalPrice).toPlain()))).build());
	}

	// sell to shop
	public static void sell(Shop shop, Player player, ItemType item, int quantity) {
		ItemStack itemStack = ItemStack.builder().itemType(item).quantity(quantity).build();

		if (!player.getInventory().contains(itemStack)) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, itemStack, SALE_TO_SHOP, INTERRUPTED, INSUFFICIENT_STOCK, Cause.source(shop).build()));
			return;
		}

		if (shop.getDesiredAmount(item) == 0) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, itemStack, SALE_TO_SHOP, INTERRUPTED, NOT_BUYING, Cause.source(shop).build()));
			return;
		}

		if (shop.getDesiredAmount(item) < quantity && shop.getDesiredAmount(item) != -1) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, itemStack, SALE_TO_SHOP, INTERRUPTED, MORE_THAN_DESIRED, Cause.source(shop).build()));
			return;
		}

		UniqueAccount account = POS.retrievePlayerAccount(player);

		if (account == null) return;

		BigDecimal totalPrice = shop.getBuyPrice(item).multiply(new BigDecimal(quantity));

		if (shop.getBalance().compareTo(totalPrice) < 0 && !shop.hasInfiniteFunds()) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, itemStack, SALE_TO_SHOP, INTERRUPTED, INSUFFICIENT_FUNDS, Cause.source(account).build()));
			return;
		}

		// TODO: streamline so player/shop HAS to have sufficient funds & stock
		if (!POS.sell(player, shop, itemStack, account, totalPrice, economy)) return;

		if (!removeItemFromPlayer((PlayerInventory) player.getInventory(), itemStack)) {
			Sponge.getEventManager().post(new TransactionEvent(shop, player, itemStack, SALE_TO_SHOP, INTERRUPTED, INSUFFICIENT_STOCK, Cause.source(shop).build()));
			POS.refund(shop, account, totalPrice, economy);
			return;
		}

		if (!shop.hasInfiniteStock())
			shop.add(item, quantity);
		shop.adjustDesiredAmount(item, quantity);

		player.sendMessage(Text.builder().color(TextColors.GREEN).append(Text.of(String.format("You have sold %s %s for %s!",
				quantity, item.getName(), economy.getDefaultCurrency().format(totalPrice).toPlain()))).build());
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
			} else
				total += item.getMaxStackQuantity();
		}
		return total;
	}
}