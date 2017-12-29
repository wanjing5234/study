package com.kk.ssm.service.impl;

import com.kk.ssm.model.Person;
import com.kk.ssm.service.IPersonService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
  
  
/** 
 * spring MVC风格的restful接口 
 *  
 * @author gaoweignag 
 * @since JDK1.7 
 */  
@Controller  
public class PersonService implements IPersonService {
  
    /** 日志实例 */
    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);
  
    public @ResponseBody  
    String hello() {  
        logger.info("hello........");  
        return "你好！hello";  
    }  
  
    public @ResponseBody  
    String say(@PathVariable(value = "msg") String msg) {  
        return "{\"msg\":\"you say:'" + msg + "'\"}";  
    }  
  
    public @ResponseBody  
    String getPerson(@PathVariable("id") int id) {  
        logger.info("获取人员信息id=" + id);  
        Person person = new Person();
        person.setName("张三");  
        person.setSex("男");  
        person.setAge(30);  
        person.setId(id);  
          
        JSONObject jsonObject = JSONObject.fromObject(person);  
        logger.info("{}", jsonObject);
        logger.info(jsonObject.toString());  
        return jsonObject.toString();  
    }  
      
    public Object deletePerson(@PathVariable("id") int id) {  
        logger.info("删除人员信息id=" + id);  
        JSONObject jsonObject = new JSONObject();  
        jsonObject.put("msg", "删除人员信息成功");
        return jsonObject;
    }  
  
  
    public @ResponseBody String addPerson(@RequestBody Person person) {  
        logger.info("注册人员信息成功id=" + person.getId());  
        JSONObject jsonObject = new JSONObject();  
        jsonObject.put("msg", "注册人员信息成功");  
        return jsonObject.toString();  
    }  
      
    public @ResponseBody Object updatePerson(@RequestBody Person person) {  
        logger.info("更新人员信息id=" + person.getId());  
        JSONObject jsonObject = new JSONObject();  
        jsonObject.put("msg", "更新人员信息成功");  
        return jsonObject.toString();  
    }  
  
}  