package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
	
	public static boolean checkVerifyCode(HttpServletRequest request) {
		//获取验证码信息
		String VerifyCodeExpected = (String) request.getSession().getAttribute(
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		//获取实际验证码
		  //需要注意的是要对文件流进行处理，否则不能识别文件流
		String verifyCodeActual = HttpServiceRequestUtil.getString(request, "verifyCodeActual");
		
		if(verifyCodeActual == null || !verifyCodeActual.equals(VerifyCodeExpected)) {
			return false;
		}
		return true;
	}
}
