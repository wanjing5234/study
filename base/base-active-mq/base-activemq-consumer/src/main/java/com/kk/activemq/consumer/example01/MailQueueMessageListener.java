package com.kk.activemq.consumer.example01;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;


@Component
public class MailQueueMessageListener implements SessionAwareMessageListener<Message> {

	private static final Logger logger = LoggerFactory.getLogger(MailQueueMessageListener.class);

	@Autowired
    private JmsTemplate jmsTemplate;

	@Autowired
	private Destination mailQueue;


	public synchronized void onMessage(Message message, Session session) {
		try {
			TextMessage msg = (TextMessage) message;
			final String ms = msg.getText();
			System.out.println("收到消息：" + ms);
			//转换成相应的对象
			Mail mail = JSONObject.parseObject(ms, Mail.class);
			if (mail == null) {
				return;
			}
			try {
				//执行发送业务
				logger.info("---------> mail:{}", mail);
				
			} catch (Exception e) {
				// 发送异常，重新放回队列
//				jmsTemplate.send(mailQueue, new MessageCreator() {
//					@Override
//					public Message createMessage(Session session) throws JMSException {
//						return session.createTextMessage(ms);
//					}
//				});
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}