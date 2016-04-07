package com.example.enaviapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapView;

public class MyMapView extends MapView {  
	  
     public MyMapView(Context context){
    	  super(context);
     }
     public MyMapView(Context context, AttributeSet attrs){
          super( context, attrs);
     }
    public MyMapView(Context context,AttributeSet attrs,int defStyle){
	      super( context, attrs,defStyle);
    }  
      
    @Override  
    public boolean onTouchEvent(MotionEvent arg0) {  
        // TODO Auto-generated method stub  
        int x = (int)arg0.getX();  
        int y = (int)arg0.getY();  
        GeoPoint geoPoint = this.getProjection().fromPixels(x, y);  
        double xx = geoPoint.getLongitudeE6();  
        double yy = geoPoint.getLatitudeE6();  
        MainActivity.destLongi=Double.toString(xx / 1E6 ); 
        MainActivity.destLati=Double.toString(yy / 1E6) ;  
        return super.onTouchEvent(arg0);  
    }  
} 
