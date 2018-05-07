package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.Shop;

public interface ShopDao {
	/**
	 * 店铺名（模糊）、店铺状态、店铺类别、区域Id、owner
	 * @param shopCondition 条件 
	 * @param rowIndex	从第几行开始读取数据
	 * @param pageSize	返回的条数
	 * @return
	 */
	List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,@Param("rowIndex")int rowIndex,
			@Param("pageSize")int pageSize);
	
	/**
	 * 返回queryShopList总数
	 * @param shopCondition
	 * @return
	 */
	int queryShopCount(@Param("shopCondition")Shop shopCondition);
	
	//通过shop id 查询店铺信息
	Shop queryByShopId(long shopId);
	
	//新增店铺
	int insertShop(Shop shop);
	
	//更新店铺信息
	int updateShop(Shop shop);
	
	
}
