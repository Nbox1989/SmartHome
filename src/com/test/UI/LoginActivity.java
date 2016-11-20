package com.test.UI;

import java.sql.Connection;
import java.sql.SQLException;

import com.test.R;
import com.test.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	private Button btn_regist;
	private Button btn_login;
	
	private EditText edt_usr;
	private EditText edt_pwd;
	
	private Connection conn;

	private Handler handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 0x01:
					int loginResult=msg.arg1;
					onLogin(loginResult);
					break;
				case 0x02:
					break;
				default:
					break;
			}
		}
	};

	private void onLogin(int loginResult) 
	{
		if(1==loginResult)
		{
			Intent i=new Intent();
			i.setClass(LoginActivity.this,LaunchActivity.class);
			startActivity(i);
		}
		else if(0==loginResult)
		{
			Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
		}
		else if(2==loginResult)
		{
			Toast.makeText(this, "Connect Error", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void closeConnection()
	{
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				conn = null;
			} finally {
				conn = null;
			}
		}
	}
	
	
  	public void openConnection() {
  		new Thread(new Runnable() {
  			@Override
  			public void run() {
  				conn = Utils.openConnection();
  			}
  		}).start();
  	}
	
  	public void InsertUser(final String usr, final String pwd) {
  		new Thread(new Runnable() {
  			@Override
  			public void run() {
  				String sql = "insert into user values("+usr+", "+pwd+")";
  				Utils.execSQL(conn, sql);
  			}
  		}).start();
  	}
	
	
  	public void DeleteUser(final String usr) {
  		new Thread(new Runnable() {
  			@Override
  			public void run() {
  				String sql = "delete from users where name='"+usr+"'";
  				Utils.execSQL(conn, sql);
  			}
  		}).start();
	}
	
  	public void onUpdate(View view) {
  		new Thread(new Runnable() {
  			@Override
  			public void run() {
  				String sql = "update users set name='lilei' where name='liyanzhen'";
  				Utils.execSQL(conn, sql);
  			}
  		}).start();
  	}
	
  	public void QueryUserPwd(final String usr, final String pwd, final Handler h) {
  		new Thread(new Runnable() {
  			@Override
  			public void run() {
  				conn = Utils.openConnection();
  				String format="select * from user where usr='%s' and pwd='%s'";
  				String query=String.format(format, usr, pwd);
  				Utils.query(conn, query, h);
  			}
		}).start();
  	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		btn_regist=(Button)findViewById(R.id.btn_regist);
		btn_regist.setOnClickListener(this);
		btn_login=(Button)findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		
		edt_usr=(EditText)findViewById(R.id.edt_usr);
		edt_pwd=(EditText)findViewById(R.id.edt_pwd);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.btn_regist:
				Intent i1=new Intent();
				i1.setClass(LoginActivity.this,RegistActivity.class);
				startActivityForResult(i1, 1000);
				break;
			case R.id.btn_login:
				String usr=edt_usr.getEditableText().toString();
				String pwd=edt_pwd.getEditableText().toString();
				QueryUserPwd(usr,pwd,handler);
				break;
			default:
				break;
		}
	}

}
