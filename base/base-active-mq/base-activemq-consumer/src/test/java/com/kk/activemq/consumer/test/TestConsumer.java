
package com.kk.activemq.consumer.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestConsumer {

	public static void main(String[] args) {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "spring.xml" });
			context.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
