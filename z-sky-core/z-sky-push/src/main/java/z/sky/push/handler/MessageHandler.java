package z.sky.push.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息处理池
 * @author jianming.zhou
 *
 */
public class MessageHandler {
	
	private Logger logger = LoggerFactory.getLogger(MessageHandler.class);
	
	private ExecutorService pool;
	private int nThreads = 100;
	
	public void setnThreads(int nThreads) {
		this.nThreads = nThreads;
	}

	@PostConstruct
	public void init() {
		pool = Executors.newFixedThreadPool(nThreads);
	}

	public void push(String user, Session session, String message) {
		pool.execute(new PushThread(user, session, message));
	}
	
	private class PushThread implements Runnable {
		
		private String user;
		private Session session;
		private String message;
		
		public PushThread(String user, Session session, String message) {
			super();
			this.user = user;
			this.session = session;
			this.message = message;
		}

		@Override
		public void run() {
			try {
				if (!session.isOpen()) {
					logger.debug("session已断开连接，无法推送");
					return;
				}
				session.getBasicRemote().sendText(message);
				logger.info("PushThread user:{} message:{}", user, message);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
	}
	
}
