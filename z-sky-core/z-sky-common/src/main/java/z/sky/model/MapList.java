package z.sky.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Map&List组合数据结构
 * 
 * @author jianming.zhou
 *
 * @param <K>
 * @param <V>
 */
public class MapList<K, V> {

	private Map<K, List<V>> map;
	
	public MapList() {
		map = new HashMap<K, List<V>>();
	}
	
	/**
	 * 支持线程安全
	 * @param threadSafe true:线程安全 
	 */
	public MapList(boolean threadSafe) {
		if (threadSafe) {
			map = Collections.synchronizedMap(new HashMap<K, List<V>>());
		} else {
			map = new HashMap<K, List<V>>();
		}
	}
	
	public List<V> get(K k) {
		return map.get(k);
	}
	
	public void put(K k, V v) {
		if (map.containsKey(k)) {
			map.get(k).add(v);
		} else {
			List<V> list = new ArrayList<V>();
			list.add(v);
			map.put(k, list);
		}
	}
	
	public Set<K> keySet() {
		return map.keySet();
	}
	
	public Set<Entry<K,List<V>>> entrySet() {
		return map.entrySet();
	}
	
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	public int size() {
		int size = 0;
		for (Entry<K, List<V>> entry : map.entrySet()) {
			size += entry.getValue().size();
		}
		return size;
	}
	
	public void clear() {
		map.clear();
	}
}
