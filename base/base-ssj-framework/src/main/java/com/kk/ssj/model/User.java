package com.kk.ssj.model;

import java.util.Date;

public class User implements Cloneable {

    private int id;
    private String username;
    private String personalName;
    private String password;
    private String phone;
    private Date addTime;
    private Integer duty;
    private Integer deleted;
    
    public User() {
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPersonalName() {
		return personalName;
	}

	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getDuty() {
		return duty;
	}

	public void setDuty(Integer duty) {
		this.duty = duty;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", username='" + username + '\'' +
				", personalName='" + personalName + '\'' +
				", password='" + password + '\'' +
				", phone='" + phone + '\'' +
				", addTime=" + addTime +
				", duty=" + duty +
				", deleted=" + deleted +
				'}';
	}
}
