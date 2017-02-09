package z.sky.util;

import redis.clients.jedis.Jedis;

/**
 * 分布式锁Demo
 * <p>Jedis需加连接池
 * @author jianming.zhou
 *
 */
public class RedisLock {
	
	private static final String PRE = "lock_";
	
	/**
	 * 加锁
	 * @param lockName
	 * @return
	 */
	public static boolean tryLock(String lockName){
        return SpringContext.getBean(Jedis.class).setnx(PRE + lockName, "lock") == 1;
    }
	
	/**
	 * 加锁
	 * @param lockName
	 * @param time 失效时间 seconds
	 * @return
	 */
	public static boolean tryLock(String lockName, long time){
		return "OK".equals(SpringContext.getBean(Jedis.class).set(PRE + lockName, "lock", "NX", "EX", time));
	}
	
	/**
	 * 解锁
	 * <p>需考虑死锁问题
	 * @param lockName
	 * @return
	 */
	public static boolean unLock(String lockName){
		return SpringContext.getBean(Jedis.class).del(PRE + lockName) == 1;
	}

}
