package com.test.data;

public class CtrlInfo {
	public String MacAddress;
	public int operateType;
	public CtrlInfo(String address,int operate) {
		MacAddress=address;
		operateType=operate;
	}
	

	public static String[] operation=new String[]{"open","close","stop"};
	
	public static String[] deviceState=new String[]{"�ѹر�","stop open","stop close","opened","closed","����"};
}
