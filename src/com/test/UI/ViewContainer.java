package com.test.UI;



import com.test.R;

import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ViewContainer {
	private EditText tv_phoneNum;
	private Button btn_mofidyPhoneNum;
	private IpAddressEditor ipaddrEditor;
	private Button btn_connectServer;
	private Button btn_disconnectServer;
	private TextView tv_clientMesRec;
	
	private MacAddressEditor edt_MACAddr;
	private Button btn_addDevice;
	private Button btn_delDevice;
	private RadioGroup rg_ctrlMsg;
	private MyListView lv_deviceList;
	private Button btn_sendCtrlMsg;
	
	
	private Context m_Context;
	
	public ViewContainer(Context c)
	{
		m_Context = c;

		tv_phoneNum=(EditText) ((LaunchActivity)c).findViewById(R.id.tv_PhoneNum);

		btn_mofidyPhoneNum=(Button)((LaunchActivity)c).findViewById(R.id.btn_confirm_phoneNum);
		btn_mofidyPhoneNum.setOnClickListener((OnClickListener) c);
    	
    	ipaddrEditor=(IpAddressEditor)((LaunchActivity)c).findViewById(R.id.edt_IP);
    	
    	btn_connectServer=(Button)((LaunchActivity)c).findViewById(R.id.btn_connectServer);
    	btn_connectServer.setOnClickListener((OnClickListener) c);
    	
    	btn_disconnectServer=(Button)((LaunchActivity)c).findViewById(R.id.btn_disconnectServer);
    	btn_disconnectServer.setOnClickListener((OnClickListener) c);
    	
    	edt_MACAddr=(MacAddressEditor)((LaunchActivity)c).findViewById(R.id.edt_MAC);
    	
    	btn_addDevice=(Button)((LaunchActivity)c).findViewById(R.id.btn_addDevice);
    	btn_addDevice.setOnClickListener((OnClickListener) c);
    	
    	btn_delDevice=(Button)((LaunchActivity)c).findViewById(R.id.btn_delDevice);
    	btn_delDevice.setOnClickListener((OnClickListener) c);
    	
    	lv_deviceList=(MyListView)((LaunchActivity)c).findViewById(R.id.list); 
    	
    	tv_clientMesRec=(TextView)((LaunchActivity)c).findViewById(R.id.tv_clientRec);
    	
	}

	public EditText get_edt_phoneNum(){return tv_phoneNum;}
	public Button get_btn_modifyPhoneNum(){return btn_mofidyPhoneNum;}
	public IpAddressEditor get_serverIP(){return ipaddrEditor;}
	public Button get_btn_connectServer(){return btn_connectServer;}
	public Button get_btn_disconnectServer(){return btn_disconnectServer;}
	public MacAddressEditor get_edt_MACAddr(){return edt_MACAddr;}	
	public Button get_btn_addDevice(){return btn_addDevice;}	
	public Button get_btn_delDevice(){return btn_delDevice;}	
	public RadioGroup get_rg_ctrlMsg(){return rg_ctrlMsg;}	
	public Button get_btn_sendCtrlMsg(){return btn_sendCtrlMsg;}	
	public ListView get_listview_deviceList(){return lv_deviceList;}
	public TextView get_tv_clientMesRec(){return tv_clientMesRec;}
	
	public int get_btn_sendCtrlMsg_checkedIndex()
	{
		switch(rg_ctrlMsg.getCheckedRadioButtonId())
		{
			case R.id.rb_open:
				return 0;
			case R.id.rb_close:
				return 1;
			case R.id.rb_stop:
				return 2;
			default:
				return 0;
		}
	}
}
