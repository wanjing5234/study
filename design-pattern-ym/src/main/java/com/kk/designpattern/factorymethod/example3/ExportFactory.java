package com.kk.designpattern.factorymethod.example3;

/**
 * 实现导出数据的业务功能对象
 */
public class ExportFactory {
	/**
	 * 导出文件
	 * @param type 用户选择的导出类型
	 * @param data 需要保存的数据
	 * @return 是否成功导出文件
	 */
//	public boolean export(String data){
//		//ExportFileApi api = factoryMethod();
//		//return api.export(data);
//	}
	
	public <T extends ExportFileApi> T createExportFileApi(Class<T> c){
		ExportFileApi api = null;
		try {
			api = (T) Class.forName(c.getName()).newInstance();
		} catch (Exception e) {
		}
		return (T) api;
	}
	
}
