package z.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import z.model.Pair;

/**
 * 工具类
 * <p>可利用lambda表达式至最简
 * @author jianming.zhou
 *
 */
public class Utils {

	/**
	 * 数据过滤
	 * 
	 * @param list
	 * @param filter
	 * @return
	 */
	public static <T> List<T> filter(List<T> list, IFilter<T> filter) {
		List<T> result = new ArrayList<T>();
		if (list != null && !list.isEmpty()) {
			for (T t : list) {
				if (filter.filter(t)) {
					result.add(t);
				}
			}
		}
		return result;
	}
	
	public interface IFilter<T> {
		public boolean filter(T e);
	}
	
	/**
	 * 数据转换
	 * 
	 * @param list
	 * @param transformer
	 * @return
	 */
	public static <E, T> List<T> transform(Collection<E> list, ITransformer<E, T> transformer) {
		List<T> result = new ArrayList<T>();
		if (list != null && !list.isEmpty()) {
			for (E e : list) {
				result.add(transformer.transformer(e));
			}
		}
		return result;
	}
	
	public interface ITransformer<E, T> {
		public T transformer(E e);
	}
	
	/**
	 * 对集合中数据根据"主键"去重
	 * 
	 * @param list
	 * @param filterSamer	转换器
	 * @return	返回集合中去掉重复部分的数据（如有重复，后者会覆盖前者）
	 */
	public static <K, T> List<T> filterSame(Collection<T> list, IFilterSamer<K, T> filterSamer) {
		List<T> result = new ArrayList<T>();
		if (list == null || list.isEmpty())
			return result;
		Map<K, T> map = new LinkedHashMap<K, T>();
		for (T t : list) {
			if (t != null) {
				Pair<K, T> pair = filterSamer.filterSame(t);
				map.put(pair.first, pair.second);
			}
		}
		for (K key : map.keySet()) {
			result.add(map.get(key));
		}
		return result;
	}
	
	public interface IFilterSamer<K, T> {
		public Pair<K, T> filterSame(T t);
	}
	
}
