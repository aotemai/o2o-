package com.imooc.o2o.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.ecxeptions.ProductOperationException;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductImgDao productImgDao;
	
	@Override
	@Transactional
	/**
	 * 1、处理缩略图，获取缩略图相对路径并赋值给product
	 * 2、往tb_product写入商品信息，获取productId
	 * 3、结合productId批量处理商品详情图
	 * 4、将商品详情图列表批量插入tb_product_img中
	 */
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException {
		// 空值判断
		if(product != null && product.getShop().getShopId() != null) {
			//给商品设置上默认属性
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			//默认为上架的状态
			product.setEnableStatus(1);
			//若商品缩略图不为空则添加
			if(thumbnail != null) {
				addThumbnail(product, thumbnail);
			}else {
				try {
					//创建商品信息
					int effectedNum = productDao.insertProduct(product);
					if(effectedNum <= 0) {
						throw new ProductOperationException("创建商品失败");
					}
				}catch(Exception e) {
					throw new ProductOperationException("创建商品失败："+e.toString());
				}
			}
			//若商品详情图不为空则添加
			if(productImgList != null && productImgList.size()>0) {
				addProductImgList(product, productImgList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS,product);
		}else {
			//传参为空则返回空值错误信息
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
		
	}

	//添加缩略图
	private void addThumbnail(Product product,ImageHolder thumbnail) {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}

	//批量添加商品详情图
	private void addProductImgList(Product product,List<ImageHolder> productImgList) {
		//获取图片存储路径，直接放在相应相铺的文件夹底下
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgLs = new ArrayList<ProductImg>();
		for (ImageHolder productImgHolder : productImgList) {
			String imgAddr = ImageUtil.generateThumbnail(productImgHolder, dest);
			ProductImg productImg = new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgLs.add(productImg);
		}
		//如果确实是有图片需要添加的，就执行批量添加操作
		if(productImgLs.size()>0) {
			try {
				int effectedNum = productImgDao.batchInsertProductImg(productImgLs);
				if(effectedNum<=0) {
					throw new ProductOperationException("创建商品详情图片失败");
				}			
			}catch(Exception e) {
				throw new ProductOperationException("创建商品详情图片失败："+e.getMessage());
			}
		}
	}

	//根据productId查询商品详情
	@Override
	public Product getProductById(long productId) {
		return productDao.queryProductById(productId);
	}

	@Override
	@Transactional
	//1.如果缩略图参数有值，则处理缩略图，
		//如果原先有缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给product
	//2.如果商品详情图列表参数有值，对商品详情图片列表进行同样的操作
	//3.将tb_product_img下面的该商品原先的商品详情图记录全删除
	//4.更新tb_product_img以及tb_product的信息
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgHolderList) throws ProductOperationException {
		if(product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			//给商品设置默认值
			product.setLastEditTime(new Date());
			//如果商品缩略图不为空且原有缩略图不为空则删除原有的缩略图并添加
			if(thumbnail != null) {
				//需要先获取一遍原来的，因为原来的信息有图片地址
			Product tempproduct = productDao.queryProductById(product.getProductId());
			if(tempproduct.getImgAddr() != null) {
				ImageUtil.deleteFileOrPath(tempproduct.getImgAddr());
				}
			addThumbnail(product, thumbnail);
			}
			//如果有新存入的商品详情图，则将原来的先删除，并添加新的图片
			if(productImgHolderList != null&&productImgHolderList.size()>0 ) {
				deleteProductImgList(product.getProductId());
				addProductImgList(product, productImgHolderList);
			}
			try {
				//更新商品信息
				int effectedNum = productDao.updateProduct(product);
				if(effectedNum<=0) {
					throw new ProductOperationException("更新图片失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS,product);
			}catch(Exception e) {
				throw new ProductOperationException("更新图片失败："+ e.toString());
			}
		}else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	
	}
	
	
	/*
	 * 删除某个商品下的详情图
	 */
	private void deleteProductImgList(Long productId) {
		//根据productId获取原来的图片
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		//删除原来图片的路径
		for (ProductImg productImg : productImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		//删除数据库里原有的图片信息
		productImgDao.deleteProductImgByProductId(productId);
	}

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		// 页码转换成数据库的行码，并调用dao层取回指定页码的商品列表
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
		// 基于同样的查询条件返回该查询条件下的商品总数
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe = new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}
	
	
}
