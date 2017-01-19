package net.sparkzz.shops.config;

import net.sparkzz.shops.util.ShopLoader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * <p>Yaml Configuration</p>
 *
 * From ModestAPI includes Validate
 * @author Brendon Butler
 */
public class YamlConfig implements Config {

	private boolean overwrite = false;
	private DumperOptions dumperOptions;
	private Path configFile;
	private FileReader reader;
	private FileWriter writer;
	private Map<String, Object> data;
	private Object tempObject;
	private Representer representer;
	private Yaml yaml;

	public YamlConfig(Path configDir, String fileName) {
		this.configFile = Paths.get(configDir.toString().substring(0, configDir.toString().lastIndexOf(File.separator)), fileName + ".yaml");

		setupDumper();

		yaml = new Yaml(representer, dumperOptions);
		load();
	}

	private void setupDumper() {
		int indent = 2;
		dumperOptions = new DumperOptions();
		representer = new Representer();

		dumperOptions.setIndent(indent);
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		dumperOptions.setAllowUnicode(Charset.defaultCharset().name().contains("UTF"));
		representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
	}

	@Override
	public BigDecimal getBigDecimal(String key) {
		tempObject = get(key);

		if (tempObject instanceof BigDecimal)
			return (BigDecimal) tempObject;
		if (tempObject instanceof Double)
			return new BigDecimal((Double) tempObject);
		if (tempObject instanceof String)
			if (isDecimalNumber(tempObject.toString()))
				return new BigDecimal(tempObject.toString());
		if (tempObject instanceof Number)
			return new BigDecimal(tempObject.toString());
		return new BigDecimal(-1);
	}

	public boolean contains(String key) {
		return data.containsKey(key);
	}

	public boolean exists() {
		return data != null;
	}

	public boolean getBoolean(String key) {
		tempObject = get(key);

		if (tempObject instanceof Boolean)
			return (Boolean) tempObject;
		if (tempObject instanceof String)
			if (tempObject.toString().equalsIgnoreCase("true") || tempObject.toString().equalsIgnoreCase("1"))
				return true;
			else if (tempObject.toString().equalsIgnoreCase("false") || tempObject.toString().equalsIgnoreCase("0"))
				return false;
		if (tempObject instanceof Number)
			if (((Number) tempObject).intValue() == 1)
				return true;
			else if (((Number) tempObject).intValue() == 0)
				return false;
		return false;
	}

	public boolean hasValue(String key) {
		tempObject = data.get(key);

		return (tempObject != null);
	}

	public boolean isEmpty() {
		return data.isEmpty() || data == null;
	}

	/**
	 * <p>Validates whether or not a string is a decimal number.</p>
	 *
	 * <p>See: {@link Double}, {@link Float}.</p>
	 *
	 * @param string Input {@link String}.
	 * @return {@code true} if input is a decimal number.
	 */
	public static boolean isDecimalNumber(String string) {
		if (string == null) {
			return false;
		}

		int length = string.length();

		if (length == 1 ) {
			return false;
		}

		int i = 0;

		if (string.charAt(0) == '-') {
			if (length < 3) {
				return false;
			}
			i = 1;
		}

		int numOfDot = 0;

		for (; i < length; i++) {
			char c = string.charAt(i);
			if (c == '.')
				numOfDot++;
			else if (c == '/')
				return false;
			else if (c < '.' || c > '9') {
				return false;
			}
		}

		return (numOfDot == 1);
	}

	/**
	 * <p>Validates whether or not a string is a number.</p>
	 *
	 * @param string Input {@link String}.
	 * @return {@code true} if input is a number.
	 */
	public static boolean isNumber(String string) {
		if (string == null) return false;

		int length = string.length();

		if (length == 0) return false;

		int i = 0;

		if (string.charAt(0) == '-') {
			if (length == 1) return false;

			i = 1;
		}

		for (; i < length; i++) {
			char c = string.charAt(i);

			if (c <= '/' || c >= ':') return false;
		}
		return true;
	}

	public byte getByte(String key) {
		tempObject = get(key);

		if (tempObject instanceof Byte)
			return (Byte) tempObject;
		if (tempObject instanceof String)
			if (isNumber(tempObject.toString()))
				return Byte.parseByte(tempObject.toString());
		if (tempObject instanceof Number)
			return Byte.parseByte(tempObject.toString());
		return -1;
	}

	public Collection<?> getCollection(String key) {
		tempObject = get(key);

		if (tempObject instanceof Collection<?>)
			return (Collection) tempObject;
		return null;
	}

	public char getChar(String key) {
		tempObject = get(key);

		if (tempObject instanceof Character)
			return (Character) tempObject;
		if (tempObject instanceof String)
			return tempObject.toString().charAt(0);
		if (tempObject instanceof Number)
			return tempObject.toString().charAt(0);
		return '\u0000';
	}

	public double getDouble(String key) {
		tempObject = get(key);

		if (tempObject instanceof Double)
			return (Double) tempObject;
		if (tempObject instanceof String)
			if (isDecimalNumber(tempObject.toString()))
				return Double.parseDouble(tempObject.toString());
		if (tempObject instanceof Number)
			return Double.parseDouble(tempObject.toString());
		return -1;
	}

	public int getInteger(String key) {
		tempObject = get(key);

		if (tempObject instanceof Integer)
			return (Integer) tempObject;
		if (tempObject instanceof String)
			if (isNumber(tempObject.toString()))
				return Integer.parseInt(tempObject.toString());
		if (tempObject instanceof Number)
			return Integer.parseInt(tempObject.toString());
		return -1;
	}

	public List<?> getList(String key) {
		tempObject = get(key);

		if (tempObject instanceof List<?>)
			return (List) tempObject;
		return null;
	}

	public long getLong(String key) {
		tempObject = get(key);

		if (tempObject instanceof Long)
			return (Long) tempObject;
		if (tempObject instanceof String)
			if (isNumber(tempObject.toString()))
				return Long.parseLong(tempObject.toString());
		if (tempObject instanceof Number)
			return Long.parseLong(tempObject.toString());
		return -1;
	}

	public Map<?, ?> getMap(String key) {
		tempObject = get(key);

		if (tempObject instanceof Map<?, ?>)
			return (Map) tempObject;
		return null;
	}

	public Map<String, Object> getValues() {
		if (!isEmpty())
			return data;
		return null;
	}

	public Object get(String key) {
		if (isEmpty())
			return null;

		final String[] nodes = key.split("\\.");
		Map curMap = data;

		for (int i = 0; i <= nodes.length - 1; ++i) {
			Object child = curMap.get(nodes[i]);

			if (child == null) return null;
			else if (!(child instanceof Map)) {
				if (i == nodes.length - 1)
					return child;
				else return null;
			}

			curMap = (Map) child;
		}
		return null;
	}

	public Path getSaveLocation() {
		return configFile;
	}

	public Set<?> getSet(String key) {
		tempObject = get(key);

		if (tempObject instanceof Set<?>)
			return (Set) tempObject;
		return null;
	}

	public Set<String> getKeys() {
		if (!isEmpty())
			return data.keySet();
		return new HashSet<>();
	}

	public short getShort(String key) {
		tempObject = get(key);

		if (tempObject instanceof Short)
			return (Short) tempObject;
		if (tempObject instanceof String)
			if (isNumber(tempObject.toString()))
				return Short.parseShort(tempObject.toString());
		if (tempObject instanceof Number)
			return Short.parseShort(tempObject.toString());
		return -1;
	}

	public String getString(String key) {
		tempObject = get(key);

		if (tempObject instanceof String)
			return (String) tempObject;
		return null;
	}

	@Override
	public UUID getUUID(String key) {
		tempObject = get(key);

		if (tempObject instanceof UUID)
			return (UUID) tempObject;
		if (tempObject instanceof String)
			return UUID.fromString(tempObject.toString());
		return null;
	}

	public Vector<?> getVector(String key) {
		tempObject = get(key);

		if (tempObject instanceof Vector<?>)
			return (Vector) tempObject;
		return null;
	}

	public void set(String key, Object object) {
		if (!exists())
			return;

		final String[] nodes = key.split("\\.");
		Map curMap = data;

		for (int i = 0; i <= nodes.length - 2; ++i) {
			Object child = curMap.get(nodes[i]);

			if (child == null) child = new LinkedHashMap();
			else if (!(child instanceof Map)) {
				if (!overwrite) return;
				child = new LinkedHashMap();
			}

			curMap.put(nodes[i], child);
			curMap = (Map) child;
		}
		curMap.put(nodes[nodes.length - 1], object);
	}

	public void setProtection(boolean value) {
		overwrite = !value;
	}

	public void load() {
		try {
			if (!Files.exists(configFile))
				return; // TODO: Error when loading

			reader = new FileReader(configFile.toFile());

			data = new LinkedHashMap((Map) yaml.load(reader));
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		}
	}

	public void reload() {
		save();
		load();
	}

	public void save() {
		if (data.isEmpty()) return;

		try {
			if (!Files.exists(configFile))
				Files.createFile(configFile);

			writer = new FileWriter(configFile.toFile());

			writer.write(yaml.dump(data));
			writer.flush();
			writer.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}