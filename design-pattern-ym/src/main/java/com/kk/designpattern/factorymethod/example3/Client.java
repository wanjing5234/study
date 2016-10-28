package com.kk.designpattern.factorymethod.example3;

public class Client {
	
	public static void main(String[] args) {
		ExportOperate operate = new ExportXmlOperate();
		operate.export("测试数据");
	
		ExportDB op2 = new ExportFactory().createExportFileApi(ExportDB.class);
		op2.export("测试数据");
	}
}
