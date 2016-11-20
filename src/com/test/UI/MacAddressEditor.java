package com.test.UI;

import com.test.R;
import com.test.utils.Utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MacAddressEditor extends LinearLayout{	
	private int[] editTextIdList=new int[6];
	private EditText[] editTextList=new EditText[6];
	
	public MacAddressEditor(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.mac_address_editor, this);
		
		editTextIdList[0]=R.id.edt_MAC_1;
		editTextIdList[1]=R.id.edt_MAC_2;
		editTextIdList[2]=R.id.edt_MAC_3;
		editTextIdList[3]=R.id.edt_MAC_4;
		editTextIdList[4]=R.id.edt_MAC_5;
		editTextIdList[5]=R.id.edt_MAC_6;
		
		for(int i=0;i<6;i++)
		{
			editTextList[i]=(EditText)findViewById(editTextIdList[i]);
			//editTextList[i].addTextChangedListener(this);
		}

	}
	
	public String getMacAddress()
	{
		String strMacAddress="";
		for(int i=0;i<6;i++)
		{
			strMacAddress+=editTextList[i].getText().toString();
		}
		return strMacAddress;
	}
	
	public void setMacAddress()
	{
		editTextList[0].setText("ac");
		editTextList[1].setText("cf");
		editTextList[2].setText("23");
		editTextList[3].setText("54");
		editTextList[4].setText("9f");
		editTextList[5].setText("fb");
	}
}
