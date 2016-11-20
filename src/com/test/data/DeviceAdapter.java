package com.test.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.test.R;
import com.test.UI.LaunchActivity;

import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

public class DeviceAdapter extends BaseAdapter{
	private List<DeviceInfo> data;  
    private LayoutInflater layoutInflater;  
    private Context context;  
    ViewHolder holder;
    public DeviceAdapter(Context context){  
        this.context=context;  
        this.data=new ArrayList<DeviceInfo>(); 
        this.layoutInflater=LayoutInflater.from(context);  
    }  
    
    public final class ViewHolder
    {
    	public TextView MacAddress;
    	public TextView online;
    	public ToggleButton tbtn;
    	public LinearLayout ctrlPanelLayout;
    	public RadioGroup rg;
    	public Button btn;
    }
    
    @Override  
    public int getCount() {  
        return data.size();  
    }  
    /** 
     * ���ĳһλ�õ����� 
     */  
    @Override  
    public Object getItem(int position) {  
        return data.get(position);  
    }  
    /** 
     * ���Ψһ��ʶ 
     */  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(final int position, View convertView, final ViewGroup parent) {   
    	holder=null;  
        if(convertView==null){  
        	holder=new ViewHolder();  
            //��������ʵ�������  
            convertView=layoutInflater.inflate(R.layout.device_list, null);  
            holder.MacAddress=(TextView)convertView.findViewById(R.id.tv_deviceMac);
            holder.online=(TextView)convertView.findViewById(R.id.tv_deviceOnline);
            holder.tbtn=(ToggleButton)convertView.findViewById(R.id.tbtn_ctrlpanel);
            holder.ctrlPanelLayout=(LinearLayout)convertView.findViewById(R.id.ly_ctrlPanel);
            holder.rg=(RadioGroup)convertView.findViewById(R.id.rg_ctrlMsg);
            holder.btn=(Button)convertView.findViewById(R.id.btn_sendCtrlMsg);
            
            convertView.setTag(holder);  
        }else{  
        	holder=(ViewHolder)convertView.getTag();  
        }  
        //������  
        
        holder.MacAddress.setText(data.get(position).MacAddress);
        holder.online.setText(data.get(position).online?"online":"offline");
        holder.tbtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				ToggleCtrlPanel(position,isChecked,parent);
			}
		});
        holder.btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RadioGroup rg=(RadioGroup)parent.getChildAt(position).findViewById(R.id.rg_ctrlMsg);
				TextView tv=(TextView)parent.getChildAt(position).findViewById(R.id.tv_deviceMac);
				// TODO Auto-generated method stub
				String strMac="";
				int ctrlType=0;
				switch(rg.getCheckedRadioButtonId())
				{
					case R.id.rb_open:
						strMac=tv.getText().toString();
						ctrlType=1;
						break;
					case R.id.rb_close:
						strMac=tv.getText().toString();
						ctrlType=2;
						break;
					case R.id.rb_stop:
						strMac=tv.getText().toString();
						ctrlType=3;
						break;
					default:
						break;
				}
				listener.onSendMsg(strMac, ctrlType);
			}
		});
        return convertView;   
    }
    
    public List<DeviceInfo> getDataList()
    {
    	return data;
    }
    
    public int findDeviceMac(String MAC)
    {
    	for(int i=0;i<data.size();i++)
    	{
    		if(data.get(i).MacAddress.toUpperCase().equals(MAC.toUpperCase()))
    		{
    			return i;
    		}
    	}
    	return -1;
    }

	private void ToggleCtrlPanel(int position, boolean isChecked,ViewGroup view) {
		// TODO Auto-generated method stub
		view.getChildAt(position).findViewById(R.id.ly_ctrlPanel).setVisibility(isChecked?View.GONE:View.VISIBLE);
	}
	
	public void setOnSendCtrlMsgListener(onSendCtrlMsgListener l)
	{
		listener=l;
	}
		
	private onSendCtrlMsgListener listener;
	
	public interface onSendCtrlMsgListener
	{
		void onSendMsg(String MAC,int ctrlType);
	}
}
