package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest {

	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test
	@Ignore
	public void testBqueryProductCategory() {
		long shopId = 1L;
		List<ProductCategory> ProductCategorylist = productCategoryDao.queryProductCategory(shopId);
		System.out.println("店铺自定义的类别数是："+ProductCategorylist.size());
	}
	
	
	@Test
	public void testABatchInsertProductCategory() {
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryName("商品类别11");
		productCategory.setPriority(1);
		productCategory.setCreateTime(new Date());
		productCategory.setShopId(1L);
		
		ProductCategory productCategory2 = new ProductCategory();
		productCategory2.setProductCategoryName("商品类别23");
		productCategory2.setPriority(1);
		productCategory2.setCreateTime(new Date());
		productCategory2.setShopId(1L);
		
		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		productCategoryList.add(productCategory);
		productCategoryList.add(productCategory2);
		int effertedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
		assertEquals(2, effertedNum);
	}
	
	@Test
	public void testCDeleteProductCategory()throws Exception{
		long shopId = 1;
		List<ProductCategory>productCategoryList = productCategoryDao.queryProductCategory(shopId);
		for (ProductCategory pc : productCategoryList) {
			if("商品类别11".equals(pc.getProductCategoryName())||"商品类别23".equals(pc.getProductCategoryName())) {
				int effectedNum = productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), shopId);
				assertEquals(1, effectedNum);
			}
		}
	}
}
