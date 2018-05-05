package com.imooc.o2o.dao;

import java.util.List;

import com.imooc.o2o.entity.Area;

public interface AreaDao {
	/**
	 * 列出区域列表
	 * @return areaList
	 */
	//查询区域
	List<Area> queryArea();
}
