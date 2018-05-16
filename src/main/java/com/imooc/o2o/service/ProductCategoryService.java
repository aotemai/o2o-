package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.ecxeptions.ProductCategoryOperationException;
import com.imooc.o2o.entity.ProductCategory;

public interface ProductCategoryService {

	/*
	 * 查询指定某个店铺下的所有商品类别信息
	 */
	List<ProductCategory> getProductCategoryList(Long shopId);
	
	/*
	 *增加商品分类
	 */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
	 throws ProductCategoryOperationException;
	
	
	/*
	 * 将此类别下的商品里的类别id置为空，再删除该商品类别
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId) 
			throws ProductCategoryOperationException;
}
