package com.kk.designpattern.factorymethod.example3;

/**
 * 实现导出数据的业务功能对象
 */
public class ExportOperate {
	/**
	 * 导出文件
	 * @param type 用户选择的导出类型
	 * @param data 需要保存的数据
	 * @return 是否成功导出文件
	 */
	public boolean export(String data){
		ExportFileApi api = null;
		//根据类型来选择究竟要创建哪一种导出文件对象
//		if(type == 1){
//			api = new ExportTxtFile();
//		}else if(type == 2){
//			api = new ExportDB();
//		}
		
		//api = factoryMethod();
		
		return api.export(data);
	}
	
	//protected abstract ExportFileApi factoryMethod();
	
	protected ExportFileApi factoryMethod(Class c){
		ExportFileApi api = null;
		try {
			api = (ExportFileApi) Class.forName(c.getName()).newInstance();
		} catch (Exception e) {
		}
		return api;
	}
}
