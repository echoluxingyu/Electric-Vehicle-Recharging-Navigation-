package com.example.enaviapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKOLUpdateElement;
import com.baidu.mapapi.MKOfflineMap;
import com.baidu.mapapi.MKOfflineMapListener;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.example.enaviapp.BMapApiDemoApp;
import com.example.enaviapp.OverItem;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.content.Context;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends MapActivity {
     static String velo;  // to store the velocity of the client
     static String batLev; // to store the battery level of the client
     static String longit="116.4"; //the longitude of the client's current location 
     static String latit="40.1"; // the latitude of the client's current location
 	 static String cliIP,outMes; //IP of the client and the message sent to the Server
 	 
     public static View mPopView;// pop-view when the marker is clicked 
     public static MyMapView mMapView;
     BMapApiDemoApp app;
     private TextView mText;
     public static final int FLASHMAP = 1; //unlock
     private MKOfflineMap mOffline;    //off-line maps
 	 public static ActivityHandler activityHandler;
 	 private RecDataThread recDataThread; //thread to wait for the incoming data

	 int cliPort; // port of the client for communication
	 static String destLongi,destLati; //the longitude and latitude of the destination
	 static BufferedReader in;
	 static PrintWriter out;
	 static Socket socket;
	 
	 static int condOfOverItem;  //use the flag as identifier to know which part of the process is coming
	 static String selectCS;
	 
     static TextView te;     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // link the class to the related interface
        
        
        //Handler to receive coming info
        Looper curLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
        if(curLooper==null){
        	activityHandler = new ActivityHandler(mainLooper);
        }
        else{
        	activityHandler = new ActivityHandler(curLooper);
        }
        
        //initialize the components in the interface which need further action
        final EditText et1=(EditText)this.findViewById(R.id.et1); 
        final EditText et2=(EditText)this.findViewById(R.id.et2); 
        final Button bt1=(Button)this.findViewById(R.id.sendbt);
        final Button bt2=(Button)this.findViewById(R.id.selDes);
        final Button bt3=(Button)this.findViewById(R.id.confirm);
        final Button bt4=(Button)this.findViewById(R.id.drop);
        final TextView tv1=(TextView)this.findViewById(R.id.tv1);
        final TextView tv2=(TextView)this.findViewById(R.id.tv2);
        te=(TextView)this.findViewById(R.id.text); 
        
        
        // require for the location service
       LocationManager locMana=(LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        locMana.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new MyLocationListener());
        
      
        // listener for the "Send" button, send query to the Server
        bt1.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		// TODO Auto-generated method stub
        		try{
                       
		               socket = new Socket("192.168.1.127", 20000); //fixed IP and fixed port number
		       		   cliPort= socket.getLocalPort(); //get the port number of the client 
		       		   InetAddress localAddress =socket.getLocalAddress(); 
		       		   cliIP=localAddress.getHostAddress(); //get the IP of the client

		               in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
		               out = new PrintWriter(socket.getOutputStream(),true); 
		               String cliP= Integer.toString(cliPort);

		               recDataThread = new RecDataThread();
		               recDataThread.registerHandler(activityHandler);//register the handler
		               
                       // produce the query of the client
		           	   outMes= "query"+"\n"+cliIP+"\n"+cliP+"\n"+velo+"\n"+batLev+"\n"+longit+"\n"+latit+"\n"+destLongi+"\n"+destLati+"\n" ;
		               out.println(outMes); 
		               
		    	}
		       catch (IOException e){
		       }
			}

        	});
        // listener for the "Select Destination" button, select destination on the maps
        bt2.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		// TODO Auto-generated method stub
        		condOfOverItem=2;
        		mMapView.setClickable(true);
                Drawable marker = getResources().getDrawable(R.drawable.dest);
                mMapView.getOverlays().add(new OverItem(marker, getApplicationContext(),0));
                tv1.setText("Dest Longi:"+destLongi+"    ");
                tv2.setText("Dest Lati:"+destLati);
			}

        	});
        
        bt3.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		// TODO Auto-generated method stub
	           // mMapView.getOverlays().clear();
	            out.println(outMes); 
	            
        
			}

        	});
        
        bt4.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		// TODO Auto-generated method stub
        		 mMapView.getOverlays().clear();
	           	outMes= "bye"+"\n"+cliIP+"\n" ;
	            out.println(outMes); 
	            
        
			}

        	});
        
        //listener for the editText
        et1.addTextChangedListener(new TextWatcher(){
        	@Override
        	public void afterTextChanged(Editable s){
        		// TODO Auto-generated method stub
        	}
        	@Override
        	public void beforeTextChanged(CharSequence s,int start,int count,int after){
        		//TODO Auto-generated method stub
        	}
        	@Override
        	public void onTextChanged(CharSequence s,int start,int before,int count){
        		//TODO Auto-generated method stub
        		 batLev=et1.getText().toString();
        	}        	
        });
        
        //listener for the editText
        et2.addTextChangedListener(new TextWatcher(){
        	@Override
        	public void afterTextChanged(Editable s){
        		// TODO Auto-generated method stub
        	}
        	@Override
        	public void beforeTextChanged(CharSequence s,int start,int count,int after){
        		//TODO Auto-generated method stub
        	}
        	@Override
        	public void onTextChanged(CharSequence s,int start,int before,int count){
        		//TODO Auto-generated method stub
        		 velo=et2.getText().toString();
        	}        	
        });
        initBaiduMap();
    }

    ///////////////////////////////initialization of Baidu maps///////////////////////////////
    public void initBaiduMap(){
    	app = (BMapApiDemoApp)this.getApplication();
		if (app.mBMapMan == null)
		{
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapApiDemoApp.MyGeneralListener());
		}
		app.mBMapMan.start();
		
        // initialize the Activity
        super.initMapActivity(app.mBMapMan);
        
        mMapView = (MyMapView)findViewById(R.id.bmapView);
        mMapView.setClickable(false);
        mMapView.setBuiltInZoomControls(true);
        // keep the illustration of overlay when zooming
        mMapView.setDrawOverlayWhenZooming(true);
        

        
        // loading off-line maps
        mOffline = new MKOfflineMap();
        mOffline.init(app.mBMapMan,new MKOfflineMapListener(){
        @Override
        	public void onGetOfflineMapState(int type, int state) {
       			switch (type) {
       			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
    			{
    				Log.d("OfflineDemo", String.format("cityid:%d update", state));
    				MKOLUpdateElement update = mOffline.getUpdateInfo(state);
    				mText.setText(String.format("%s : %d%%", update.cityName, update.ratio));
    			}
    				break;
       			case MKOfflineMap.TYPE_NEW_OFFLINE:
       				Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
       				break;
       			case MKOfflineMap.TYPE_VER_UPDATE:
       				Log.d("OfflineDemo", String.format("new offlinemap ver"));
       				break;
        			}
        		}
        });
        int num = mOffline.scan();
		if (num != 0)
		Log.d("OfflineDemo", String.format("scan offlinemap num:%d", num));
		
		// initialize the center 
		GeoPoint point =new GeoPoint((int)(39.915*1e6),(int)(116.404*1e6));
        mMapView.getController().setCenter(point);
        mMapView.getController().setZoom(12);
        

    
        // get the resource to mark on the map 
        condOfOverItem=1;
        Drawable marker = getResources().getDrawable(R.drawable.o2);
        mMapView.getOverlays().add(new OverItem(marker, getApplicationContext(),0));
		//marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());   //为maker定义位置和边界
		


    }
    
    //deal with the incoming message from the recDataThread
	public class ActivityHandler extends Handler{
        public ActivityHandler(Looper looper){
               super(looper);
        }
        public void handleMessage(Message msg) {
        	switch(msg.what){
        	    case FLASHMAP: //information received
        		ArrayList<Drawable> recMarker = new ArrayList<Drawable>();
        		recMarker.add(getResources().getDrawable(R.drawable.o1));  
        		//recMarker.add(getResources().getDrawable(R.drawable.cs2));
        		//recMarker.add(getResources().getDrawable(R.drawable.cs3));
	            mMapView.getOverlays().clear();
	           // for(int i=0;i<3;i++){
   				    mMapView.getOverlays().add(new OverItem(recMarker.get(0), getApplicationContext(),0)); //add ItemizedOverlay object to mMapView
   					// built pop-view when the marker is clicked 
   					mPopView=getLayoutInflater().inflate(R.layout.popview, null);
   					mMapView.addView(mPopView,new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,null, MapView.LayoutParams.TOP_LEFT));
   					mPopView.setVisibility(View.GONE);   	
        		
	            //}
	            break;
        	}
        	 
        }
	}
//    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
    
    //to get the current location
    class MyLocationListener implements LocationListener{
    	 @Override
    	 public void onLocationChanged(Location location){
    		 // TODO Auto_generated method stub
    		  Double longi=location.getLongitude();
    		  Double lati=location.getLatitude();
    		  longit=longi.toString();
    		  latit=lati.toString();
    	 }
    	 @Override
    	 public void onProviderDisabled(String provider){
    		 //when the provider is closed by the user
    		 Log.i("GPSLocation","provider is closed!");
         }
    	 public void onStatusChanged(String provider, int status,Bundle extras){
    		 //when the provider's status is changed between OUT_OF_SERVICE,TEMPORARILY_UNAVAILABLE and AVALAIBLE
    		 Log.i("GPSLocation","the status of provider is changed!");
    	 }
		@Override
		public void onProviderEnabled(String provider) {
			// when the provider is open
			Log.i("GPSLocation","provider is open!");
	   }
     }
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
