package z.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Properties工具类
 * @author jianming.zhou
 *
 */
public class PropertiesUtil {
	
	private final static String CODE = "utf-8";
	
	/**
	 *读取文件数据到Properties中
	 * <p>例如[file:/xxx.properties]
	 * @param file
	 * @return
	 */
	public static Properties read(String file) {
		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream(file);
		try {
			prop.load(new InputStreamReader(in, CODE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	public static Properties readII(String file) {
		Properties props = new Properties();
        try {
			props.load(new BufferedReader(new InputStreamReader(new FileInputStream(file), CODE)));
		} catch (IOException e) {
			e.printStackTrace();
		}
        return props;
	}

}
