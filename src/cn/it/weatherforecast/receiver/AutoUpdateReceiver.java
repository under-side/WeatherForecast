package cn.it.weatherforecast.receiver;

import cn.it.weatherforecast.service.AutoUpdateWeatherService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Intent i = new Intent(arg0, AutoUpdateWeatherService.class);
		arg0.startService(i);
	}
}
