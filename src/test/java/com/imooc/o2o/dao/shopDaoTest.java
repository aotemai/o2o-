package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;

public class shopDaoTest extends BaseTest{

	@Autowired
	private ShopDao shopDao;
	
	@Test
	public void testUpdateShop() {
		
		Shop shop = new Shop();
		shop.setShopId(1L);	
		shop.setAdvice("测试更新后的建议");	
		shop.setLastEditTime(new Date());
		shop.setPhone("测试更新后的电话");
		shop.setShopAddr("测试更新后的地址");	
		
		int effectedNum = shopDao.updateShop(shop);
		assertEquals(1, effectedNum);	
	}
	
	
	@Test
	public void testInsertShop() {
		
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		ShopCategory shopCategory = new ShopCategory();
		Area area = new Area();
		//给对象赋值
		owner.setUserId(1L);
		area.setAreaId(2);
		shopCategory.setShopCategoryId(1L);
		
		shop.setAdvice("测试的建议");
		shop.setArea(area);
		shop.setCreateTime(new Date());
		shop.setEnableStatus(1);
		shop.setOwner(owner);
		shop.setPhone("测试的电话");
		shop.setPriority(1);
		shop.setShopAddr("测试的地址");
		shop.setShopCategory(shopCategory);
		shop.setShopDesc("测试的描述");
		shop.setShopImg("测试的图片");
		shop.setShopName("测试的名字");
		
		int effectedNum = shopDao.insertShop(shop);
		assertEquals(1, effectedNum);	
	}
	
}
