package com.example.enaviapp;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.baidu.mapapi.*;




public class OverItem extends ItemizedOverlay<OverlayItem> {

	private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	private Drawable marker;


	


	public OverItem(Drawable marker, Context context, int n) {
		super(boundCenterBottom(marker));
		this.marker = marker;
        if(MainActivity.condOfOverItem==1){
        	double lati=Double.parseDouble(MainActivity.latit); 
        	double longi=Double.parseDouble(MainActivity.longit);
        	GeoPoint p1 = new GeoPoint((int) (lati * 1E6), (int) (longi * 1E6));
        	 GeoPoint p=CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(p1));
        	mGeoList.add(new OverlayItem(p, "Location", "Your Location"));
           
        	populate();
        }
        
        if(MainActivity.condOfOverItem==2){
        	double lati=Double.parseDouble(MainActivity.destLati); 
        	double longi=Double.parseDouble(MainActivity.destLongi);
        	GeoPoint p1 = new GeoPoint((int) (lati * 1E6), (int) (longi * 1E6));
        	GeoPoint p=CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(p1));
        	mGeoList.add(new OverlayItem(p, "Destination", "Your Destination"));

        	populate();
        }
        
        if(MainActivity.condOfOverItem==3){
        	double[] longi =new double[3];
        	double[] lati =new double[3];
        	for(int i=0;i<3;i++){
        	    longi[i]=Double.parseDouble(RecDataThread.dataArr[5*i+3]);
        	    lati[i]=Double.parseDouble(RecDataThread.dataArr[5*i+4]);
        	}

			GeoPoint p1 = new GeoPoint((int) (lati[0] * 1E6), (int) (longi[0] * 1E6));
			GeoPoint pa=CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(p1));
		   // ����OverlayItem��������������Ϊ��item��λ�ã������ı�������Ƭ
			mGeoList.add(new OverlayItem(pa, "CS1", "Charge"));
			GeoPoint p2 = new GeoPoint((int) (lati[1] * 1E6), (int) (longi[1] * 1E6));
			GeoPoint pb=CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(p2));
		   // ����OverlayItem��������������Ϊ��item��λ�ã������ı�������Ƭ
			mGeoList.add(new OverlayItem(pb, "CS2", "Charge"));
			GeoPoint p3 = new GeoPoint((int) (lati[2] * 1E6), (int) (longi[2] * 1E6));
			GeoPoint pc=CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(p3));
		   // ����OverlayItem��������������Ϊ��item��λ�ã������ı�������Ƭ
			mGeoList.add(new OverlayItem(pc, "CS", "Charge"));

			populate(); //createItem(int)��������item��һ���������ݣ��ڵ�����������ǰ�����ȵ����������
        	
        }
        
//       // if(MainActivity.condOfOverItem==4){
//
//        	double longi2=Double.parseDouble(RecDataThread.dataArr[8]);
//        	double lati2=Double.parseDouble(RecDataThread.dataArr[9]);
//        	//double longi3=Double.parseDouble(RecDataThread.dataArr[13]);
//        	//double lati3=Double.parseDouble(RecDataThread.dataArr[14]);
//
//			GeoPoint p2 = new GeoPoint((int)  (lati2 * 1E6), (int) (longi2 * 1E6));
//			//GeoPoint p3 = new GeoPoint((int) (lati3 * 1E6), (int) (longi3 * 1E6));
//
//			 GeoPoint pb=CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(p2));
//			 //GeoPoint pc=CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(p3));
//		   // ����OverlayItem��������������Ϊ��item��λ�ã������ı�������Ƭ��
//
//			mGeoList.add(new OverlayItem(pb, "CS2", "Charge2"));
//			//mGeoList.add(new OverlayItem(pc, "CS3", "Charge3"));	
//			populate(); //createItem(int)��������item��һ���������ݣ��ڵ�����������ǰ�����ȵ����������
//        }
//        if(MainActivity.condOfOverItem==5){
//
//
//        	double longi3=Double.parseDouble(RecDataThread.dataArr[13]);
//        	double lati3=Double.parseDouble(RecDataThread.dataArr[14]);
//
//		     GeoPoint p3 = new GeoPoint((int) (lati3 * 1E6), (int) (longi3 * 1E6));
//
//			 GeoPoint pc=CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(p3));
//		   // ����OverlayItem��������������Ϊ��item��λ�ã������ı�������Ƭ��
//
//			mGeoList.add(new OverlayItem(pc, "CS3", "Charge3"));	
//			populate(); //createItem(int)��������item��һ���������ݣ��ڵ�����������ǰ�����ȵ����������
//			
//        }

				
	}

	
	//������δ��벻��Ҫ���Ķ�
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		// Projection�ӿ�������Ļ��������;�γ������֮��ı任
		Projection projection = mapView.getProjection(); 
		for (int index = size() - 1; index >= 0; index--) { // ����mGeoList
			OverlayItem overLayItem = getItem(index); // �õ�����������item

			String title = overLayItem.getTitle();
			// �Ѿ�γ�ȱ任�������MapView���Ͻǵ���Ļ��������
			Point point = projection.toPixels(overLayItem.getPoint(), null); 

			// ���ڴ˴�������Ļ��ƴ���
			Paint paintText = new Paint();
			paintText.setColor(Color.BLUE);
			paintText.setTextSize(15);
			canvas.drawText(title, point.x-30, point.y+10, paintText); // �����ı�
		}

		super.draw(canvas, mapView, shadow);
		//����һ��drawable�߽磬ʹ�ã�0��0�������drawable�ײ����һ�����ĵ�һ������
		boundCenterBottom(marker);
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mGeoList.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mGeoList.size();
	}
	@Override
	// ��������¼�������Ĵ��봦��������ע�ĵ��Ժ󵯳�����
	protected boolean onTap(int i) {
		if(MainActivity.condOfOverItem==3){
		final int num=i;
		
		setFocus(mGeoList.get(num));
		// ��������λ��,��ʹ֮��ʾ
		MainActivity.selectCS=RecDataThread.dataArr[5*num];
        GeoPoint pt = mGeoList.get(num).getPoint();
		MainActivity.mMapView.updateViewLayout( MainActivity.mPopView,new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,pt, MapView.LayoutParams.BOTTOM_CENTER));
		MainActivity.mPopView.setVisibility(View.VISIBLE); 
		MainActivity.mPopView.setOnClickListener(new View.OnClickListener(){	    	
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				SIPSActivity.gpsFlash=1;
				MainActivity.mPopView.setVisibility(View.GONE); 					
				Double time=Double.parseDouble(RecDataThread.dataArr[15+num])/1000;
				DecimalFormat    df   = new DecimalFormat("######0.00");   
				String timeT=df.format(time); 
				String illuMes=timeT+" hours expected"+"\n"+RecDataThread.dataArr[num*5+1]+"vehicles in queue"+"\n"+RecDataThread.dataArr[num*5+2]+"machines"+"\n";		
				MainActivity.te.setText(illuMes);
				MainActivity.outMes="confirm"+"\n"+RecDataThread.dataArr[num*5]+"\n"+RecDataThread.dataArr[num*5+1]+"\n"+MainActivity.cliIP+"\n";		
			}	        	
        });
		}
		
		return true;
		
	}

	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		// TODO Auto-generated method stub
		// ��ȥ����������
		if (MainActivity.mPopView != null){
		MainActivity.mPopView.setVisibility(View.GONE); 
		}
		return super.onTap(arg0, arg1);
		
		
	}
}

