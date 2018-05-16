package com.imooc.o2o.web.shopadmin;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
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
import com.imooc.o2o.util.HttpServletRequestUtil;


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
	
	/*
	 *获取商铺管理页
	 * 根据用户传进来的shopId进行判断是否何进入商铺管理页
	 * 用map集合接收参数
	 */
	@RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId <= 0) {
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if (currentShopObj == null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			} else {
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		} else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}

	@RequestMapping(value="/getshoplist",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getshoplist(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();

	PersonInfo	user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	/*
	 *根据店铺id获取店铺信息 
	 */
	@RequestMapping(value="/getshopbyid",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopById(HttpServletRequest request){
		//定义个map集合接收一下数据
		Map<String,Object> modelMap =new HashMap<String,Object>();
		//根据shopId查找数据
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId > -1) {//表示接收到前端的数据
			try {
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			}catch(Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}	
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
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
		String shopstr = HttpServletRequestUtil.getString(request, "shopstr");
		//利用ObjectMapper的readValue放啊发将json值转换成对象
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
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");//从session里面取出user对象，user对象在登录中实现
			//session todo
			
			shop.setOwner(owner);
		
			ShopExecution se;
			try {
				ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
				se = shopService.addShop(shop,imageHolder);
				
				if(se.getState()==ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					//一旦创建成功就将数据保存在session里面
						//改用户可以操作的店铺列表
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if(shopList==null||shopList.size()==0) {//如果店铺列表是空的也就是说创建第一个店铺
						shopList = new ArrayList<Shop>();//创建一个店铺对象
						shopList.add(se.getShop());
						request.getSession().setAttribute("shopList", shopList);
					}else {//如果不是第一个店铺
						shopList.add(se.getShop());
						request.getSession().setAttribute("shopList", shopList);
					}
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
		
	}
	
	@RequestMapping(value="/modifyshop",method=RequestMethod.POST)
	@ResponseBody
	//修改店铺信息
	private Map<String,Object> modifyshop(HttpServletRequest request){
		//判断验证码
		Map<String,Object> modelMap = new HashMap<String,Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误！");
			return modelMap;
		}
		//将传进来的值转换成字符串
		String shopstr = HttpServletRequestUtil.getString(request, "shopstr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopstr, Shop.class);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//request本次会话上下文获取上传文件内容
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		CommonsMultipartFile shopImg =null;
		//判断是否有上传文件流
		if(commonsMultipartResolver.isMultipart(request)) {
			//如果有那么将它转换成multipartHttpServletRequest对象
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		//2.修改店铺的信息
		if(shop!=null && shop.getShopId()!=null) {//判断店铺和店铺id不为空
			ShopExecution se;
			try {
				if(shopImg==null) {//如果相片是空的就传入空的值
					se = shopService.modifyShop(shop, null);
				}else {
					ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
					se = shopService.modifyShop(shop, imageHolder);
				}
				if(se.getState()==ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			}catch(ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}catch(IOException e) {
				modelMap.put("success", false);
				modelMap.put("success", e.getMessage());
			}
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
	}
	
	
	
	/*
	 *将流转换成文件类型
	 */
	private static void inputStreamToFile(InputStream ins,File file) {
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
	}
	
	
	
	
	
	
	
	
	
	
}




