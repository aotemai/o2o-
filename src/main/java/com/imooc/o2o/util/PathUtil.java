package com.imooc.o2o.util;

public class PathUtil {
	
	/*
	 * 根据不同的执行环境（不同的操作系统）去选择执行路径
	 */
	
	//windows是\，unix是/ ,,
	private static String seperator = System.getProperty("file.separator");
	public static String getImgBasePath() {	
		String os = System.getProperty("os.name");//获取操作系统的名称
		String basePath="";
		if(os.toLowerCase().startsWith("win")) {//判断是否window系统
			basePath = "E:/image/";
		}else {
			basePath="/home/image/";//其它系统
		}
		//seperator英文翻译是分隔器
			//转换成系统识别的斜杆
		basePath=basePath.replace("/", seperator);
		return basePath;	
	}
	
	/*
	 * 返回项目图片的子路径
	 */
	public static String getShopImagePath(long shopId) {
		String imagePath = "upload/item/shop/"+shopId+"/";
		return imagePath.replace("/", seperator);//转换成系统识别的斜杆
	}
}
