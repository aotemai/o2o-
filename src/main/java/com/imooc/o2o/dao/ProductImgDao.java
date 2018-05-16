package com.imooc.o2o.dao;

import java.util.List;

import com.imooc.o2o.entity.ProductImg;

public interface ProductImgDao {
	
	/*
	 * 批量插入商品详情图
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);
	
	//根据商品的id删除商品的图片,生效行数是int
	int deleteProductImgByProductId(long productId);
	
	//列出某个商品的详情图列表
	List<ProductImg> queryProductImgList(long productId);
}
