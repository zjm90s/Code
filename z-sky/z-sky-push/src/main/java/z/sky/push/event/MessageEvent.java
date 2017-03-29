package z.sky.push.event;

import javax.websocket.Session;

import org.springframework.context.ApplicationEvent;

/**
 * 消息事件体
 * <p>现在已不基于事件
 * 
 * @author jianming.zhou
 *
 */
@Deprecated
public class MessageEvent extends ApplicationEvent  {
	private static final long serialVersionUID = 1L;
	
	private Session session;
	private String message;

	public MessageEvent(Object source, Session session, String message) {
		super(source);
		this.session = session;
		this.message = message;
	}

	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
