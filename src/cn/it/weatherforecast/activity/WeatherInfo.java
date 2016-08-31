package cn.it.weatherforecast.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.fragment.WeatherInfoAfterFragment;
import cn.it.weatherforecast.fragment.WeatherInfoBeforeFragment;
import cn.it.weatherforecast.fragment.WeatherInfoScrollFragment;
import cn.it.weatherforecast.util.HttpCallbackListenerForJson;
import cn.it.weatherforecast.util.HttpUtilForDowloadJson;
import cn.it.weatherforecast.util.Utility;

public class WeatherInfo extends Activity implements
		android.view.GestureDetector.OnGestureListener {

	private Button mSelectAreasButton;
	private FragmentManager fm;
	private Fragment beforeFragment;
	private Fragment afterFragment;
	private Fragment scrollFragment;
	private String mSelectCityId;
	// 定义手势检测器实例
	GestureDetector detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_info);
		mSelectCityId = getIntent().getStringExtra("select_city_id");
		//测试模块 
		{
			String url=
					"https://api.heweather.com/x3/weather?cityid=CN101310215&key=e880b41d75d840d7aaaad18356139993";
			HttpUtilForDowloadJson.sendHttpRequest(url, new HttpCallbackListenerForJson() {
				
				@Override
				public void onFinish(String response) {
					// TODO Auto-generated method stub
					SharedPreferences data=getSharedPreferences("CN101310215", Context.MODE_PRIVATE);
					Utility.handleWeatherInfoResponseByJSON(data, WeatherInfo.this, response);
				}
				
				@Override
				public void onError(Exception e) {
					// TODO Auto-generated method stub
				e.printStackTrace();	
				}
			});
		}
		// 动态的装载Fragment
		fm = getFragmentManager();
		beforeFragment = fm.findFragmentById(R.id.layout_for_fragment_now);
		afterFragment = fm.findFragmentById(R.id.layout_for_fragment_now);
        scrollFragment=fm.findFragmentById(R.id.layout_for_fragment_scroll);
		if (beforeFragment == null) {
			beforeFragment = new WeatherInfoBeforeFragment(this, mSelectCityId);
			fm.beginTransaction()
					.add(R.id.layout_for_fragment_now, beforeFragment).commit();
		}

		if (afterFragment == null) {
			afterFragment = new WeatherInfoAfterFragment(WeatherInfo.this,
					mSelectCityId);
		}
		if(scrollFragment==null)
		{
			scrollFragment=new WeatherInfoScrollFragment(this, mSelectCityId);
			fm.beginTransaction().replace(R.id.layout_for_fragment_scroll, scrollFragment).commit();
		}

		mSelectAreasButton = (Button) findViewById(R.id.switch_city);
		mSelectAreasButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WeatherInfo.this,
						ChooseAreasActivity.class);
				startActivity(intent);
			}
		});

		// 创建手势检测器
		detector = new GestureDetector(this, this);
	}

	// 将该activity上的触碰事件交给GestureDetector处理
	public boolean onTouchEvent(MotionEvent me) {
		return detector.onTouchEvent(me);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 滑屏监测
	 * 
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float minMove = 100; // 最小滑动距离
		float minVelocity = 0; // 最小滑动速度

		// // 获取手势事件中X轴中的起始位置
		// float beginX = e1.getX();
		// float endX = e2.getX();

		// 获取手势事件中Y轴中的起始位置变化
		float beginY = e1.getY();
		float endY = e2.getY();

		// 根据手势事件中X-Y轴变化，来做逻辑操作
		if ((beginY - endY) > minMove && Math.abs(velocityY) > minVelocity)// up
		{
			if (fm.findFragmentById(R.id.layout_for_fragment_now).equals(
					beforeFragment)) {

				fm.beginTransaction().remove(beforeFragment)
						.add(R.id.layout_for_fragment_now, afterFragment)
						.commit();
			}
			Toast.makeText(this, velocityX + "上滑", Toast.LENGTH_SHORT).show();
		} else if ((endY - beginY) > minMove
				&& Math.abs(velocityY) > minVelocity)// down
		{
			if (fm.findFragmentById(R.id.layout_for_fragment_now).equals(
					afterFragment)) {

				fm.beginTransaction().remove(afterFragment)
						.add(R.id.layout_for_fragment_now, beforeFragment)
						.commit();
			}

			Toast.makeText(this, velocityX + "下滑", Toast.LENGTH_SHORT).show();
		}
		return true;
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
