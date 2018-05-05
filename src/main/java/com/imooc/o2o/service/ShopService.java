package com.imooc.o2o.service;

import java.io.InputStream;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.ecxeptions.ShopOperationException;
import com.imooc.o2o.entity.Shop;

public interface ShopService {

	ShopExecution addShop(Shop shop,InputStream shopImgInputStream,String fileName)throws ShopOperationException;

}
