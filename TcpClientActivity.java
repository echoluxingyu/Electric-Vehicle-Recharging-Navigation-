package introduction.Android.enavi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class TcpClientActivity extends Activity{
	String cliIP,outMes; //IP of the client and the message sent to the Server
	int cliPort; // port of the client for communication
	Double destLongi,destLati; //the longitude and latitude of the destination


	@Override
	protected void onCreate(Bundle savedInstanceState){
		//TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_tcpclient); //link the class to the related interface 
        
		//initialize the components in the interface which need further action
		final TextView tv1=(TextView) findViewById(R.id.clTx1);
		final TextView tv2=(TextView) findViewById(R.id.clTx2);
		final TextView tv3=(TextView) findViewById(R.id.clTx3);
		final TextView tv4=(TextView) findViewById(R.id.clTx4);
		final Button bt1=(Button) findViewById(R.id.clBtn1);
		Button bt2=(Button) findViewById(R.id.clBtn2);
		
		// the listener for the "Confirm" button, get, organize and display the needed client information  
		bt1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//TODO Auto-generated method stub

				tv1.setText("Velocity:"+MainActivity.velo); 
				tv2.setText("Battery:"+MainActivity.batLev); 
				tv3.setText("Location:"+MainActivity.longit+" "+MainActivity.latit);
				tv4.setText("Destination:"+destLongi+""+destLati); //由百度 maps API获取
				bt1.setEnabled(false);
			}

		});
		
		//the listener for the "Send" button, connect to the client and send the query
		bt2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//TODO Auto-generated method stub
				try{

		               Socket socket = new Socket("xxx.xxx.xxx.xxx", 10000); //fixed IP and fixed port number
		       		   cliPort= socket.getLocalPort(); //get the port number of the client 
		       		   InetAddress localAddress =socket.getLocalAddress(); 
		       		   cliIP=localAddress.getHostAddress(); //get the IP of the client

		               BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //输入应该为client IP， port，velocity，destination
		               PrintWriter out = new PrintWriter(socket.getOutputStream(),true); 
		               String cliP= Integer.toString(cliPort);
		               String deLongi=Double.toString(destLongi); 
		               String deLati=Double.toString(destLati); 
                       // produce the query of the client
		           	   outMes= "query"+"/n"+cliIP+"/n"+cliP+"/n"+MainActivity.velo+"/n"+MainActivity.batLev+"/n"+MainActivity.longit+"/n"+MainActivity.latit+"/n"+deLongi+"/n"+deLati+"/n" ;
		               out.println(outMes); 
		               
		               out.close(); 
		               in.close(); 
		               socket.close(); 
		    	}
		       catch (IOException e){
		       }
			}

		});
	}

}
