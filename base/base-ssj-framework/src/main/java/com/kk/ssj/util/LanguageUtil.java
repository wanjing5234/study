package com.kk.ssj.util;

import com.kk.ssj.filter.AuthorizationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LanguageUtil {

	private LanguageUtil(){
	}

	public static int getLanguageValue() {
		HttpServletRequest httpServletRequest = AuthorizationFilter.threadLocalRequest.get();
		int language = 1;
		if (httpServletRequest != null) {
			HttpSession session = httpServletRequest.getSession();
			if (null != session.getAttribute("language")) {
				language = (int) session.getAttribute("language");
			}
		}
		return language;
	}
}
