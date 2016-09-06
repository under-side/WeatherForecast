package cn.it.weatherforecast.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import cn.it.weatherforecast.db.WeatherForecastDB;
import cn.it.weatherforecast.util.HttpCallbackListenerForJson;
import cn.it.weatherforecast.util.HttpUtilForDowloadJson;
import cn.it.weatherforecast.util.MyApplication;
import cn.it.weatherforecast.util.Utility;

public class DownAreasService extends Service {

	// 获取城市信息的URL
	private static final String URL = "https://api.heweather.com/x3/citylist?search=allchina&key=e880b41d75d840d7aaaad18356139993";
	private WeatherForecastDB mDB;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		mDB = MyApplication.getWeatherForecastDB();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				downloadAreas();

				stopSelf();

			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}

	// 运用封装的HTTP服务，从网上获取城市的信息
	private void downloadAreas() {
		// TODO Auto-generated method stub
		HttpUtilForDowloadJson.getJsonFromHttp(URL,
				new HttpCallbackListenerForJson() {

					@Override
					public void onFinish(String response) {
						// TODO Auto-generated method stub
						Utility.handleJsonForDBAreas(mDB, response);
					}

					@Override
					public void onError(Exception e) {
						// TODO Auto-generated method stub
						e.printStackTrace();
					}
				});
	}
}
