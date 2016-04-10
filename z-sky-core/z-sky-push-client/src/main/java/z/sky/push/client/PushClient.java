package z.sky.push.client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import z.sky.push.base.model.MessageModel;
import z.sky.push.base.utils.SpringContext;

import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * 消息推送Client
 * 
 * @author jianming.zhou
 *
 */
public class PushClient {
	
	private Logger log = LoggerFactory.getLogger(PushClient.class);
	
	/** exchange名称 */
	private String exchangeName;
	
	@Resource
	private ConnectionFactory connectionFactory;
	
	private Channel channel;
	
	private static PushClient pushClient;
	
	private PushClient() {}
	
	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	@PostConstruct
	private void init() throws IOException, TimeoutException {
		Connection conn = connectionFactory.newConnection();
		channel = conn.createChannel();  
	}
	
	public static synchronized PushClient getInstance() {
		if (null == pushClient) {
			pushClient = SpringContext.getBean(PushClient.class);
		}
		return pushClient;
	}

	/**
	 * 异步消息推送接口
	 * @param model
	 * @throws AlreadyClosedException MQ服务连接中断
	 */
	public void messagePush(MessageModel model) throws AlreadyClosedException {
		try {
			channel.basicPublish(exchangeName, "", MessageProperties.PERSISTENT_TEXT_PLAIN, 
					SerializationUtils.serialize(model));  
			log.info("message:{}", model.toString());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (AlreadyClosedException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

}
