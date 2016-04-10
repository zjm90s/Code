package z.sky.push.listener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import z.sky.push.base.model.MessageModel;
import z.sky.push.handler.MessageHandler;
import z.sky.push.websocket.WebSocketPool;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * MQ监听
 * @author jianming.zhou
 *
 */
public class MQListener {
	
	private Logger logger = LoggerFactory.getLogger(MQListener.class);
	
	/** exchange名称 */
	private String exchangeName;
	/** queue名称前缀 */
	private String queueNamePre;
	
	@Resource
	private ConnectionFactory connectionFactory;
	@Resource
	private MessageHandler messageHandler;
	
	private Connection conn;
	
	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}
	public void setQueueNamePre(String queueNamePre) {
		this.queueNamePre = queueNamePre;
	}

	@PostConstruct
	public void init() throws IOException, TimeoutException {
		initConnection();
		new MQListenerThread(getQueueName()).start();
	}
	
	/**
	 * 获取Queue名称
	 * @return
	 * @throws UnknownHostException
	 */
	private String getQueueName() throws UnknownHostException {
		InetAddress ia = InetAddress.getLocalHost();
		String hostName = ia.getHostName();
		String queueName = queueNamePre + "." + hostName;
		return queueName;
	}
	
	/**
	 * 监控线程
	 * @author jianming.zhou
	 *
	 */
	private class MQListenerThread extends Thread {
		
		private Channel channel;
		private QueueingConsumer consumer;
		
		private String queueName;
		
		public MQListenerThread(String queueName) {
			this.queueName = queueName;
		}

		@Override
		public void run() {
			initDeclare();
			
	        while(true){  
	        	try {
	        		Delivery delivery = consumer.nextDelivery();  
	        		MessageModel message = (MessageModel) SerializationUtils.deserialize(delivery.getBody());
	        		doPush(message);
	            	/** 确认消息已经收到 */  
	            	channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);  
	        	} catch (ShutdownSignalException e) {
	            	logger.error(e.getMessage(), e);
	            	shutdownSleep();
	            	recovery();
	            } catch (Exception e) {
	            	logger.error(e.getMessage(), e);
	            	errorSleep();
	            }
	        } 
		}
		
		/**
		 * 恢复
		 */
		private void recovery() {
			initDeclare();
		}
		
		/**
		 * 初始化Exchange及Queue
		 */
		private void initDeclare() {
			try {
				initConnection();
				channel = conn.createChannel();  
				consumer = new QueueingConsumer(channel); 
				
				channel.exchangeDeclare(exchangeName, "fanout", true);
				channel.queueDeclare(queueName, true, false, false, null);  
				channel.queueBind(queueName, exchangeName, "routingKey");
				
				channel.basicConsume(queueName, false, consumer);  
				channel.basicQos(1);
				
				logger.info("Declare[Exchange:{}, Queue:{}]", exchangeName, queueName);	
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
	}
	
	/**
	 * 初始化连接
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	public synchronized void initConnection() throws IOException, TimeoutException {
		if (conn == null || !conn.isOpen()) {
			conn = connectionFactory.newConnection();
		}
	}
	
	/**
	 * 广播/指定推送
	 * @param message
	 */
	private void doPush(MessageModel message) {
		logger.info("[doPush] users size: {}, Message: {}", message.getUsers() == null? "All" : message.getUsers().size(), message);
		if (null == message.getUsers() || message.getUsers().isEmpty()) { //广播
			for (Session session : WebSocketPool.getAllSessionSet()) {
				messageHandler.push("all", session, message.getMsg());
			}
		} else {
			for (String user : message.getUsers()) {
				for (Session session : WebSocketPool.getSessionByUser(user)) {
					messageHandler.push(user, session, message.getMsg());
				}
			}
		}
	}
	
	/**
	 * 默认10s等待
	 */
	private void shutdownSleep() {
		try {
			int millis = 1000*10;
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 默认1s等待
	 */
	private void errorSleep() {
		try {
			int millis = 1000;
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
