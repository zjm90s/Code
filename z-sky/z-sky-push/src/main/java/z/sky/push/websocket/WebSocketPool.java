package z.sky.push.websocket;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebSocket连接池
 * 
 * @author jianming.zhou
 *
 */
public class WebSocketPool {
	
	private static Logger log = LoggerFactory.getLogger(WebSocketPool.class);
	
	/** 按用户分组 */
	private static Map<String, Set<Session>> sessionMap = new HashMap<String, Set<Session>>();
	/** 用于广播 */
	private static Set<Session> sessionSet = new HashSet<Session>();

	public static Map<String, Set<Session>> getAllSession() {
		return sessionMap;
	}
	
	public static Set<Session> getAllSessionSet() {
		Set<Session> allSession = new HashSet<Session>();
		for (Entry<String, Set<Session>> entry : sessionMap.entrySet()) {
			allSession.addAll(entry.getValue());
		}
		return allSession;
	}
	
	public static Set<Session> getSessionByUser(String user) {
		if (sessionMap.containsKey(user)) {
			return sessionMap.get(user);
		}
		return new HashSet<Session>();
	}
	
	public static int add(String user, Session session) {
		log.debug("Add new session for {}", user);
		sessionSet.add(session);
		if (sessionMap.containsKey(user)) {
			sessionMap.get(user).add(session);
		} else {
			Set<Session> sessionList = new HashSet<Session>();
			sessionList.add(session);
			sessionMap.put(user, sessionList);
		}
		return sessionMap.get(user).size();
	}
	
	public static int remove(String user, Session session) {
		log.debug("Delete session for {}", user);
		sessionSet.remove(session);
		if (sessionMap.containsKey(user)) {
			Set<Session> sessionList = sessionMap.get(user);
			sessionList.remove(session);
			return sessionList.size();
		} else {
			return 0;
		}
	}

}
