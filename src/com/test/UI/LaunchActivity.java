package com.test.UI;
import java.util.List;

import com.test.R;
import com.test.data.ClientThread;
import com.test.data.CtrlInfo;
import com.test.data.DeviceAdapter;
import com.test.data.DeviceAdapter.onSendCtrlMsgListener;
import com.test.data.DeviceInfo;
import com.test.utils.MyMessage;
import com.test.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class LaunchActivity extends Activity implements OnClickListener, onSendCtrlMsgListener{

	private ViewContainer m_viewContainer;
	
	private ClientThread clientThread;
	
	private SharedPreferences.Editor editor;
	
	private List<DeviceInfo> devicelist;
	
	private DeviceAdapter deviceAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try
		{
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			
			setContentView(R.layout.activity_launch);
			
			
			m_viewContainer=new ViewContainer(this);
			try
			{
				initListView();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			initParams();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void initListView() {
		// TODO Auto-generated method stub
		 //List<DeviceInfo> list=getData(); 
		 deviceAdapter=new DeviceAdapter(this);
		 deviceAdapter.setOnSendCtrlMsgListener(this);
    	 m_viewContainer.get_listview_deviceList().setAdapter(deviceAdapter);  
    	 
	}

	private void initParams() {
		try
		{
			String phoneNum = Utils.getNativePhoneNumber();
			m_viewContainer.get_edt_phoneNum().setText(phoneNum);
		}
		catch(Exception e)
		{
			m_viewContainer.get_tv_clientMesRec().append(e.toString());
			Toast.makeText(this,"fail to read PhoneNumber",Toast.LENGTH_LONG);
		}
		if(getSharedPreferences("data", MODE_PRIVATE).getString("ServerIP", null)!=null)
		{
			m_viewContainer.get_serverIP().setIpAddress(
					getSharedPreferences("data", MODE_PRIVATE).getString("ServerIP", null));
		}
		if(getSharedPreferences("data", MODE_PRIVATE).getInt("ServerPort", 0)!=0)
		{
			m_viewContainer.get_serverIP().setPort(
					getSharedPreferences("data", MODE_PRIVATE).getInt("ServerPort", 0));
		}
		m_viewContainer.get_edt_MACAddr().setMacAddress();
	}
	
	public Handler m_handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
        	String MACAddress="";
            switch(msg.what)
            {
        		case 0x00:
        			m_viewContainer.get_tv_clientMesRec().append("connect server "+
        					msg.getData().getString("ip")+":"+msg.getData().getInt("port")
        					+" successfully   "+Utils.getTime()+"\n"); 
        			editor=getSharedPreferences("data", MODE_PRIVATE).edit();
        			editor.putString("ServerIP", msg.getData().getString("ip"));
        			editor.putInt("ServerPort", msg.getData().getInt("port"));
        			editor.commit();
        			break;
        		case 0x01:
        			MACAddress=msg.getData().getString("MAC");
        			if(deviceAdapter.findDeviceMac(MACAddress)<0)
        			{
            			deviceAdapter.getDataList().add(new DeviceInfo(MACAddress.toUpperCase(), false));
            			deviceAdapter.notifyDataSetChanged();     
            			Toast.makeText(MyApplication.getContext(), "add device successfully", Toast.LENGTH_LONG).show();
        			}
        			else
        			{
        				Toast.makeText(MyApplication.getContext(), "device already added", Toast.LENGTH_LONG).show();
        			}
        			break;
        		case 0x02:
        			MACAddress=msg.getData().getString("MAC");
        			int index=deviceAdapter.findDeviceMac(MACAddress);
        			if(index>=0)
        			{
	        			deviceAdapter.getDataList().remove(index);
	        			deviceAdapter.notifyDataSetChanged();
	        			Toast.makeText(MyApplication.getContext(), "remove device successfully", Toast.LENGTH_LONG).show();
        			}
        			else
        			{
        				Toast.makeText(MyApplication.getContext(), "device not added", Toast.LENGTH_LONG).show();
        			}
        			break;
            	case 0x10:
            		m_viewContainer.get_tv_clientMesRec().append(msg.getData().getString("msg")+"\n");
            		break;
            	case 0x11:
//            		m_viewContainer.get_tv_clientMesRec().append  ("server: device "
//            				+msg.getData().getString("MAC")+" state "+
//            				msg.getData().getString("deviceState")+"     "+Utils.getTime()+"\n");
            		m_viewContainer.get_tv_clientMesRec().append(msg.getData().getString("msg")+"\n");
            		break;
            	case (byte)0x88:
            		m_viewContainer.get_tv_clientMesRec().append(msg.getData().getString("msg")+"     "+Utils.getTime()+"\n");
            		break;
            	case (byte)0x90:	//正常读取socket字节流
            		m_viewContainer.get_tv_clientMesRec().append(msg.getData().getString("msg")+"     "+Utils.getTime()+"\n");
            		break;
            	case (byte)0x91:	//其他信息
            		m_viewContainer.get_tv_clientMesRec().append(msg.getData().getString("msg")+"     "+Utils.getTime()+"\n");
        			break;
				case (byte)0x99:
					handleDeviceOnline(msg.getData().getString("msg"));
					break;
				case (byte)0xEE:
					new AlertDialog.Builder(LaunchActivity.this).setTitle("thread Error").setMessage(msg.getData().getString("msg")).show();
					break;
            	default:
            		break;
            }
            m_viewContainer.get_tv_clientMesRec().scrollTo(0, m_viewContainer.get_tv_clientMesRec().getBottom());
        }
 
	  
	};


	Runnable runnable = new Runnable() {  
		  
        @Override  
        public void run() {  
            // handler自带方法实现定时器  
            try {  
            	sendHeartBeatMsg();
            	m_handler.postDelayed(this, 30000);  
            } catch (Exception e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
    };  

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.btn_confirm_phoneNum:
				confirmPhoneNum();
				break;
			case R.id.btn_connectServer:
				connectServer();
				break;
			case R.id.btn_disconnectServer:
				disconnectServer();
				break;
			case R.id.btn_addDevice:
				addDevice();
				break;
			case R.id.btn_delDevice:
				delDevice();
				break;
			default:
				break;
		}
	}

	private void confirmPhoneNum() {
		String phoneNum=m_viewContainer.get_edt_phoneNum().getText().toString();
		Utils.setNativePhoneNumber(phoneNum);
		Toast.makeText(this,"set PhoneNum successfully",Toast.LENGTH_LONG).show();
	}


	private void connectServer()
	{
		clientThread=new ClientThread(m_viewContainer.get_serverIP().getIPAddress(),
				m_viewContainer.get_serverIP().getPort(),m_handler);
		new Thread(clientThread).start();  
		m_handler.postDelayed(runnable, 30000);

	}
	
	private void disconnectServer()
	{
		if(clientThread!=null)
		{
			clientThread.closeSocket();
		}
	}

	private void addDevice()
	{
		try {
			clientThread.revHandler.sendMessage(MyMessage.insertMessage
					((byte) 0x01, m_viewContainer.get_edt_MACAddr().getMacAddress().toString()));			
		}
		catch(Exception e)
		{
			new AlertDialog.Builder(this).setTitle("send Error").setMessage(e.toString()).show();
		}
	}
	
	private void delDevice()
	{
		clientThread.revHandler.sendMessage(MyMessage.insertMessage
				((byte) 0x02, m_viewContainer.get_edt_MACAddr().getMacAddress().toString()));
	}

	private void sendCtrlMsg(String MACAddress, int ctrltype) {
		Message msg=new Message();
		msg.what=0x10;
		msg.obj=new CtrlInfo(MACAddress, ctrltype);
		clientThread.revHandler.sendMessage(msg);
	} 
	
	private void sendHeartBeatMsg()
	{
		clientThread.revHandler.sendMessage(MyMessage.insertMessage
				((byte)0x88, "heartbeat"));
	}


	private void handleDeviceOnline(String MACAddress)
	{
		for(int i=0;i<deviceAdapter.getDataList().size();i++)
		{
			if (deviceAdapter.getDataList().get(i).MacAddress.toUpperCase().equals(MACAddress.toUpperCase()))
			{
				if (!deviceAdapter.getDataList().get(i).online)
				{
					deviceAdapter.getDataList().get(i).online = true;
					deviceAdapter.notifyDataSetChanged();
				}
				return;
			}
		}
	}

	@Override
	public void onSendMsg(String MAC, int ctrlType) {
		// TODO Auto-generated method stub
		sendCtrlMsg(MAC,ctrlType);
	}  


	
}
