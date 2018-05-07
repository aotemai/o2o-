package com.imooc.o2o.util;

/**
 * 测试Thumbnails对图片添加水印和压缩
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	
	//设置时间格式
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	//随机数对象
	private static final Random r = new Random();
	
	//日志信息
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	/**
	 * 将CommonsMultipartFile类装换成File类
	 * @param cFile
	 * @return
	 */
	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
		File newFile = new File(cFile.getOriginalFilename());
		try {
			cFile.transferTo(newFile);
		} catch (IllegalStateException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return newFile;
	}
	
	
	/**
	 * 处理缩略图，并返回新生成图片的相对值路径
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	
	//处理用户传递过来的文件对象路径,thumbnail为读取的文件名，targetAddr为目标文件路径
	public static String generateThumbnail(InputStream thumbnailInputStream,String fileName,String targetAddr) {
		/*
		 * 随机文件名+扩展名=新文件名，新文件名存储在targetAddr路径下
		 */
		//获取随机文件名
		String realFileName = getRandomFileName();
		//获取文件的扩展名
		String extension = getFileExtension(fileName);
		//创建目录
		makeDirPath(targetAddr);
		//获取相对路径
		String relativeAddr = targetAddr + realFileName + extension;//目录+扩展名+随机文件名
		
		logger.debug("current relativeAddr is"+relativeAddr);
		
		//最后文件的路径=根路径+相对路径
		File dest = new File(PathUtil.getImgBasePath()+relativeAddr);
		
		logger.debug("current complete addr is:"+PathUtil.getImgBasePath()+relativeAddr);
		
		try {
		//创建缩略图
		Thumbnails.of(thumbnailInputStream).size(200, 200)
		.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.gif")),0.25f)
		.outputQuality(0.8f).toFile(dest);//toFile保存在哪个文件夹里面
		}catch(IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return relativeAddr;//返回图片的路径。返回相对地址
	}
	/*
	 * 创建目标路径所涉及的目录，比如说/home/aaa/bbb,那么home aaa bbb这三个文件名都会创建出来
	 */
	private static void makeDirPath(String targetAddr) {
		// 获取绝对路径的全路径
		String realFileParentPath = PathUtil.getImgBasePath()+targetAddr;
		File dirPath = new File(realFileParentPath);
		if(!dirPath.exists()) {
			dirPath.mkdirs();
		}
		
	}

	/*
	 * 获取输入文件流的扩展名
	 */
	private static String getFileExtension(String fileName) {
		// 获取原来的文件名的后缀名
		
		return fileName.substring(fileName.lastIndexOf("."));
	}
	
	/*
	 * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
	 */
	public static String getRandomFileName() {
		// 获取随机的五位数
		int rannum = r.nextInt(89999)+10000;//也就是取值大于10000小于99999
		String nowTimeStr = sDateFormat.format(new Date());
		return nowTimeStr+rannum;//字符串+整形会自动转换成子浮串
	}

	public static void main(String[] args) throws IOException {
		
		String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		//读取文件，设置像素，添加水印（1水印位置，2水印路径，3透明度），输出（压缩比和路径）
		Thumbnails.of(new File("C:/Users/huajun/Desktop/hello.JPG")).size(200, 200)//像素200*200
		.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.gif")),0.25f)//透明度0.25
		.outputQuality(0.8f).toFile("C:/Users/huajun/Desktop/helloword.JPG");//压缩0.8
	}
	
	
	
	/**
	 * storePath是文件的路径还是目录的路径
	 * 如果storePath是文件路径则删除该文件
	 * 如果storePath是目录的路径则删除该目录下的文件
	 */
	public static void deleteFileOrPath(String storePath) {
		//创建一个文件对象
		File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
		if(fileOrPath.exists()) {
			if(fileOrPath.isDirectory()) {
				File files[] = fileOrPath.listFiles();
				for(int i=0;i<files.length;i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}
}



