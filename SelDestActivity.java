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
	    mapView.setBuiltInZoomControls(true);  //�����������õ����ſؼ�   
	    mapView.setTraffic(true); //����ʵʱ��ͨ��Ϣ
	    MapController mapController = mapView.getController();  // �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����   
	    GeoPoint point = new GeoPoint((int) (116.3 * 1E6),(int) (39.5 * 1E6));  //�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)  
	    mapController.setCenter(point);  //���õ�ͼ���ĵ�   
	    mapController.setZoom(12);    //���õ�ͼzoom����  
	    
	    

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

