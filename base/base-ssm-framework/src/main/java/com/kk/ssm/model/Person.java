package com.kk.ssm.model;

import java.io.Serializable;

public class Person implements Serializable {
  
    private static final long serialVersionUID = -3727979363425652597L;
  
    private int id;  
      
    private String name;  
      
    private String sex;  
      
    private int age;  
  
    public int getId() {  
        return id;  
    }  
  
    public void setId(int id) {  
        this.id = id;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public String getSex() {  
        return sex;  
    }  
  
    public void setSex(String sex) {  
        this.sex = sex;  
    }  
  
    public int getAge() {  
        return age;  
    }  
  
    public void setAge(int age) {  
        this.age = age;  
    }  
}  