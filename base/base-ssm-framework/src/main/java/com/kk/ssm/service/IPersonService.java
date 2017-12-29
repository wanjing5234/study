package com.kk.ssm.service;

import javax.ws.rs.core.MediaType;

import com.kk.ssm.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;  
import org.springframework.web.bind.annotation.RequestBody;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;  
import org.springframework.web.bind.annotation.ResponseBody;  
  
/**
 * spring MVC风格的restful接口 
 *  
 * @author gaoweignag 
 * @since JDK1.7 
 */  
@RequestMapping("/test")  
@Controller  
public interface IPersonService {  
  
  
    @RequestMapping(value = "/hello", produces = "text/plain;charset=UTF-8")  
    public @ResponseBody  
    String hello();  
  
      
    @RequestMapping(value = "/say/{msg}", produces = "application/json;charset=UTF-8")  
    public @ResponseBody  
    String say(@PathVariable(value = "msg") String msg);  
  
      
    @RequestMapping(value = "/person/{id:\\d+}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")  
    public @ResponseBody  
    String getPerson(@PathVariable("id") int id);  
      
      
    @RequestMapping(value = "/person/{id:\\d+}", method = RequestMethod.DELETE)  
    public @ResponseBody Object deletePerson(@PathVariable("id") int id) ;  
      
    /** 
     * 推荐使用，这种可以解决绝大多数问题 
     * @param person 
     * @return 
     */  
    @RequestMapping(value = "/person", method = RequestMethod.POST,   
            produces = {MediaType.APPLICATION_JSON,"application/json;charset=UTF-8"},  
            consumes = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})  
    public String addPerson(Person person);  
      
      
    @RequestMapping(value = "/person", method = RequestMethod.PUT)  
    public @ResponseBody Object updatePerson(@RequestBody Person person);
      
      
}  