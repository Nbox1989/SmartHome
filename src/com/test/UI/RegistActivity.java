package com.test.UI;

import java.sql.Connection;

import com.test.R;
import com.test.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistActivity extends Activity implements OnClickListener{
    private Button btn_back;
    private Button btn_regist;
    
    private EditText edt_usr;
    private EditText edt_pwd;
    private EditText edt_pwd2;
	
    private Connection conn;
    
    private Handler handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 0x00:
					RegistActivity.this.finish();
					break;
				case 0x01:
					Toast.makeText(RegistActivity.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
					break;
				case 0x02:
					Toast.makeText(RegistActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}
	};
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		
		btn_back=(Button)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
		btn_regist=(Button)findViewById(R.id.btn_regist);
		btn_regist.setOnClickListener(this);
				
		
		edt_usr=(EditText)findViewById(R.id.edt_usr);
		edt_pwd=(EditText)findViewById(R.id.edt_pwd);
		edt_pwd2=(EditText)findViewById(R.id.edt_confirmpwd);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.btn_back:
				this.finish();
				break;
			case R.id.btn_regist:
				Regist();
			break;
			default:
				break;
		}
	}

	private void Regist()
	{
		String user=edt_usr.getText().toString();
		String pwd=edt_pwd.getText().toString();
		String pwd2=edt_pwd2.getText().toString();
		if(pwd.equals(pwd2))
		{
			InsertUser(user,pwd,handler);
		}
		else
		{
			Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
		}
	}

	public void InsertUser(final String usr, final String pwd, final Handler h) {
  		new Thread(new Runnable() {
  			@Override
  			public void run() {
  				conn = Utils.openConnection();
  				Utils.InsertUser(conn, usr, pwd, h);
  			}
		}).start();
  	}
	
}
