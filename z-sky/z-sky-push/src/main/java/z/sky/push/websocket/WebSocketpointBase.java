package z.sky.push.websocket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 子类@ServerEndpoint url中需包含{token}
 * 
 * @author jianming.zhou
 *
 */
public abstract class WebSocketpointBase {
	
	private Logger logger = LoggerFactory.getLogger(WebSocketpointBase.class);
	
	/**
	 * Client端发送消息时会触发
	 * <p>暂时不做任何处理
	 * 
	 * @param session
	 * @param message
	 * @throws IOException
	 */
	@OnMessage
    public void onMessage(Session session, String message) throws IOException {
		String user = getUserBySession(session);
		logger.info("Received from user[{}]: {}", user, message);
    }
	
	@OnOpen
    public void onOpen(Session session) {
        logger.debug("Client connected");
        String user = getUserBySession(session);
        if (!StringUtils.isBlank(user)) {
        	int size = WebSocketPool.add(user, session);
        	logger.info("open {}'s session size : {}", user, size);
        }
    }

    @OnClose
    public void onClose(Session session) {
    	logger.debug("Connection closed");
    	String user = getUserBySession(session);
    	if (!StringUtils.isBlank(user)) {
    		int size = WebSocketPool.remove(user, session);
    		logger.info("close {}'s session size : {}", user, size);
    	}
    }
    
    @OnError
    public void onError(Throwable throwable) {
    	logger.error(throwable.getMessage(), throwable);
    }
    
    private String getUserBySession(Session session) {
    	String token = session.getPathParameters().get("token");
    	String user = null;
    	try {
    		user = getUserByToken(token);
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    	}
    	return user;
    }
    
    /**
     * 根据token获取用户，无则返回null
     * 
     * @param token
     * @return
     */
    protected abstract String getUserByToken(String token);
}
