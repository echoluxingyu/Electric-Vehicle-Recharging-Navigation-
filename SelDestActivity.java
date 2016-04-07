package introduction.Android.enavi;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;

import android.os.Bundle;


public class SelDestActivity extends com.baidu.mapapi.MapActivity {
	public BMapManager mapManager;
	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
	    setContentView(R.layout.activity_seldest); 
	    mapManager = new BMapManager(getApplication());  
        mapManager.init("E9A6A88227D65F39BA5E0BD67D13D5F19882F83A", null);  
	    super.initMapActivity(mapManager);  
	    MapView mapView = (MapView) findViewById(R.id.bmapsView);  
	    mapView.setBuiltInZoomControls(true);  //设置启用内置的缩放控件   
	    mapView.setTraffic(true); //开启实时交通信息
	    MapController mapController = mapView.getController();  // 得到mMapView的控制权,可以用它控制和驱动平移和缩放   
	    GeoPoint point = new GeoPoint((int) (116.3 * 1E6),(int) (39.5 * 1E6));  //用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)  
	    mapController.setCenter(point);  //设置地图中心点   
	    mapController.setZoom(12);    //设置地图zoom级别  
	    
	    

    }  
    @Override  
	protected void onDestroy() {  
    	if (mapManager != null) {  
    		mapManager.destroy();  
            mapManager = null;  
        }  
       super.onDestroy();  
    }  
    @Override  
    protected void onPause() {  
    	if (mapManager != null) {  
    		mapManager.stop();  
    	}  
       super.onPause();  
    }  
    @Override  
    protected void onResume() {  
    	if (mapManager != null) {  
    		mapManager.start();  
        }  
        super.onResume();  
     }  
	   
	@Override  
    protected boolean isRouteDisplayed() {  
		 return false;  
    }  

	    

}

