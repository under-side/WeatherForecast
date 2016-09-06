package cn.it.weatherforecast.service;

import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import cn.it.weatherforecast.receiver.AutoUpdateReceiver;
import cn.it.weatherforecast.util.HttpCallbackListenerForJson;
import cn.it.weatherforecast.util.HttpUtilForDowloadJson;
import cn.it.weatherforecast.util.MyApplication;
import cn.it.weatherforecast.util.Utility;

public class AutoUpdateWeatherService extends Service {

	private String mSelectId;
	//���d�����Ϣ��URL��ַ
	private static final String url = "https://api.heweather.com/x3/weather?cityid=";
	private static final String key = "&key=e880b41d75d840d7aaaad18356139993";
	
	SharedPreferences data;


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		mSelectId=intent.getStringExtra("StartAutoService");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateWeather();
			}
		}).start();
		// �����˰˸�Сʱ���������ѣ�ÿ���˸�Сʱ����ִ��һ�ι㲥
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 8 * 60 * 60 * 1000;
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent i = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime,
				pendingIntent);
		return super.onStartCommand(intent, flags, startId);
	}

	protected void updateWeather() {
		//һ�����и������������t����ǰ�ĸ��r�gͬ����SQLite��ԓCode��
				MyApplication.getWeatherForecastDB()
				.updateSelectedAreasTime(mSelectId,new Date().getTime());
				// ���ˢ�º��������ص�ǰ�ĳ������ݣ���ȥ���»��ƽ���
				data = getSharedPreferences(mSelectId, Context.MODE_PRIVATE);
				// ��ɾ���ļ��е�����
				data.edit().clear().commit();
				// �M�b���dָ������URL·��
				String weatherUrl = url
						+ mSelectId + key;
				//�_��һ�����̣��������dָ�����е������Ϣ
				HttpUtilForDowloadJson.getJsonFromHttp(weatherUrl,
						new HttpCallbackListenerForJson() {

							@Override
							public void onFinish(String response) {
								// TODO Auto-generated method stub
								Utility.handleWeatherInfoResponseByJSON(data,
										response);
							}
							@Override
							public void onError(Exception e) {
								// TODO Auto-generated method stub
								e.printStackTrace();
							}
						});
	}
}
