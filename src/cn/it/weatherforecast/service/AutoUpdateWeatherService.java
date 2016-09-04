package cn.it.weatherforecast.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import cn.it.weatherforecast.receiver.AutoUpdateReceiver;
import cn.it.weatherforecast.util.HttpUtilForDowloadJson;
import cn.it.weatherforecast.util.MyApplication;

public class AutoUpdateWeatherService extends Service {

	private String mSelectId;
	private String mSelectCityName;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateWeather();
			}
		}).start();
		//设置了八个小时的闹钟提醒，每个八个小时将会执行一次广播
		AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour=8*60*60*1000;
	    long triggerAtTime=SystemClock.elapsedRealtime()+anHour;
	    Intent i=new Intent(this,AutoUpdateReceiver.class);
	    PendingIntent pendingIntent=PendingIntent.getBroadcast(this, 0, i, 0);
	    manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
		return super.onStartCommand(intent, flags, startId);
	}
	protected void updateWeather() {
		HttpUtilForDowloadJson.getWeatherInfoFromHttp
		(mSelectCityName,mSelectId, MyApplication.getContext());
	}
}
