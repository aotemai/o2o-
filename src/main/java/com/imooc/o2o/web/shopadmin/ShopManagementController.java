package com.imooc.o2o.web.shopadmin;



import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.ecxeptions.ShopOperationException;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServiceRequestUtil;


/**
 * 店铺管理
 * @author huajun
 *
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private AreaService areaService;
	
	@RequestMapping(value="/getshopinitinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object>getShopInitInfo(){
		/*
		 * 获取店铺列表和区域列表，赋值到modelMap里面去，再返回到前台
		 */
		Map<String,Object>modelMap = new HashMap<String,Object>();
		List<ShopCategory>shopCategoryList = new ArrayList<ShopCategory>();
		List<Area>areaList = new ArrayList<Area>();
		
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());//获取Category的全部列表
			areaList = 	areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	/*
	 * 店铺的注册
	 */
	@RequestMapping(value="/registershop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object>registerShop(HttpServletRequest request){
		Map<String,Object>modelMap = new HashMap<String,Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//1.接收并转换相应的信息，包括店铺的信息以及图片的信息
			//将前端转换来的信息转换成shopStr实体类
		String shopstr = HttpServiceRequestUtil.getString(request, "shopstr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopstr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//CommonsMultipartFile实现文件上传
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver CommonsMultipartResolver=new CommonsMultipartResolver(
				request.getSession().getServletContext());//request本次会话上下文获取上传文件内容
		//判断是否有上传文件流
		if(CommonsMultipartResolver.isMultipart(request)) {
			//如果有那么将它转换成multipartHttpServletRequest对象
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
			shopImg=(CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}else {
			//如果不具备文件流，那么就报错，因为图片肯定是要有流的
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
		}
		//2.注册店铺
		if(shop!=null&&shopImg!=null) {
			PersonInfo owner = new PersonInfo();
			//session todo
			owner.setUserId(1L);
			shop.setOwner(owner);
		
			ShopExecution se;
			try {
				se = shopService.addShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
				
				if(se.getState()==ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());;
			}				
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
		//3.返回的结果
		
	}
	
	/*
	 *将流转换成文件类型
	 */
/*	private static void inputStreamToFile(InputStream ins,File file) {
		FileOutputStream os = null;
		try {
			os=new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while((bytesRead=ins.read(buffer))!=-1) {
				os.write(buffer,0,bytesRead);
			}
		}catch(Exception e) {
			throw new RuntimeException("调用inputStreamToFile产生异常:"+e.getMessage());
		}finally {
			try {
				if(os!=null) {
					os.close();
				}
				if(ins!=null) {
					ins.close();
				}
			}catch(IOException e) {
				throw new RuntimeException("调用inputStreamToFile关闭io产生异常:"+e.getMessage());
			}
		}
	}*/
	
	
	
	
	
	
	
	
	
	
}


