package com.imooc.o2o.util;

/**
 * 将pageInde转换成rowIndex
 * @author huajun
 *
 */
public class PageCalculator {
	
	public static int calculateRowIndex(int pageIndex,int pageSize) {
		//理解：如果pageIndex是等于2的话，pageSize等于5的话，就是（2-1）*5就是从第五条数据开始读取
		return(pageIndex > 0)?(pageIndex-1)*pageSize : 0;
	}
}
