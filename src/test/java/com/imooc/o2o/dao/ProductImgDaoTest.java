package com.imooc.o2o.dao;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductImg;

public class ProductImgDaoTest extends BaseTest {
	
	@Autowired
	private ProductImgDao productImgDao;

	@Test
	public void testBatchInsertProductImg()throws Exception{
		ProductImg productImg = new ProductImg();
		productImg.setImgAddr("图片地址");
		productImg.setImgDesc("图片的详情");
		productImg.setPriority(1);
		productImg.setCreateTime(new Date());
		productImg.setProductId(1L);
			
		ProductImg productImg2 = new ProductImg();
		productImg2.setImgAddr("图片地址2");
		productImg2.setImgDesc("图片的详情2");
		productImg2.setPriority(1);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(1L);
		
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImg);
		productImgList.add(productImg2);
		int effectedNum = productImgDao.batchInsertProductImg(productImgList);
		assertEquals(2, effectedNum);
	}
	
	@Test
	public void testDeleteProductImgByProductId() throws Exception{
		long productId = 1;
		int effectedNum = productImgDao.deleteProductImgByProductId(productId);
		assertEquals(4, effectedNum);
	}
}
