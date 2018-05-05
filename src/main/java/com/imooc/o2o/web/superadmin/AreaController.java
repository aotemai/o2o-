package com.imooc.o2o.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.entity.Area;
import com.imooc.o2o.service.AreaService;

@Controller
@RequestMapping("/superadmin")
public class AreaController {
	
	Logger logger = LoggerFactory.getLogger(AreaController.class);
	
	@Autowired
	private AreaService areaService;
	
	@RequestMapping(value="/listarea",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> listArea(){
			//一般用info记录日志启动的时间
		logger.info("===start===");
		long startTimen=System.currentTimeMillis();
		
		//初始化Map
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//初始化List，将Area实体类的值存入到list列表
		List<Area> list = new ArrayList<Area>();
		try {
		//将service查询到的数据存放在list列表里
		list = areaService.getAreaList();
		//取出
		modelMap.put("rows", list);
		modelMap.put("total", list.size());
		}catch(Exception e) {
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		
	logger.error("test error!");	
	
	long endTime = System.currentTimeMillis();
		//debug调优，看花费多长时间
	logger.debug("costTime:[{}ms]",endTime-startTimen);
	logger.info("====end===");
	
	
		return modelMap;
	}
}
