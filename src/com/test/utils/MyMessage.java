package com.test.utils;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;

public class MyMessage {
	public static Message insertMessage(byte b,String str)
	{
		Message msg=new Message();
		Bundle bundle=new Bundle();
		bundle.putString("msg", str);
		msg.what=b;
		msg.setData(bundle);
		return msg;
	}
	
	public static Message insertMessage(byte b,ArrayList<String> strlist)
	{
		Message msg=new Message();
		Bundle bundle=new Bundle();
		bundle.putStringArrayList("msg", strlist);
		msg.what=b;
		msg.setData(bundle);
		return msg;
	}
	
	
	public static String getMessage(Message msg)
	{
		return msg.getData().getString("msg");
	}
}
