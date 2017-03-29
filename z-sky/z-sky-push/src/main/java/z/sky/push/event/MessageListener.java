package z.sky.push.event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

/**
 * 消息异步处理
 * <p>现在已不基于事件
 * 
 * @author jianming.zhou
 *
 */
@Deprecated
public class MessageListener implements ApplicationListener<MessageEvent> {
	
	private Logger logger = LoggerFactory.getLogger(MessageListener.class);
	
	private ExecutorService pool;
	private int nThreads = 100;
	
	private MessageListener() {
		pool = Executors.newFixedThreadPool(nThreads);
	}

	@Override
	public void onApplicationEvent(MessageEvent event) {
		pool.execute(new PushThread(event.getSession(), event.getMessage()));
	}
	
	private class PushThread implements Runnable {
		
		private Session session;
		private String message;
		
		public PushThread(Session session, String message) {
			super();
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
				logger.debug("PushThread message:{}", message);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
	}
	
}
