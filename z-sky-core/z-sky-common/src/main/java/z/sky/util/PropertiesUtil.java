package z.sky.util;

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
	 * 文件读取
	 * <p>支持绝对和相对路径
	 * @param file 文件名
	 * @return
	 */
	public static Properties read(String file) {
		Properties props = new Properties();
        try {
			props.load(new BufferedReader(new InputStreamReader(new FileInputStream(file), CODE)));
		} catch (IOException e) {
			e.printStackTrace();
		}
        return props;
	}
	
	/**
	 * 资源文件读取
	 * <p>用于读取resources下文件
	 * @param file 文件名
	 * @return
	 */
	public static Properties read4Res(String file) {
		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream("/" + file);
		try {
			prop.load(new InputStreamReader(in, CODE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
}
