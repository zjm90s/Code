package z.sky.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * LOG计数器
 * <p>用于减少相似日志的打印
 * 
 * @author zhoujianming
 *
 */
public class LogCounter {
	
	private static int INTERVAL = 1*60*1000;
	private static int MAX_COUNT = 100;
	
	private static Map<String, AtomicInteger> counter = new HashMap<>();
	
	static {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(INTERVAL);
					} catch (InterruptedException e) {
					}
					counter.clear();
				}
			}
		}).start();
	}
	
	public static boolean skip(String...key) {
		return skip(MAX_COUNT, key);
	}
	
	public static boolean skip(int maxCount, String...key) {
		String _key = parseKey(key);
		AtomicInteger count = counter.get(_key);
		if (count != null) {
			count.incrementAndGet();
		} else {
			counter.put(_key, new AtomicInteger(1));
		}
		return count.get() > maxCount;
	}
	
	private static String parseKey(String...key) {
		StringBuilder strb = new StringBuilder();
		for (String k : key) {
			strb.append("_").append(k);
		}
		return strb.toString();
	}

}
