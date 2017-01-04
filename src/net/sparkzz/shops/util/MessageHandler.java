package net.sparkzz.shops.util;

import org.spongepowered.api.text.Text;

/**
 * @author Brendon Butler
 */
public class MessageHandler {

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