package com.imooc.o2o.service.Impl;


import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.ecxeptions.ShopOperationException;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;


@Service
public class ShopServiceImpl implements ShopService{
	
	@Autowired
	private ShopDao shopDao;
	
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName)throws ShopOperationException {
		// 空值判断
		if(shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		
		try {
			//往数据库里面增添信息，先给店铺信息赋初始值
			shop.setEnableStatus(0);//审核中
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			//添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if(effectedNum<=0) {
			throw new ShopOperationException("店铺创建失败");
			}else {
				if(shopImgInputStream!=null) {
					//存储图片
					try {
						addShopImg(shop,shopImgInputStream,fileName);
					
					
					}catch(Exception e) {
					throw new ShopOperationException("addShopImg error:"+e.getMessage());
					}
					//跟新店铺的图片地址
					effectedNum = shopDao.updateShop(shop);
					if(effectedNum<=0) {
						throw new ShopOperationException("更新图片地址失败");
					}
				}
			}
			
			
		}catch(Exception e) {
			throw new ShopOperationException("addShop error:"+e.getMessage());
		}
		
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}

	private void addShopImg(Shop shop, InputStream shopImgInputStream,String fileName) {
		// 获取shop图片目录的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream, fileName,dest);
		shop.setShopImg(shopImgAddr);
		
	}

	/*
	 * 根据店铺id获取店铺信息
	 */
	@Override
	public Shop getByShop(long shopId) {
		// TODO Auto-generated method stub
		return shopDao.queryByShopId(shopId);
	}

	/*
	 * 更新店铺信息，包括对图片的处理
	 */
	@Override
	public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName)
			throws ShopOperationException {
			//先判断是否有这商铺
		if(shop==null||shop.getShopId()==null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}else {
			try {
				//1、判断是否需要处理图片（跟新图片的话需要将就的图片删除掉）
				if(shopImgInputStream!=null&&fileName!=null&&!"".equals(fileName)) {
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if(tempShop.getShopImg()!=null) {
						ImageUtil.deleteFileOrPath(tempShop.getShopAddr());
					}
					addShopImg(shop, shopImgInputStream, fileName);
				}
				//2、更新店铺信息
				shop.setLastEditTime(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if(effectedNum<=0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
					
				}else {
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS);
				}
			}
			catch(Exception e) {
				throw new ShopOperationException("modifyShop error"+e.getMessage());
			}
		
		}
	}

	//这里传的是pageIndex是因为前端只认页数，而dao层只认行数，所以需要编写个工具类进行转换一下
	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		//将pageIndex转换成rowIndex
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if(shopList != null) {
			se.setShopList(shopList);
			se.setCount(count);
		}else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

}
