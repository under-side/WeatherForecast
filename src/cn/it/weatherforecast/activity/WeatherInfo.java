package cn.it.weatherforecast.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.fragment.WeatherInfoBeforeFragment;

public class WeatherInfo extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_info);
		String selectCityId=getIntent().getStringExtra("select_city_id");
		FragmentManager fm=getFragmentManager();
		Fragment beforeFragment=fm.findFragmentById(R.id.layout_for_fragment_now);
		if(beforeFragment==null)
		{
			beforeFragment=new WeatherInfoBeforeFragment(this, selectCityId);
			fm.beginTransaction()
			.replace(R.id.layout_for_fragment_now, beforeFragment)
			.commit();
		}
	}
}
