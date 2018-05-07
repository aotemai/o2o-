package com.imooc.o2o.service;

import java.io.InputStream;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.ecxeptions.ShopOperationException;
import com.imooc.o2o.entity.Shop;

public interface ShopService {

	/*
	 * 返回ShopExecution是因为它里面封装了count 和 shopList对象，方便整合在一起
	 * 根据shopCondition返回相应店铺列表
	 */
	public ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
	
	/*
	 * 通过店铺id获取店铺信息
	 */
	Shop getByShop(long shopId);
	
	/*
	 * 更新店铺信息，包括对图片的处理
	 */
	ShopExecution modifyShop(Shop shop,InputStream shopImgInputStream,String fileName)throws ShopOperationException;
	
	ShopExecution addShop(Shop shop,InputStream shopImgInputStream,String fileName)throws ShopOperationException;

}
