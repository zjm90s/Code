package z.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 * @author jianming.zhou
 *
 */
public class Md5Util {

	public static String MD5(String source) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer();
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return "";
	}
}
