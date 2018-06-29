package com.mybatis.plugin;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	private StringUtil() {

	}

	/**
	 * 判断是不是数值型
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		return matcherCaseInsensitive("^[-\\+]?[\\d]*$", str).matches();
	}

	/**
	 * 正则替换
	 * 
	 * @param str
	 *            需要替换字符串
	 * @param regex
	 *            正则表达式
	 * @param map
	 *            匹配字段 需要进行替换的值
	 * @return
	 */
	public static String replaceMatch(String str, String regex, Map<String, Object> map) {
		Matcher matcher;
		String text = str;
		Object obj;
		while ((matcher = matcherCaseInsensitive(regex, text)).find()) {
			obj = map.get(matcher.group(1).trim());
			if (obj == null) {
				text = matcher.replaceFirst("");
			} else {
				text = matcher.replaceFirst(obj.toString());
			}
		}
		return text;
	}

	/**
	 * 正则替换
	 * 
	 * @param str
	 *            替换字符串
	 * @param regex
	 *            正则表达式
	 * @param replaceStr
	 *            替换后的值
	 * @return
	 */
	public static String replaceAllMatch(String str, String regex, String replaceStr) {
		String text = str;
		Matcher matcher = matcherCaseInsensitive(regex, text);
		if (matcher.find()) {
			text = matcher.replaceAll(replaceStr);
		}
		return text;
	}

	/**
	 * 忽略大小写
	 * 
	 * @param regex
	 *            匹配正则
	 * @param str
	 *            匹配字符串
	 * @return
	 */
	public static Matcher matcherCaseInsensitive(String regex, String str) {
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		return pattern.matcher(str);
	}

	/**
	 * 获得驼峰字段 case '_': case '-': case '@': case '$': case '#': case ' ': case '/':
	 * case '&':
	 * 
	 * @param inputString
	 * @param firstCharacterUppercase
	 * @return
	 */
	public static String getCamelCaseString(String inputString, boolean firstCharacterUppercase) {
		StringBuilder sb = new StringBuilder();

		boolean nextUpperCase = false;
		for (int i = 0; i < inputString.length(); i++) {
			char c = inputString.charAt(i);

			switch (c) {
			case '_':
			case '-':
			case '@':
			case '$':
			case '#':
			case ' ':
			case '/':
			case '&':
			case ':':
				if (sb.length() > 0) {
					nextUpperCase = true;
				}
				break;

			default:
				if (nextUpperCase) {
					sb.append(Character.toUpperCase(c));
					nextUpperCase = false;
				} else {
					sb.append(Character.toLowerCase(c));
				}
				break;
			}
		}

		if (firstCharacterUppercase) {
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		}

		return sb.toString();
	}
}
