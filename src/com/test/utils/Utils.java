package com.test.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.test.UI.MyApplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Utils {
	public static byte byteApp2Server='%';
	public static byte byteServer2App='$';
	public static byte byteClient2Server='.';
	public static byte byteServer2Client=':';
	public static byte byteEnd='#';
	
    /** 
     * ��ȡ�绰���� 
     */  
	private static String NativePhoneNumber="";
	
    public static String getNativePhoneNumber() {  
        if(NativePhoneNumber.isEmpty()||NativePhoneNumber=="")
        {
	        TelephonyManager telephonyManager = (TelephonyManager) MyApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
	        try
	        {
	        	NativePhoneNumber=telephonyManager.getLine1Number();  
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
        }
        return NativePhoneNumber;  
    }

	public static void setNativePhoneNumber(String str)
	{
		NativePhoneNumber=str;
	}
    
    /* Convert byte[] to hex string.�������ǿ��Խ�byteת����int��Ȼ������Integer.toHexString(int)��ת����16�����ַ�����   
    * @param src byte[] data   
    * @return hex string   
    */      
   public static String bytesToHexString(byte[] src){   
       StringBuilder stringBuilder = new StringBuilder("");   
       if (src == null || src.length <= 0) {   
           return null;   
       }   
       for (int i = 0; i < src.length; i++) 
       {   
           int v = src[i] & 0xFF;   
           String hv = Integer.toHexString(v);   
           if (hv.length() < 2) {   
               stringBuilder.append(0);   
           }   
           stringBuilder.append(hv);   
       }   
       return stringBuilder.toString();   
   }   
   
   /**  
    * Convert hex string to byte[]  
    * @param hexString the hex string  
    * @return byte[]  
    */  
   public static byte[] hexStringToBytes(String hexString) {   
       if (hexString == null || hexString.equals("")) {   
           return null;   
       }   
       hexString = hexString.toUpperCase();  
       char[] hexChars = transformArrayToEven(hexString.toCharArray());
       byte[] d = new byte[hexChars.length/2];   
       for (int i = 0; i <hexChars.length; i++) 
       {  
    	   byte b;
    	   if(i%2==0)
    	   {
    		   b=(byte)(charToByte(hexChars[i])<<4);
    	   }
    	   else
    	   {
    		   b=charToByte(hexChars[i]);
    	   }
    	   d[i/2]+=b;
       }   
       return d;   
   }   
   
   private static char[] transformArrayToEven(char[] c)
   {
	   if(c.length%2==0)
	   {
		   return c;
	   }
	   else
	   {
		   char[] result=new char[c.length+1];
		   result[0]='0';
		   for(int i=0;i<c.length;i++)
		   {
			   result[i+1]=c[i];
		   }
		   return result;
	   }
   }
   
   /**  
    * Convert char to byte  
    * @param c char  
    * @return byte  
    */  
    public static byte charToByte(char c) 
    {   
       return (byte) "0123456789ABCDEF".indexOf(c);   
    }  
    
    public static char byteToChar(byte b)
    {
    	return "0123456789ABCDEF".toCharArray()[b];
    }
    
    public static String getPhoneNumFromBytes(byte[] bt)
    {
    	String str="";
    	for(int i=0;i<bt.length;i++)
    	{
    		str+=String.valueOf(bt[i]);
    	}
    	return str;
    }
  
    public static String getTime()
    {
    	return new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
    }
    
    public static Boolean MacAddressMatch(String str)
    {
    	String reg="^([0-9A-Fa-f][0-9A-Fa-f]-){5}[0-9A-Fa-f][0-9A-Fa-f]$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		boolean b = matcher.matches();
    	return b;
    }
    
    public static Boolean HexByteMatch(String str)
    {
    	String reg = "^[0-9A-Fa-f][0-9A-Fa-f]$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		boolean b = matcher.matches();
    	return b;
    }
    
    public static Boolean IPNumMatch(String str)
    {
    	String reg="^(1\\d{0,2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		boolean b = matcher.matches();
    	return b;
    }
    
    public static String StringJoin(String join,String...params)
    {
    	String result="";
    	for(int i=0;i<params.length;i++)
    	{
    		if(i==params.length-1)
    		{
    			result+=params[i];
    		}
    		else
    		{
    			result+=params[i]+join;
    		}
    	}
    	return result;
    }
    
    final static String DRIVER_NAME = "com.mysql.jdbc.Driver";

    public static Connection openConnection() {
        Connection conn = null;
        try {
            Class.forName(DRIVER_NAME);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            conn = null;
        } catch (SQLException e) {
            conn = null;
        }
        System.out.println(conn);
        return conn;
    }

    public static void InsertUser(Connection conn, final String usr, final String pwd, Handler h) 
    {
  		if(queryUserExist(conn, usr)==true)
  		{
  			Message msg=h.obtainMessage();
  			msg.what=0x02;
  			msg.sendToTarget();
  		}
  		else
  		{
  			String sql = "insert into user values('"+usr+"', '"+pwd+"')";
			boolean result=Utils.execSQL(conn, sql);
			Message msg=h.obtainMessage();
  			msg.what=result?0x00:0x01;
  			Bundle b =new Bundle();
  			b.putString("usr", usr);
  			b.putString("pw", pwd);
  			msg.setData(b);
  			msg.sendToTarget();
  		}
  	}
	
    public static boolean queryUserExist(Connection conn, String usr)
    {
    	if (conn == null) {
            return false;
        }
        Statement statement = null;
        ResultSet result = null;
        try {
        	String format="select * from user where usr='%s'";
        	String sql=String.format(format, usr);
        	
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            System.out.println(result);
            if (result != null && result.first()) 
            {
                return true;
            }
            else
            {
            	 return false;
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return false;
        } 
        finally
        {
            try 
            {
                if (result != null) {
                    result.close();
                    result = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
            } 
            catch (SQLException sqle) 
            {
                sqle.printStackTrace();
            }
        }
    }
    
    public static void query(Connection conn, String sql, Handler h) {
        if (conn == null) {
            return;
        }
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            System.out.println(result);
            if (result != null && result.first()) 
            {
                int idColumnIndex = result.findColumn("usr");
                int nameColumnIndex = result.findColumn("pwd");
                System.out.println("id\t\t" + "name");
                while (!result.isAfterLast()) {
                    System.out.print(result.getString(idColumnIndex) + "\t\t");
                    System.out.println(result.getString(nameColumnIndex));
                    result.next();
                }
                Message msg=h.obtainMessage();
                msg.what=0x01;
                msg.arg1=1;
                msg.sendToTarget();
            }
            else
            {
            	 Message msg=h.obtainMessage();
                 msg.what=0x01;
                 msg.arg1=0;
                 msg.sendToTarget();
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            Message msg=h.obtainMessage();
            msg.what=0x01;
            msg.arg1=2;
            msg.sendToTarget();
        } 
        finally
        {
            try 
            {
                if (result != null) {
                    result.close();
                    result = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if(conn!=null)
                {
                	conn.close();
                	conn=null;
                }
                
            } 
            catch (SQLException sqle) 
            {
                sqle.printStackTrace();
            }
        }
    }

    public static boolean execSQL(Connection conn, String sql) {
        boolean execResult = false;
        if (conn == null) {
            return execResult;
        }
        Statement statement = null;
        try {
            statement = conn.createStatement();
            if (statement != null) {
                statement.execute(sql);
                execResult=true;
            }
        } catch (SQLException e) {
            execResult = false;
        }
        return execResult;
    }
    
    public static final String REMOTE_IP = "120.25.217.224";
    public static final String DATABASE="mydatabase";
    public static final String URL = "jdbc:mysql://" + REMOTE_IP + "/"+DATABASE;
	public static final String USER = "shenyu";
	public static final String PASSWORD = "shenyu123";

}
