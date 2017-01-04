package net.sparkzz.shops.util;

import net.sparkzz.shops.shop.Shop;
import org.spongepowered.api.entity.living.player.Player;
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
		if (getAvailableSpace((PlayerInventory) player.getInventory(), item) < quantity) {
			player.sendMessage(Text.of("%sNot enough inventory space!", TextColors.RED));
			return false;
		}

		UniqueAccount account = POS.retrievePlayerAccount(player, economy);

		if (account == null) return false;

		BigDecimal totalPrice = shop.getBuyPrice(item).multiply(new BigDecimal(quantity));

		if (!POS.purchase(player, shop, account, totalPrice, economy)) return false;

		if (!shop.hasInfiniteStock()) {
			if (shop.getRemaining(item) < quantity) {
				player.sendMessage(Text.of("%sStore has insufficient stock!", TextColors.RED));
				POS.refund(player, shop, account, totalPrice, economy);
				return false;
			}
			shop.remove(item, quantity);
		}

		ItemStack itemStack = ItemStack.builder().itemType(item).quantity(quantity).build();

		player.getInventory().offer(itemStack);
		player.sendMessage(Text.of("%sYou have purchased %s%d %s%s %sfor %s%d%s!",
				TextColors.BLUE, TextColors.GREEN, quantity, TextColors.BLUE, TextColors.GREEN, (quantity != 1 ? MessageHandler.pluralize(item.getName()) : item.getName()),
				TextColors.BLUE, TextColors.GREEN, economy.getDefaultCurrency().format(totalPrice), TextColors.BLUE));
		return true;
	}

	// sell to shop
	public static boolean sell(Shop shop, Player player, ItemType item, int quantity) {
		if (!player.getInventory().contains(ItemStack.builder().itemType(item).quantity(quantity).build())) {
			player.sendMessage(Text.of("%sYou don't have enough of this item!", TextColors.RED));
			return false;
		}

		if (shop.getDesiredAmount(item) == -1) {
			player.sendMessage(Text.of("%sThis shop is not buying this item at this time!", TextColors.RED));
			return false;
		}

		if (shop.getDesiredAmount(item) < quantity) {
			player.sendMessage(Text.of("%sQuantity provided is more than the shop is willing to buy!", TextColors.RED));
			return false;
		}

		UniqueAccount account = POS.retrievePlayerAccount(player, economy);

		if (account == null) return false;

		BigDecimal totalPrice = shop.getBuyPrice(item).multiply(new BigDecimal(quantity));

		if (shop.getBalance().compareTo(totalPrice) < 0) {
			player.sendMessage(Text.of("%sThis shop has insufficient funds for this transaction!", TextColors.RED));
			return false;
		}

		if (!POS.sell(player, shop, account, totalPrice, economy));

		// TODO: remove item from player inventory
		shop.add(item, quantity);

		player.sendMessage(Text.of("%sYou have sold %s%d %s%s %sfor %s%d%s!",
				TextColors.BLUE, TextColors.GREEN, quantity, TextColors.BLUE, TextColors.GREEN, (quantity != 1 ? MessageHandler.pluralize(item.getName()) : item.getName()),
				TextColors.BLUE, TextColors.GREEN, economy.getDefaultCurrency().format(totalPrice), TextColors.BLUE));
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