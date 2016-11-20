package com.test.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.test.utils.MyMessage;
import com.test.utils.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ClientThread extends Thread {  
   
	private Socket socket;  
	private InputStream input=null;
    private OutputStream output = null;
    private InputStreamReader inputReader=null;
    private BufferedReader br = null;  
    
    private String serverIP;
    private int serverPort;
    private String str;
    // 定义向UI线程发送消息的Handler对象  
    Handler handler;  
    // 定义接收UI线程的Handler对象  
    public Handler revHandler;  
    // 该线程处理Socket所对用的输入输出流  
    
    private ClientComThread clientComThread=null;
    
  
    public ClientThread(String IP,int port,Handler handler) {  
        this.handler = handler;
        serverIP=IP;
        serverPort=port;
    }  
  
    @Override  
    public void run() {  
        socket = new Socket();  
        try {  
            socket.connect(new InetSocketAddress(serverIP, serverPort), 3000); 
            input=socket.getInputStream();
            inputReader=new InputStreamReader(input);
            br = new BufferedReader(inputReader);  
            output = socket.getOutputStream();  
            sendConnectMsg();
            // 启动一条子线程来读取服务器相应的数据  
            Message msg=new Message();
            msg.what=(byte)0x00;
            Bundle b=new Bundle();
            b.putString("ip", serverIP);
            b.putInt("port", serverPort);
            msg.setData(b);
            handler.sendMessage(msg);
            
            clientComThread=new ClientComThread(socket); 
            clientComThread.start();
            // 为当前线程初始化Looper  
            Looper.prepare();  
            // 创建revHandler对象  ,处理UI线程的操作
            revHandler = new Handler() {  
  
                @Override  
                public void handleMessage(Message msg) {  
                    switch(msg.what)
                    {
						case (byte) 0x00:
							sendMsgToServer(MyMessage.getMessage(msg));
							break;
						case (byte) 0x01:
							try {
								addDevice(MyMessage.getMessage(msg));
							}
							catch (Exception e)
							{
								handler.sendMessage(MyMessage.insertMessage((byte)0xEE,e.toString()));
							}
							break;
						case (byte) 0x02:
							delDevice(MyMessage.getMessage(msg));
							break;
						case (byte) 0x10:
							ctrlDevice((CtrlInfo) msg.obj);
							break;
						case (byte) 0x88:
							heartBeat();
							break;
						default:
							break;
                    }
                }  
  
            };   
            // 启动Looper  
            Looper.loop();  
  
        }
        catch(ConnectException ce)
        {
            handler.sendMessage(MyMessage.insertMessage((byte)0x91, "网络超时"));  
        }
        catch (SocketTimeoutException e) {  
            handler.sendMessage(MyMessage.insertMessage((byte)0x91, "网络连接超时！"));  
        }
        catch (IOException io) {  
            io.printStackTrace();  
        }  
  
    }  
    
    private void sendMsgToServer(String str)
    {
    	try {  
            output.write((str+ "\r\n").getBytes("gbk"));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
    
    
    private void sendConnectMsg()
    {
    	byte[] buf=new byte[23];
    	
    	buf[0]=Utils.byteApp2Server;
    	
    	String phoneNum=Utils.getNativePhoneNumber();
    	char[] charPhoneNum=phoneNum.toCharArray();
    	byte[] bytePhoneNum=new byte[11];
    	for(int i=0;i<11;i++)
    	{
    		bytePhoneNum[i]=Utils.charToByte(charPhoneNum[i]);
    	}
    	System.arraycopy(bytePhoneNum, 0, buf, 1, 11);
    	
    	buf[18]=(byte)0xFF;
    	buf[22]=Utils.byteEnd;
    	try 
    	{
			output.write(buf);
			output.flush();
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    private void addDevice(String MACAddress)
    {
    	byte[] buf=new byte[23];
    	
    	buf[0]=Utils.byteApp2Server;
    	
    	String phoneNum=Utils.getNativePhoneNumber();
    	char[] charPhoneNum=phoneNum.toCharArray();
    	byte[] bytePhoneNum=new byte[11];
    	for(int i=0;i<11;i++)
    	{
    		bytePhoneNum[i]=Utils.charToByte(charPhoneNum[i]);
    	}
    	System.arraycopy(bytePhoneNum, 0, buf, 1, 11);
    	
    	byte[] byteMac=Utils.hexStringToBytes(MACAddress);
    	System.arraycopy(byteMac, 0, buf, 12, 6);
    	
    	buf[18]=(byte)0x01;
    	buf[22]=Utils.byteEnd;
    	try 
    	{
			output.write(buf);
			output.flush();
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    private void delDevice(String MACAddress)
    {
    	byte[] buf=new byte[23];
    	
    	buf[0]=Utils.byteApp2Server;
    	
    	String phoneNum=Utils.getNativePhoneNumber();
    	char[] charPhoneNum=phoneNum.toCharArray();
    	byte[] bytePhoneNum=new byte[11];
    	for(int i=0;i<11;i++)
    	{
    		bytePhoneNum[i]=Utils.charToByte(charPhoneNum[i]);
    	}
    	System.arraycopy(bytePhoneNum, 0, buf, 1, 11);
    	
    	byte[] byteMac=Utils.hexStringToBytes(MACAddress);
    	System.arraycopy(byteMac, 0, buf, 12, 6);
    	
    	buf[18]=(byte)0x02;
    	buf[22]=Utils.byteEnd;
    	try 
    	{
			output.write(buf);
			output.flush();
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    private void ctrlDevice(CtrlInfo ctrl)
    {
    	byte[] buf=new byte[23];
    	
    	buf[0]=Utils.byteApp2Server;
    	
    	String phoneNum=Utils.getNativePhoneNumber();
    	char[] charPhoneNum=phoneNum.toCharArray();
    	byte[] bytePhoneNum=new byte[11];
    	for(int i=0;i<11;i++)
    	{
    		bytePhoneNum[i]=Utils.charToByte(charPhoneNum[i]);
    	}
    	System.arraycopy(bytePhoneNum, 0, buf, 1, 11);
    	
    	String MACAddress=ctrl.MacAddress;
    	byte[] byteMac=Utils.hexStringToBytes(MACAddress);
    	System.arraycopy(byteMac, 0, buf, 12, 6);
    	
    	buf[18]=(byte)0x10;
    	buf[20]=(byte)ctrl.operateType;
    	buf[22]=Utils.byteEnd;
    	try 
    	{
			output.write(buf);
			output.flush();
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    private void heartBeat()
    {
    	byte[] buf=new byte[23];
    	
    	buf[0]=Utils.byteApp2Server;
    	
    	String phoneNum=Utils.getNativePhoneNumber();
    	char[] charPhoneNum=phoneNum.toCharArray();
    	byte[] bytePhoneNum=new byte[11];
    	for(int i=0;i<11;i++)
    	{
    		bytePhoneNum[i]=Utils.charToByte(charPhoneNum[i]);
    	}
    	System.arraycopy(bytePhoneNum, 0, buf, 1, 11);
    	    	
    	buf[18]=(byte)0x88;
    	buf[22]=Utils.byteEnd;
    	try 
    	{
			output.write(buf);
			output.flush();

			handler.sendMessage(MyMessage.insertMessage((byte)0x88, "send heartbeat"));
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    
    
    public void closeSocket()
    {    	
		try 
		{
			str="client disconnect, IP："+socket.getInetAddress().toString();
			handler.sendMessage(MyMessage.insertMessage((byte) 0x91, str));
			this.interrupt();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
    
    @Override
   	public void interrupt() {
   		// TODO Auto-generated method stub
    	try
    	{
	    	if(socket!=null)
	    	{
	    		socket.close();
	    	}
	    	if(input!=null)
	    	{
	    		input.close();
	    	}
	    	if(output!=null)
	    	{
	    		output.close();
	    	}
	    	if(inputReader!=null)
	    	{
	    		inputReader.close();
	    	}
	    	if(br!=null)
	    	{
	    		br.close();
	    	}
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    	} 
    	finally
    	{
    		super.interrupt();
    	}
   	}

    
    class ClientComThread extends Thread
    {
    	private Socket socket;
    	
    	private byte[] bufRcv=new byte[8192];
    	private byte[] buf=new byte[23];
    	
    	public ClientComThread(Socket s) 
    	{
			// TODO Auto-generated constructor stub
    		this.socket=s;
		}
    	@Override  
        public void run() {  
            String content = null;  
            int size=0;
            // 不断的读取Socket输入流的内容  
            try 
            {  
            	while((size=input.read(bufRcv))>=0)
				{
                	System.arraycopy(bufRcv, 0, buf, 0, 23);
                    handler.sendMessage(MyMessage.insertMessage((byte)0x90, "receive server bytes: "+Utils.bytesToHexString(buf)));
                    dataProcess(buf[18],buf);
                }  
            } 
            catch (IOException io) 
            {  
                io.printStackTrace();  
            }  
        }
    	
    	private void dataProcess(byte cmd,byte[] buf)
    	{
    		switch(cmd)
    		{
				case (byte)0x01:
					rcvaddDeviceReply(buf);
					break;
				case (byte)0x02:
					rcvdelDeviceReply(buf);
					break;
				case (byte)0x10:
					rcvctrlDeviceReply(buf);
					break;
				case (byte)0x11:
					rcvDeviceState(buf);
					break;
				case (byte)0xFF:
					rcvonlineReply(buf);
					break;
				case (byte)0x88:
					rcvheartBeatReply(buf);
					break;
				case (byte)0x99:
					recDeviceOnline(buf);
				default:
					break;
    		}
    	}

		private void rcvaddDeviceReply(byte[] buf) {
			String str;
			byte[] Mac=new byte[6];
			System.arraycopy(buf, 12, Mac, 0, 6);
			
			Message msg=new Message();
			msg.what=0x01;
			Bundle b=new Bundle();
			b.putString("MAC", Utils.bytesToHexString(Mac));
			msg.setData(b);
            handler.sendMessage(msg);
		}
		
		private void rcvdelDeviceReply(byte[] buf) {
			String str;
			byte[] Mac=new byte[6];
			System.arraycopy(buf, 12, Mac, 0, 6);
			
			Message msg=new Message();
			msg.what=0x02;
			Bundle b=new Bundle();
			b.putString("MAC", Utils.bytesToHexString(Mac));
			msg.setData(b);
            handler.sendMessage(msg);
		}
		
		private void rcvctrlDeviceReply(byte[] buf) {
			byte[] Mac=new byte[6];
			System.arraycopy(buf, 12, Mac, 0, 6);
            handler.sendMessage(MyMessage.insertMessage((byte)0x10, "server: "+CtrlInfo.operation[buf[20]-1]+" device "
            		+Utils.bytesToHexString(Mac)+" successfully"));
		}
		
		private void rcvDeviceState(byte[] buf) {
			byte[] Mac=new byte[6];
			System.arraycopy(buf, 12, Mac, 0, 6);
			String strmsg="server: device "
            		+Utils.bytesToHexString(Mac)+" state "+CtrlInfo.deviceState[buf[20]];
            handler.sendMessage(MyMessage.insertMessage((byte)0x11, strmsg));
            
//            Message msg=new Message();
//            Bundle b=new Bundle();
//            b.putString("MAC", Utils.bytesToHexString(Mac));
//            b.putString("deviceState", CtrlInfo.deviceState[buf[20]]);
//            msg.setData(b);
//            handler.sendMessage(msg);
		}
		
		private void rcvonlineReply(byte[] buf) {
			byte[] btPhoneNum=new byte[11];
			String phoneNum="";
			
			System.arraycopy(buf, 1, btPhoneNum, 0, 11);			
			phoneNum=Utils.getPhoneNumFromBytes(btPhoneNum);
			
			handler.sendMessage(MyMessage.insertMessage((byte)0x91, phoneNum+" online"));
		}
		
		private void rcvheartBeatReply(byte[] buf){
            handler.sendMessage(MyMessage.insertMessage((byte)0x88, "receive server heartbeat"));
		}

		private void recDeviceOnline(byte[] buf) {
			byte[] Mac=new byte[6];
			System.arraycopy(buf, 12, Mac, 0, 6);
			handler.sendMessage(MyMessage.insertMessage((byte)0x99, Utils.bytesToHexString(Mac)));
		}
    	
    }
}  