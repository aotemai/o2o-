/*package com.imooc.o2o.service;

import java.awt.image.ImagingOpException;
import java.io.InputStream;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.ecxeptions.ShopOperationException;
import com.imooc.o2o.entity.Shop;

public interface ShopService {

	
	 * 返回ShopExecution是因为它里面封装了count 和 shopList对象，方便整合在一起
	 * 根据shopCondition返回相应店铺列表
	 
	public ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
	
	
	 * 通过店铺id获取店铺信息
	 
	Shop getByShop(long shopId);
	
	
	 * 更新店铺信息，包括对图片的处理
	 
	ShopExecution modifyShop(Shop shop,ImageHolder thumbnail)throws ShopOperationException;
	
	ShopExecution getByShop(Shop shop,ImageHolder thumbnail)throws ShopOperationException;

	ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;
}
*/


package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.ecxeptions.ShopOperationException;
import com.imooc.o2o.entity.Shop;

public interface ShopService {
	/**
	 * 根据shopCondition分页返回相应店铺列表
	 * 
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

	/**
	 * 通过店铺Id获取店铺信息
	 * 
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);

	/**
	 * 更新店铺信息，包括对图片的处理
	 * 
	 * @param shop
	 * @param shopImg
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;

	/**
	 * 注册店铺信息，包括图片处理
	 * 
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;
}
