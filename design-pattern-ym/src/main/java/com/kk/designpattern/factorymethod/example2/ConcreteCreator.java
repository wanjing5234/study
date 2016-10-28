package com.kk.designpattern.factorymethod.example2;

public class ConcreteCreator extends Creator {

	@Override
	protected Product factoryMethod() {
		//重定义工厂方法，返回一个具体的Product对象
		return new ConcreteProduct();
	}

	
}
