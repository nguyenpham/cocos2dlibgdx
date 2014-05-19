package org.cclgdx.utils;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/*
 * Plist parser.
 * Supports:
 *  - string     -> String
 *  - integer    -> Integer
 *  - real       -> Double
 *  - date       -> Date string
 *  - true/false -> Boolean
 *  - data       -> string (not byte[])
 *  - dict       -> HashMap<String, Object>
 *  - array      -> ArrayList<Object>
 */

public class PlistParser {

	// for tag name
	private static final String EL_KEY = "key";
	private static final String EL_STRING = "string";
	private static final String EL_INTEGER = "integer";
	private static final String EL_DATA = "data";
	private static final String EL_DATE = "date";
	private static final String EL_REAL = "real";
	private static final String EL_TRUE = "true";
	private static final String EL_FALSE = "false";

	private static final String EL_DICT = "dict";
	private static final String EL_ARRAY = "array";

	public static HashMap<String, Object> parse(String filename) {
		try {
			XmlReader reader = new XmlReader();
			Element root = reader.parse(Gdx.files.internal(filename));
			Array<Element> dictArray = root.getChildrenByName(EL_DICT);
			if (dictArray != null && dictArray.size > 0) {

				Element dict = dictArray.get(0);
				int numberOfChildren = dict.getChildCount();

				HashMap<String, Object> map = new HashMap<String, Object>();
				// Array<Element> frames = root.getChildrenByName("frames");

				String key = null;
				for (int i = 0; i < numberOfChildren; i++) {
					Element child = dict.getChild(i);

					if (child.getName().equals(EL_KEY)) {
						key = child.getText();
					} else if (child.getName().equals(EL_DICT)) {
						HashMap<String, Object> subMap = parseDict(child);
						map.put(key, subMap);
					}
				}

				return map;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private static HashMap<String, Object> parseDict(Element dict) {
		assert (dict != null && dict.getName().equals(EL_DICT));
		HashMap<String, Object> dictMap = new HashMap<String, Object>();

		int n = dict.getChildCount();

		String key = null;
		for (int i = 0; i < n; i++) {
			Element child = dict.getChild(i);
			if (child.getName().equals(EL_KEY)) {
				key = child.getText();
			} else if (child.getName().equals(EL_STRING)) {
				dictMap.put(key, child.getText());
			} else if (child.getName().equals(EL_INTEGER)) {
				Integer num = new Integer(child.getText());
				dictMap.put(key, num);
			} else if (child.getName().equals(EL_DICT)) {
				HashMap<String, Object> subMap = parseDict(child);
				dictMap.put(key, subMap);
			} else if (child.getName().equals(EL_REAL)) {
				dictMap.put(key, new Float(child.getText()));
			} else if (child.getName().equals(EL_TRUE) || child.getName().equals(EL_FALSE)) {
				dictMap.put(key, new Boolean(child.getName()));
			} else if (child.getName().equals(EL_DATE) || child.getName().equals(EL_DATA)) {
				dictMap.put(key, child.getText());
			}
		}

		return dictMap;
	}
}
