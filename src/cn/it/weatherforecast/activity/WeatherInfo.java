package cn.it.weatherforecast.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.fragment.WeatherInfoBeforeFragment;

public class WeatherInfo extends Activity implements OnGestureListener
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_info);
		String selectCityId=getIntent().getStringExtra("select_city_id");
		WeatherInfoBeforeFragment beforeFragment=new WeatherInfoBeforeFragment(this, selectCityId);
		FragmentManager fm=getFragmentManager();
		fm.beginTransaction()
		.replace(R.id.layout_for_fragment_now, beforeFragment)
		.commit();
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
