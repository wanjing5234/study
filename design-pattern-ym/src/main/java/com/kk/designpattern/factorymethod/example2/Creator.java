package com.kk.designpattern.factorymethod.example2;

/**
 * 创建器，申明工厂方法
 *
 */
public abstract class Creator {

	/**
	 * 创建Product的工厂
	 */
	protected abstract Product factoryMethod();
	
	/**
	 * 示意方法，实现某些功能的方法
	 */
	public void someOperation(){
		//通常在这些方法实现中，需要调用工厂 方法来获取Product对象
		
		Product product = factoryMethod();
	}
	
	
}
