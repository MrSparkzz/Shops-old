package net.sparkzz.shops.util;

import net.sparkzz.shops.Shops;
import net.sparkzz.shops.config.Config;
import net.sparkzz.shops.shop.Shop;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import java.util.*;

/**
 * @author Brendon Butler
 */
public class ShopLoader {

	public static Map<UUID, Shop> shops;

	public static void load(Shops plugin, Config config) {
		shops = new HashMap<>();

		if (!config.exists() || config.isEmpty()) {
			plugin.getLogger().info("Shop configurations not found!");
			return;
		}

		Shop curShop;

		for (String key : config.getKeys()) {
			UUID uuid = UUID.fromString(key);

			Map<ItemType, Map<String, Object>> inventory = new HashMap<>();

			for (String itemDataRaw : (List<String>) config.getList(uuid + ".items")) {
				String[] data = itemDataRaw.replaceAll("\\s+", "").split(",");

				Optional<ItemType> itemTypeOptional = Sponge.getRegistry().getType(ItemType.class, data[0]);

				if (!itemTypeOptional.isPresent())
					continue;

				ItemType itemType = itemTypeOptional.get();

				inventory.put(itemType, new HashMap<String, Object>() {{
					put("quantity", data[1]);
					put("desired", data[2]);
					put("sale-price", data[3]);
					put("buy-price", data[4]);
				}});
			}

			curShop = new Shop(
					uuid,
					config.getUUID(uuid + ".owner-id"),
					config.getString(uuid + ".name"),
					config.getBigDecimal(uuid + ".balance"),
					config.getBoolean(uuid + ".infinite.funds"),
					config.getBoolean(uuid + ".infinite.stock"),
					inventory
			);

			plugin.getLogger().debug(String.format("Adding Shop: %s (%s)", curShop.getName(), curShop.getShopID()));
			shops.put(uuid, curShop);
		}
	}

	public static void save(Shops plugin, Config config) {
		for (Shop shop : shops.values()) {
			UUID id = shop.getShopID();
			config.set(id + ".name", shop.getName());
			config.set(id + ".owner-id", shop.getOwnerID().toString());
			config.set(id + ".balance", shop.getBalance().toPlainString());
			config.set(id + ".infinite.funds", shop.hasInfiniteFunds());
			config.set(id + ".infinite.stock", shop.hasInfiniteStock());

			List items = new ArrayList<String>();

			shop.getInventory().forEach((item, data) -> {{
				StringBuilder builder = new StringBuilder();
				builder.append(item.getName());
				builder.append("," + data.get("quantity"));
				builder.append("," + data.get("desired"));
				builder.append("," + data.get("sale-price"));
				builder.append("," + data.get("buy-price"));
				items.add(builder.toString());
			}});

			config.set(id + ".items", items);
		}

		config.save();
	}
}