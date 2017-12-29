package com.kk.ssj.exception;

import com.kk.ssj.util.I18nUtil;
import com.kk.ssj.util.StringUtil;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ApiException extends Exception {

	private static final long serialVersionUID = 1L;
	private int status;
    private String message;
    
    private String developerMessage;
    private String userMessage;

    private final String DEV_MSG_BUNDLE = "DeveloperMessages";
    private final String USER_MSG_BUNDLE = "UserMessages";

    private String parseMessage(String msg, String bundle) {
        if (null == msg) {
            return null;
        }
        if (msg.startsWith("{") && msg.endsWith("}")) {
            try {
                String msgTemplate = msg.substring(1, msg.length() - 1).trim();
                ResourceBundle resb = ResourceBundle.getBundle(bundle);
                msg = resb.getString(msgTemplate);
            } catch (MissingResourceException e) {
                return msg;
            }
        }
        return msg;
    }

    public ApiException(int status, String message) {
        this.status = status;
        this.message = I18nUtil.getIstance().getMsg(message, null);
    }
    
    public ApiException(int status, String developerMessage, String userMessage) {
        this.status = status;
        //this.developerMessage = userMessage;
        userMessage = StringUtil.removeCharacters(userMessage, "{", "}");
        this.userMessage = I18nUtil.getIstance().getMsg(userMessage, null);
    }

    public ApiException(int status, String developerMessage, String userMessage, Object[] args) {
        this.status = status;
        userMessage = StringUtil.removeCharacters(userMessage, "{", "}");
        this.userMessage = I18nUtil.getIstance().getMsg(userMessage, args);
    }
    
    public String ResponseException(String userMessage){
    	userMessage = StringUtil.removeCharacters(userMessage, "{", "}");
    	userMessage = I18nUtil.getIstance().getMsg(userMessage, null);
    	return userMessage;
    }

    public ApiException() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = parseMessage(developerMessage, DEV_MSG_BUNDLE);
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = parseMessage(userMessage, USER_MSG_BUNDLE);
    }

}

