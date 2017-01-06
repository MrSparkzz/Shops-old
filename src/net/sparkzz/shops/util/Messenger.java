package net.sparkzz.shops.util;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * @author Brendon Butler
 */
public class Messenger {

	// command messages
	public static final Text
			CMD_ONLY_PLAYERS = Text.builder().color(TextColors.RED).append(Text.of("Only players can use this command!")).build();

	// shop messages
	public static final Text
			ERROR_ACCESSING_ACC = Text.builder().color(TextColors.RED).append(Text.of("Error accessing account!")).build(),
			INSUFFICIENT_FUNDS = Text.builder().color(TextColors.RED).append(Text.of("Insufficient funds!")).build(),
			INSUFFICIENT_FUNDS_SHOP = Text.builder().color(TextColors.RED).append(Text.of("This shop has insufficient funds!")).build(),
			INSUFFICIENT_INV_SPACE = Text.builder().color(TextColors.RED).append(Text.of("You don't have enough inventory space!")).build(),
			INSUFFICIENT_INV_STOCK = Text.builder().color(TextColors.RED).append(Text.of("You don't have enough of this item to sell!")).build(),
			INSUFFICIENT_STOCK = Text.builder().color(TextColors.RED).append(Text.of("This shop doesn't have enough of this item!")).build(),
			INVALID_ACC_BAL = Text.builder().color(TextColors.RED).append(Text.of("Invalid account balance!")).build(),
			NOT_BUYING = Text.builder().color(TextColors.RED).append(Text.of("This shop is not currently buying this item!")).build(),
			OTHER = Text.builder().color(TextColors.RED).append(Text.of("An error occurred while processing your transaction")).build(),
			YOU_PURCHASED = Text.builder().color(TextColors.BLUE).append(Text.of("&1You have purchased &a%s &1%s &afor &1%s&a!")).build(),
			YOU_SOLD = Text.builder().color(TextColors.GREEN).append(Text.of("&1You have sold &a%s &1%s &afor &1%s&a!")).build();

	public static Text format(Text text) {
		// TODO: replace text colors from legacy color codes

		// TODO: replace codes to TransactionEvent elements (might have to be done in the TransactionEvent/TransactionListener class)

		return text;
	}

	// TODO: replace with matcher? && improve compatibility
	public static String pluralize(String string) {
		if (string.endsWith("es") || string.endsWith("rs") || string.endsWith("ps") || string.endsWith("ds") || string.endsWith("gs") || string.endsWith("ts") || string.equalsIgnoreCase("fish") || string.equalsIgnoreCase("sheep"))
			return string;
		else if (string.endsWith("ch") || string.endsWith("sh") || string.endsWith("s") || string.endsWith("x") || string.endsWith("z"))
			return string.concat("es");
		else if (string.endsWith("ay") || string.endsWith("ey") || string.endsWith("oy") || string.endsWith("uy"))
			return string.concat("s");
		else if (string.endsWith("y"))
			return string.substring(0, string.length() - 1).concat("ies");
		else if (string.endsWith("f"))
			return string.substring(0, string.length() - 1).concat("ves");
		else if (string.endsWith("fe"))
			return string.substring(0, string.length() - 2).concat("ves");
		else if (string.endsWith("bo") || string.endsWith("co") || string.endsWith("do") || string.endsWith("fo") || string.endsWith("go") || string.endsWith("ho") || string.endsWith("jo") || string.endsWith("ko") || string.endsWith("lo") ||
				string.endsWith("mo") || string.endsWith("no") || string.endsWith("po") || string.endsWith("qo") || string.endsWith("ro") || string.endsWith("so") || string.endsWith("to") || string.endsWith("vo") || string.endsWith("wo") ||
				string.endsWith("xo") || string.endsWith("zo"))
			return string.concat("s");
		return string;
	}

	public static Text pluralize(Text text) {
		return Text.of(pluralize(text.toString()));
	}
}