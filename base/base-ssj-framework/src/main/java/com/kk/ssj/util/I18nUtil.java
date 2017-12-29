package com.kk.ssj.util;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

public class I18nUtil {

	//定义一个静态私有变量(不初始化，不使用final关键字，使用volatile保证了多线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用)
    private static volatile I18nUtil instance;  
	
	private static MessageSource messages;
	
	private I18nUtil() {
	}
	
    //定义一个共有的静态方法，返回该类型实例
    public static I18nUtil getIstance() { 
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (I18nUtil.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    instance = new I18nUtil();   
                    messages = (MessageSource) ApplicationContextUtil.getBean("messageSource");
                }   
            }   
        }   
        return instance;   
    }   
    
    public String getMessage(String code, Integer language) {
    	code = StringUtil.removeCharacters(code, "{", "}");
    	String msg = code;
		Locale locale = getLocale(language);
		try {
			msg = messages.getMessage(code, null, locale);
		} catch (NoSuchMessageException e) {
			msg = code;
		}
		return msg;
    }

    public String getMsg(String code) {
    	code = StringUtil.removeCharacters(code, "{", "}");
    	return getMsg(code, null);
    }

	public String getMsg(String code, int language) {
		code = StringUtil.removeCharacters(code, "{", "}");
		return getMsg(code, language, null);
	}

	public String getMsg(String code, Object[] args) {
		int language = LanguageUtil.getLanguageValue();
		return getMsg(code, language, args);
	}

	public String getMsg(String code, int language, Object[] args) {
		Locale locale = getLocale(language);
		String msg = "";
		try {
			msg = messages.getMessage(code, args, locale);
		} catch (NoSuchMessageException e) {
			msg = code;
		}
		return msg;
	}

	private Locale getLocale(int language) {
		Locale locale = null;
		if (0 == language) {
			return Locale.US;
		} else if (1 == language) {
			return Locale.CHINA;
		} else if (2 == language) {
			return Locale.TAIWAN;
		}
		return Locale.CHINA;
	}
}
