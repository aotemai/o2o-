package com.imooc.o2o.ecxeptions;

public class ShopOperationException extends RuntimeException{

	/**
	 * 中断回滚事务的处理
	 *
	 */
	private static final long serialVersionUID = 8767618379348020850L;

	public ShopOperationException(String msg) {
		super(msg);
	}
}
