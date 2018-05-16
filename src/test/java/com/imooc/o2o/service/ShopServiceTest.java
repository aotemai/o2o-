package com.imooc.o2o.service;



import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.ecxeptions.ShopOperationException;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;

public class ShopServiceTest extends BaseTest{
	
	@Autowired
	private ShopService shopService;
	
	@Test
	public void testGetShopList() {
		Shop shopCondition = new Shop();
		ShopCategory sc = new ShopCategory();
		sc.setShopCategoryId(2L);
		shopCondition.setShopCategory(sc);
		ShopExecution se = shopService.getShopList(shopCondition, 2, 2);//1页展示2条数据
		System.out.println("店铺列表数为：" + se.getShopList().size());
		System.out.println("店铺总数为："+se.getCount());
	}
	
	@Test
	@Ignore
	public void testModifyShop() throws ShopOperationException,FileNotFoundException{
		Shop shop = new Shop();
		shop.setShopId(1L);
		shop.setShopName("修改后的店铺路径");
		File shopImg = new File("C:/Users/huajun/Desktop/测试相片.png");
		InputStream is = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder("测试相片.png",is);//先传名字再传流
		ShopExecution shopExecution = shopService.modifyShop(shop,imageHolder);
		System.out.println(shopExecution.getShop().getShopImg());
	}
	
	@Test
	@Ignore
	public void testAddShop() throws FileNotFoundException {
		
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		ShopCategory shopCategory = new ShopCategory();
		Area area = new Area();
		//给对象赋值
		owner.setUserId(1L);
		area.setAreaId(2);
		shopCategory.setShopCategoryId(1L);	
		shop.setAdvice("审核中");
		shop.setArea(area);
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setOwner(owner);
		shop.setPhone("133333333");
		shop.setPriority(1);
		shop.setShopAddr("广州市");
		shop.setShopCategory(shopCategory);
		shop.setShopDesc("健身");
		shop.setShopName("测试哑铃3");
		
		File shopImg = new File("C:/Users/huajun/Desktop/cs10005.jpg");
		InputStream is = new FileInputStream(shopImg);	
		ImageHolder imageHolder = new ImageHolder(shopImg.getName(),is);//先传名字再传流
		ShopExecution se = shopService.addShop(shop,imageHolder);
		
		assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
	}
}
