package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServiceRequestUtil {
	/*
	 * 将key转换成整型
	 */
	//将request提取出key，将key转换成整型
	public static int getInt(HttpServletRequest request,String key) {
		try {
			return Integer.decode(request.getParameter(key));//转换成整型是decode
		}catch(Exception e) {
			return -1;
		}		
	}
	
	/*
	 * 将key转换成长整型
	 */
	public static long getLong(HttpServletRequest request,String key) {
		try {
			return Long.valueOf(request.getParameter(key));//转换成长整型是valueOf
		}catch(Exception e) {
			return -1;
		}		
	}
	
	/*
	 * 将key转换成Double
	 */
	public static Double getDouble(HttpServletRequest request,String key) {
		try {
			return Double.valueOf(request.getParameter(key));//转换成Double是valueOf
		}catch(Exception e) {
			return -1d;
		}		
	}
	
	/*
	 * 将key转换成boolean
	 */
	public static boolean getboolean(HttpServletRequest request,String key) {
		try {
			return Boolean.valueOf(request.getParameter(key));//转换成boolean是valueOf
		}catch(Exception e) {
			return false;
		}		
	}
	
	/*
	 * 将key转换成string
	 */
	public static String getString(HttpServletRequest request,String key) {
		try {
			String result = request.getParameter(key);
			//如果不为空将两处的空格去掉
			if(result!=null) {
				result=result.trim();
			}
			if("".equals(result)) {
				result = null;
			}
			return result;
		}catch(Exception e) {
			return null;
		}		
	}
	
}
