package com.imooc.o2o.ecxeptions;

public class ProductCategoryOperationException extends RuntimeException{

	private static final long serialVersionUID = 3787686066662957333L;

	public ProductCategoryOperationException(String msg) {
		super(msg);
	}
}
