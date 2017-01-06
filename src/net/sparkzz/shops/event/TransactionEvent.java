package net.sparkzz.shops.event;

import net.sparkzz.shops.shop.Shop;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.living.humanoid.player.TargetPlayerEvent;
import org.spongepowered.api.event.impl.AbstractEvent;
import org.spongepowered.api.item.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Brendon Butler
 */
public class TransactionEvent extends AbstractEvent implements Cancellable {

	public enum Reason {
		NONE, INSUFFICIENT_FUNDS, INSUFFICIENT_STOCK, INVALID_ACC_BAL, INSUFFICIENT_INV_SPACE, MORE_THAN_DESIRED, NOT_BUYING, OTHER
	}

	public enum Status {
		FAILED, SUCCESS, INTERRUPTED
	}

	public enum TransactionType {
		REFUND, SALE, SALE_TO_SHOP
	}

	private final BigDecimal cost;
	private final Cause cause;
	private final Date dateTime;
	private final ItemStack item;
	private final Player customer;
	private final Reason reason;
	private final Shop shop;
	private final Status status;
	private final TransactionType transactionType;
	private boolean cancelled = false;

	public TransactionEvent (Shop shop, Player customer, ItemStack item, TransactionType transactionType, Status status, Cause cause) {
		this.dateTime = new Date();
		this.shop = shop;
		this.customer = customer;
		this.item = item;
		this.transactionType = transactionType;
		this.status = status;
		this.reason = Reason.NONE;
		this.cause = cause;
		this.cost = (transactionType == TransactionType.SALE_TO_SHOP ?
				shop.getBuyPrice(item.getItem().getType()).multiply(new BigDecimal(item.getQuantity())) :
				shop.getSalePrice(item.getItem().getType()).multiply(new BigDecimal(item.getQuantity())));
	}

	public TransactionEvent (Shop shop, Player customer, ItemStack item, TransactionType transactionType, Status status, Reason reason, Cause cause) {
		this.dateTime = new Date();
		this.shop = shop;
		this.customer = customer;
		this.item = item;
		this.transactionType = transactionType;
		this.status = status;
		this.reason = reason;
		this.cause = cause;
		this.cost = (transactionType == TransactionType.SALE_TO_SHOP ?
				shop.getBuyPrice(item.getItem().getType()).multiply(new BigDecimal(item.getQuantity())) :
				shop.getSalePrice(item.getItem().getType()).multiply(new BigDecimal(item.getQuantity())));
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public BigDecimal getCost() {
		return this.cost;
	}

	public Cause getCause() {
		return this.cause;
	}

	public Date getDateTime() {
		return this.dateTime;
	}

	public ItemStack getItemStack() {
		return this.item;
	}

	public Player getCustomer() {
		return this.customer;
	}

	public Reason getReason() {
		return this.reason;
	}

	public Shop getShop() {
		return this.shop;
	}

	public Status getStatus() {
		return this.status;
	}

	public TransactionType getTransactionType() {
		return this.transactionType;
	}
}