package com.example.enaviapp;

import android.app.Application;
import android.widget.Toast;
import com.baidu.mapapi.*;

public class BMapApiDemoApp extends Application {
	
	static BMapApiDemoApp mDemoApp;
	
	//the manager for MapAPI
	BMapManager mBMapMan = null;
	
	// 授权Key
	String mStrKey = "E9A6A88227D65F39BA5E0BD67D13D5F19882F83A";
	boolean m_bKeyRight = true;	// 授权Key正确，验证通过
	
	//general listener for the authentication issues and network error
	static class MyGeneralListener implements MKGeneralListener {
		public void onGetNetworkState(int iError) {

		}
		public void onGetPermissionState(int iError) {
			if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
				// wrong Baidu API key
				Toast.makeText(BMapApiDemoApp.mDemoApp.getApplicationContext(), 
						"please type in valid Key in BMapApiDemoApp.java!",
						Toast.LENGTH_LONG).show();
				BMapApiDemoApp.mDemoApp.m_bKeyRight = false;
			}
		}
		
	}
	
	@Override
    public void onCreate() {
		mDemoApp = this;
		mBMapMan = new BMapManager(this);
		mBMapMan.init(this.mStrKey, new MyGeneralListener());
		super.onCreate();
	}
	@Override
	//invoke destroy() when the app is terminated to avoid the the consumption of time on initialization 
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}

}
