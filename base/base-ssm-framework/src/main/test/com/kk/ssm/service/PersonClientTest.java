package com.kk.ssm.service;

import com.kk.ssm.model.Person;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class PersonClientTest {
      
    private static final Logger logger = LoggerFactory.getLogger(PersonClientTest.class);
      
    private static RestTemplate template = null;
      
    @BeforeClass
    public static void beforeClass(){  
        template = new RestTemplate();   
          
    }  
      
    @Test
    public void testHello(){
        logger.info("进入hello Method start...");
        String url = "http://localhost:82/test/hello";
        /** 
         * 第一个参数是restful接口请求路径 
         * 第二个参数是响应的类型 String.class 
         */  
        String result = template.getForObject(url, String.class);
        logger.info("输出结果："+result);
        logger.info("进入hello Method end...");
    }  
      
    @Test  
    public void testSay(){
        logger.info("进入say Method start...");
        String url = "http://localhost:82/springMVCRestful/test/say/gaoweigang";  
        String result = template.getForObject(url, String.class);
        logger.info("输出结果："+result);
        logger.info("进入say Method end...");
          
    }  
      
    @Test  
    public void testGetPerson(){
        logger.info("进入getPerson Method start...");
        /** 
         * restful参数类型是int，不能传String类型的参数，否则调用不到接口 
         */  
        String url = "http://localhost:82/springMVCRestful/test/person/101";  
        String result = template.getForObject(url, String.class);
        logger.info("输出结果："+result);
        logger.info("进入getPerson Method end...");
          
    }  
      
      
    @Test  
    public void testDeletePerson(){
        logger.info("进入deletePerson Method start...");
        /** 
         * restful参数类型是int，不能传String类型的参数，否则调用不到接口 
         */  
        String url = "http://localhost:82/springMVCRestful/test/person/1234";  
          
        try {  
            template.delete(url);  
        } catch (RestClientException e) {
            e.printStackTrace();  
        }
        logger.info("进入deletePerson Method end...");
    }
      
      
    @Test  
    public void testUpdatePerson(){
        logger.info("进入UpdatePerson Method start...");
        /** 
         * restful参数类型是int，不能传String类型的参数，否则调用不到接口 
         */  
        String url = "http://localhost:82/springMVCRestful/test/person";  
        try {
            Person person =new Person();  
            person.setId(1234);  
            person.setName("gaoweigang");  
            person.setAge(22);  
            person.setSex("男");  
              
            template.put(url, person);  
        } catch (RestClientException e) {  
            e.printStackTrace();  
        }
        logger.info("进入UpdatePerson Method end...");
    }
      
    @Test  
    public void testAddPerson(){
        logger.info("进入addPerson Method start...");
        /** 
         * restful参数类型是int，不能传String类型的参数，否则调用不到接口 
         */  
        String url = "http://localhost:82/springMVCRestful/test/person";  
        Person person =new Person();
        person.setId(1234);  
        person.setName("gaoweigang");  
        person.setAge(22);  
        person.setSex("男");  
          
        String result = template.postForObject(url, person, String.class);
        logger.info("输出结果为："+result);

        logger.info("进入addPerson Method end...");
    }
}