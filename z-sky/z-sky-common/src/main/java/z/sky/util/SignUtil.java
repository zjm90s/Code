package z.sky.util;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Hex;

public class SignUtil {

	/**
	 * sign生成
	 * 
	 * @param obj 对象
	 * @param key 密钥
	 * @return
	 * @throws Exception
	 */
	public synchronized static String genSign(Object obj, String key) throws Exception {
		Map<String, Object> params = getValueMap(obj);
		return encryptToken(params, key);
	}

	private static Map<String, Object> getValueMap(Object obj) throws Exception {
		Map<String, Object> map = new TreeMap<String, Object>();
		Field[] fields = obj.getClass().getDeclaredFields();
		copyFieldToMap(obj, map, fields);
		Field[] superoFields = obj.getClass().getSuperclass().getDeclaredFields();
		copyFieldToMap(obj, map, superoFields);
		return map;
	}

	private static void copyFieldToMap(Object obj, Map<String, Object> map, Field[] fields) throws Exception {
		for (Field field : fields) {
            Class<?> fieldType = field.getType();
            String fieldTypeName = fieldType.getSimpleName();
			if ("List".equals(fieldTypeName)) {
				continue;
			} // TODO sign for List, Map, Bean...
			field.setAccessible(true);
			Object value = field.get(obj);
			if (value != null) {
				map.put(field.getName(), value);
			}
		}
		map.remove("serialVersionUID");
		map.remove("sign");
	}

	private static String encryptToken(Map<String, Object> params, String key) throws Exception {
		String s = formatList(params) + key;
		return sha256Encode(s);
	}

	private static String formatList(Map<String, Object> params) {
		if (params == null || params.isEmpty()) {
			return "";
		}
		TreeMap<String, Object> map = null;
		if (params instanceof TreeMap) {
			map = (TreeMap<String, Object>) params;
		} else {
			map = new TreeMap<>(params);
		}
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sb.append(entry.getKey()).append("=").append(String.valueOf(entry.getValue())).append("&");
		}
		String str = sb.toString();
		return str.substring(0, str.length() - 1);
	}
	
	private static String sha256Encode(String message) throws Exception {
		String encode = null;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		encode = Hex.encodeHexString(md.digest(message.getBytes()));
		return encode;
	}
}