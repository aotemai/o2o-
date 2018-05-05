package com.imooc.o2o.service;



import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;

public class ShopServiceTest extends BaseTest{
	
	@Autowired
	private ShopService shopService;
	
	@Test
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
		ShopExecution se = shopService.addShop(shop,is,shopImg.getName());
		
		assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
	}
}
