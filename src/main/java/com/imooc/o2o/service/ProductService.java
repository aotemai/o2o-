package com.imooc.o2o.service;

import java.io.InputStream;
import java.util.List;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.ecxeptions.ProductOperationException;
import com.imooc.o2o.entity.Product;

public interface ProductService {

	/*
	 * InputStream thumbnail,String thumbnailName是属于缩略图的
	 * 		可以将他们封装成一个类再作调用
	 * List<InputStream>productImgList,List<String>productImgNameList 是属于详情图的
	 * 
	ProductException addProduct(Product product,InputStream thumbnail,String thumbnailName,
			List<InputStream>productImgList,List<String>productImgNameList ) throws ProductOperationException;*/
	
	
	/**
	 * 查询商品列表并分页，可输入的条件有： 商品名（模糊），商品状态，店铺Id,商品类别
	 * 
	 * @param productCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

	
	//增加商品信息以及图片处理
	ProductExecution addProduct(Product product,ImageHolder thumbnail,
			List<ImageHolder> productImgList)throws ProductOperationException;
	
	//通过商品Id查询唯一的商品信息
	Product getProductById(long productId);
	
	/**
	 * 修改商品信息的方法
	 * @param product 商品
	 * @param thumbnail 缩略图
	 * @param productImgHolderList 商品详情图列表
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution modifyProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgHolderList)
			throws ProductOperationException;
	
}
