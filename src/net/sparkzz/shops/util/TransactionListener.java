package net.sparkzz.shops.util;

import org.spongepowered.api.event.Listener;

/**
 * @author Brendon Butler
 */
public class TransactionListener {

	@Listener
	public void onTransactionEvent(TransactionEvent event) {
		if (event.getStatus() == TransactionEvent.Status.FAILED || event.getStatus() == TransactionEvent.Status.INTERRUPTED) {
			switch (event.getReason()) {
				case INSUFFICIENT_FUNDS:
					if (event.getTransactionType() == TransactionEvent.TransactionType.SALE)
						event.getCustomer().sendMessage(Messenger.INSUFFICIENT_FUNDS);
					if (event.getTransactionType() == TransactionEvent.TransactionType.SALE_TO_SHOP)
						event.getCustomer().sendMessage(Messenger.INSUFFICIENT_FUNDS_SHOP);
					break;
				case INSUFFICIENT_INV_SPACE:
					event.getCustomer().sendMessage(Messenger.INSUFFICIENT_INV_SPACE);
					break;
				case INSUFFICIENT_STOCK:
					if (event.getTransactionType() == TransactionEvent.TransactionType.SALE)
						event.getCustomer().sendMessage(Messenger.INSUFFICIENT_STOCK);
					if (event.getTransactionType() == TransactionEvent.TransactionType.SALE_TO_SHOP)
						event.getCustomer().sendMessage(Messenger.INSUFFICIENT_INV_STOCK);
					break;
				case INVALID_ACC_BAL:
					event.getCustomer().sendMessage(Messenger.INVALID_ACC_BAL);
					break;
				default:
					event.getCustomer().sendMessage(Messenger.OTHER);
					break;
			}
		}

		//TODO: log data
	}
}