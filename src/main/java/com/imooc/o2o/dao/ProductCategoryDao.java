package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.ProductCategory;

public interface ProductCategoryDao {

	List<ProductCategory> queryProductCategory(Long shopId);
	
	/*
	 * 批量新增商品类别
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);

	/*
	 * 删除指定商品类别
	 */
	int deleteProductCategory(@Param("productCategoryId")long productCategoryId,@Param("shopId")long shopId);
	
}
