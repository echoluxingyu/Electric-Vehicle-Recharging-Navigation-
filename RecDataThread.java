package com.example.enaviapp;

import java.io.IOException;
import android.os.Message;
import android.util.Log;





public class RecDataThread extends Thread implements Runnable{
	
public Thread recVideoThread;

	

	

	MainActivity.ActivityHandler myHandler;
	public int resTime=0;
	static String[] dataArr=new String[20];
	
	//创建新线程并开启
	public RecDataThread(){
		recVideoThread = new Thread(this);
		recVideoThread.start();
		
	}		
	//终止线程
	public void OnStop(){	

		recVideoThread.stop();			
	}
	
	public void registerHandler(MainActivity.ActivityHandler handler) {
		myHandler = handler;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		try {
			String line=MainActivity.in.readLine(); 
               // do the operation when client receive the recommendation
	           if(line.equals("path")) 
	           { 
	        	   MainActivity.condOfOverItem=3;
	        	   for(int i=0;i<18;i++){
	        		   dataArr[i]=MainActivity.in.readLine();
	        		   Log.d("RecDataThread", "Received: " + dataArr[i]);
	        		   
	        	   }
	        		//向activity发送handler消息
					Message pictureMsg = new Message();
					pictureMsg.what = MainActivity.FLASHMAP;
					myHandler.sendMessage(pictureMsg);
	               line = MainActivity.in.readLine(); //read the next line
	           }
	           
	           // do the operation when client send the confirmation

	           if(line.equals("bye")) 
	           { 
	        	  
	               MainActivity.out.close(); 
	               MainActivity.in.close(); 
	               MainActivity.socket.close(); 

	           }

		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	SendLock();
//			try {		
//		//向activity发送handler消息
//					Message pictureMsg = new Message();
//					pictureMsg.what = MainActivity.FLASHMAP;
//					myHandler.sendMessage(pictureMsg);			
//				
//				}							
//			catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	}


}
