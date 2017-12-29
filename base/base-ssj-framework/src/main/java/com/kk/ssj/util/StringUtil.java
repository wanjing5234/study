package com.kk.ssj.util;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class StringUtil {

	public static boolean isNullOrEmpty(String s) {
		if (s == null || s.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);
	}
	
	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}
	
    /** 
    * 字符串的右对齐输出 
    * @param c 填充字符 
    * @param l 填充后字符串的总长度 
    * @param string 要格式化的字符串 
    */  
    public static String flushRight(char c, long l, String string) {  
            String str = "";   
            String temp = "";     
            if (string.length() > l)     
                str = string;     
            else    
                for (int i = 0; i < l - string.length(); i++)     
                    temp = temp + c;     
            str = temp+string;     
            return str;    
    }  
    
    /** 
    * 字符串的左对齐输出 
    * @param c 填充字符 
    * @param l 填充后字符串的总长度 
    * @param string 要格式化的字符串 
    */  
    public static String flushLeft(char c, long l, String string) {  
            String str = "";   
            String temp = "";     
            if (string.length() > l)     
                str = string;     
            else    
                for (int i = 0; i < l - string.length(); i++)     
                    temp = temp + c;     
            str = string+temp;     
            return str;    
    }   
    
      // 获取ip地址
 	  public static String getIpAddress() {
 	    try {
 	      Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
 	      InetAddress ip = null;
 	      while (allNetInterfaces.hasMoreElements()) {
 	        NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
 	        if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
 	          continue;
 	        } else {
 	          Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
 	          while (addresses.hasMoreElements()) {
 	            ip = addresses.nextElement();
 	            if (ip != null && ip instanceof Inet4Address) {
 	              return "http://" + ip.getHostAddress();
 	            }
 	          }
 	        }
 	      }
 	    } catch (Exception e) {
 	      e.printStackTrace();
 	    }
 	    return "";
 	  }
 	  
 	  ArrayList<File> list = new ArrayList<File>();
 	  public List<File> listFolderFile(String localPath){
 		File file = new File(localPath);
 		if (file.exists()) {
 			File[] files = file.listFiles();
 			for (File file2 : files) {
 				if (!file2.isDirectory()) {
 					list.add(file2);
 				}else {
 					listFolderFile(file2.getAbsolutePath());
 				}
 			}
 		}
		return list;
 	  }
 	  
	  ArrayList<File> dirList = new ArrayList<File>();
 	  public List<File> listFolder(String localPath){
 		File file = new File(localPath);
 		if (file.exists()) {
 			File[] files = file.listFiles();
 			for (File file2 : files) {
 				if (file2.isDirectory()) {
 					dirList.add(file2);
 					listFolder(file2.getAbsolutePath());
 				}
 			}
 		}
		return dirList;
 	  }
 	  
 	 public static String removeCharacters(String str, String beginStr, String endStr) {
     	String result = str;
     	if (str.startsWith(beginStr) && str.endsWith(endStr)) {
     		result = str.substring(1, str.length() - 1);
         }
         return result;
     }
 	 
 	 public static int countChar(String str, String substr) {
 		if (isNullOrEmpty(str) || isNullOrEmpty(substr)) {
			return 0;
		}
 		int count = 0;  
        int index = 0;  
        while((index=str.indexOf(substr))!=-1)  
        {  
            str = str.substring(index + substr.length());  
            count++;      
        }  
        return count;
 	 }
 	 
}
