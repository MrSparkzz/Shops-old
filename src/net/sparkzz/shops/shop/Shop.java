package net.sparkzz.shops.shop;

import org.spongepowered.api.item.ItemType;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Brendon Butler
 */
public class Shop {

	private BigDecimal balance;
	private boolean infiniteFunds = false;
	private boolean infiniteStock = false;
	private List<ItemType> items;
	private Map<ItemType, Map<String, Object>> inventory;
	private String name;
	private UUID ownerID, shopID;

	// To be used when creating a shop for the first time
	public Shop(String name, UUID ownerID) {
		this.name = name;
		this.balance = new BigDecimal(0);
		this.inventory = new HashMap<>();
		this.items = new ArrayList<ItemType>(inventory.keySet()) {
			public boolean add(ItemType item) {
				super.add(item);
				Collections.sort(items, (first, second) -> first.getName().compareTo(second.getName()));
				return true;
			}
		};
		this.shopID = UUID.randomUUID();
		this.ownerID = ownerID;
	}

	// To be used when loading a shop
	public Shop(UUID shopID, UUID ownerID, String name, BigDecimal balance, boolean infiniteFunds, boolean infiniteStock, Map<ItemType, Map<String, Object>> inventory) {
		this.shopID = shopID;
		this.ownerID = ownerID;
		this.name = name;
		this.balance = balance;
		this.infiniteFunds = infiniteFunds;
		this.infiniteStock = infiniteStock;
		this.inventory = inventory;
		this.items = new ArrayList<ItemType>(inventory.keySet()) {
			public boolean add(ItemType item) {
				super.add(item);
				Collections.sort(items, (first, second) -> first.getName().compareTo(second.getName()));
				return true;
			}
		};
	}

	public boolean contains(ItemType item) {
		return inventory.containsKey(item);
	}

	public boolean hasInfiniteFunds() {
		return infiniteFunds;
	}

	public boolean hasInfiniteStock() {
		return infiniteStock;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public BigDecimal getBuyPrice(ItemType item) {
		return new BigDecimal(inventory.get(item).get("buy-price").toString());
	}

	public BigDecimal getSalePrice(ItemType item) {
		return new BigDecimal(inventory.get(item).get("sale-price").toString());
	}

	public int getDesiredAmount(ItemType item) {
		return Integer.parseInt(inventory.get(item).get("desired").toString());
	}

	public int getRemaining(ItemType item) {
		if (infiniteStock) return -1;

		return Integer.parseInt(inventory.get(item).get("quantity").toString());
	}

	public int getID(ItemType item) {
		return items.indexOf(item) + 1;
	}

	public ItemType getItemById(int id) {
		return items.get(id - 1);
	}

	public Map<ItemType, Map<String, Object>> getInventory() {
		return inventory;
	}

	public String getName() {
		return name;
	}

	public UUID getOwnerID() {
		return ownerID;
	}

	public UUID getShopID() {
		return shopID;
	}

	public void add(ItemType item, int quantity) {
		inventory.get(item).put("quantity", getRemaining(item) + quantity);
		items.add(item);
	}

	public void add(ItemType item, int quantity, int desired, BigDecimal salePrice, BigDecimal buyPrice) {
		if (!inventory.containsKey(item)) {
			Map<String, Object> itemData = new HashMap<>();

			itemData.put("quantity", quantity);
			itemData.put("desired", desired);
			itemData.put("sale-price", salePrice);
			itemData.put("buy-price", buyPrice);

			inventory.put(item, itemData);
			items.add(item);
		}
	}

	public void deposit(BigDecimal amount) {
		balance = balance.add(amount);
	}

	public void remove(ItemType item, int quantity) {
		if (getRemaining(item) != -1 || infiniteStock)
			inventory.get(item).put("quantity", (int) inventory.get(item).get("quantity") - quantity);
	}

	public void withdraw(BigDecimal amount) {
		if (!infiniteFunds)
			balance = balance.subtract(amount);
	}

	public void adjustDesiredAmount(ItemType item, int quantity) {
		if (getDesiredAmount(item) != -1)
			inventory.get(item).put("desired", (int) inventory.get(item).get("desired") - quantity);
	}
}