package cn.it.weatherforecast.receiver;

import cn.it.weatherforecast.service.AutoUpdateWeatherService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/*
 * 此类为后台建立的长期更新服务所建立的广播，用于提醒后台更新服务
 */
public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Intent i = new Intent(arg0, AutoUpdateWeatherService.class);
		arg0.startService(i);
	}
}
