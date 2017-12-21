package z.sky.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 异步请求工具类
 * @author zhoujianming
 *
 */
public class AsynHelper {
	
	private static ExecutorService pool = Executors.newCachedThreadPool();
	
	public static <T> Future<T> submit(Callable<T> callable) {
		Future<T> future = pool.submit(callable);
		return future;
	}
	
	public static <T> Futurer<T> submitNoThrow(Callable<T> callable) {
		Future<T> future = pool.submit(callable);
		return new Futurer<T>(future);
	}
	
	public static class Futurer<V> {
		
		private Future<V> future;

		public Futurer(Future<V> future) {
			this.future = future;
		}
		
		public V get() {
			try {
				return future.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}

}
