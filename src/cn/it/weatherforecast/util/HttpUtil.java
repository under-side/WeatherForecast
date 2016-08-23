package cn.it.weatherforecast.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/*
 * 该类封了运用HttpURLConnection来获取网络数据
 */
public class HttpUtil {

	public static void sendHttpRequest(final String address,final HttpCallbaceListener listener)
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection=null;
				try {
					URL url=new URL(address);
					try {
						connection=(HttpURLConnection) url.openConnection();
						connection.setRequestMethod("GET");
						connection.setReadTimeout(5000);
						connection.setConnectTimeout(5000);
						
						InputStream input=connection.getInputStream();
						InputStreamReader reader=new InputStreamReader(input);
						BufferedReader bufferReader=new BufferedReader(reader);
                       
						String line=null;
						StringBuilder respond=new StringBuilder();
						while((line=bufferReader.readLine())!=null)
						{
							respond.append(line);
						}
						if(listener!=null)
						{
							listener.onFinish(respond.toString());
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if(listener!=null)
						{
							listener.onError(e);
						}
					}
					finally{
						if(connection!=null)
						{
							connection.disconnect();
						}
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
	}
}
