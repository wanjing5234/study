package com.kk.activemq.provider.test;

import com.kk.activemq.provider.example01.MQProducer;
import com.kk.activemq.provider.example01.Mail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = {"classpath:spring.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestProducer {

    @Autowired
	private MQProducer mqProducer;
	
	@Test
	public void send(){
		Mail mail = new Mail();
		mail.setTo("174754613@qq.com");
		mail.setSubject("异步发送邮件");
		mail.setContent("Hi,This is a message!");
														
		this.mqProducer.sendMessage(mail);
		System.out.println("发送成功..");		
		
	}

}