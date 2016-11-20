package com.test.UI;

import com.test.R;
import com.test.utils.Utils;

import android.content.Context;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard.Key;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class IpAddressEditor extends LinearLayout{
	private int[] editTextIdList=new int[5];
	private EditText[] editTextList=new EditText[5];
	
	
	public IpAddressEditor(Context context, AttributeSet attrs)
	{
		super(context,attrs);
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ip_address_editor, this);
		
		editTextIdList[0]=R.id.edt_IP_1;
		editTextIdList[1]=R.id.edt_IP_2;
		editTextIdList[2]=R.id.edt_IP_3;
		editTextIdList[3]=R.id.edt_IP_4;
		editTextIdList[4]=R.id.edt_Port;
		
		
		for(int i=0;i<5;i++)
		{
			editTextList[i]=(EditText)findViewById(editTextIdList[i]);
			if(i<4)
			{
				editTextList[i].setInputType(EditorInfo.TYPE_CLASS_PHONE); //要设置edittext的singleline属性为true，不然会白屏
			}
		}

		
	}
	
	private void JumpPrev(int edtId)
	{
		for(int i=1;i<4;i++)
		{
			if(editTextIdList[i]==edtId)
			{
				editTextList[i-1].setFocusable(true);
				editTextList[i-1].setFocusableInTouchMode(true);
				editTextList[i-1].requestFocus();
				editTextList[i-1].requestFocusFromTouch();
				return;
			}
		}
	}
	
	private void JumpNext(int edtId)
	{
		for(int i=0;i<4;i++)
		{
			if(editTextIdList[i]==edtId)
			{
				editTextList[i+1].setFocusable(true);
				editTextList[i+1].setFocusableInTouchMode(true);
				editTextList[i+1].requestFocus();
				editTextList[i+1].requestFocusFromTouch();
				return;
			}
		}
	}
	
	private void TrimDot(int edtId)
	{
		for(int i=0;i<4;i++)
		{
			if(editTextIdList[i]==edtId)
			{				
				String str=editTextList[i].getText().toString();
				editTextList[i].setText(str.substring(0, str.length()-1));
				return;
			}
		}
	}
	
	private Boolean checkIPNumCompleted(String str)
	{
		if(str.matches("\\d{3}|[3-9][0-9]|2[6-9]|0"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String getIPAddress()
	{		
		return Utils.StringJoin(".", editTextList[0].getText().toString(),editTextList[1].getText().toString(),
				editTextList[2].getText().toString(),editTextList[3].getText().toString());	
	}	
	
	public int getPort()
	{
		return Integer.parseInt(editTextList[4].getText().toString());
	}
	
	public void setIpAddress(String ipAddress)
	{
		String[] ip=ipAddress.split("\\.");
		for(int i=0;i<4;i++)
		{
			editTextList[i].setText(ip[i]);
		}
	}
	
	public void setPort(int port)
	{
		editTextList[4].setText(String.valueOf(port));
	}

	
}
