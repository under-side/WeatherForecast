package cn.it.weatherforecast.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.it.weatherforecast.R;

public class WeatherInfoFragment extends Fragment{
	
	private FragmentManager fragmentManager;
	private Fragment mWeatherBeforeFragment;
	private Fragment mWeatherAfterFragment;
	private Fragment mWeatherScrollFragment;
	
	private Context mContext;
	private String mSelectCityId;
	SharedPreferences data;
	// 定义手势检测器实例
    GestureDetector detector;
	//自定义构造函数，从托管Activity中获取必要数据
		public WeatherInfoFragment(Context context, String selectCityId) {
			mContext = context;
			mSelectCityId = selectCityId;
		}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		fragmentManager=getChildFragmentManager();
		data=mContext.getSharedPreferences(mSelectCityId, Context.MODE_PRIVATE);
        addOperationToFragment();
        
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_weather_info, container,false);
		return view;
	}
	
	private void addOperationToFragment()
	{
	mWeatherBeforeFragment = fragmentManager.findFragmentById(R.id.layout_for_fragment_now);
	mWeatherAfterFragment = fragmentManager.findFragmentById(R.id.layout_for_fragment_now);
	mWeatherScrollFragment = fragmentManager.findFragmentById(R.id.layout_for_fragment_scroll);
	
	if (mWeatherBeforeFragment == null) {
		mWeatherBeforeFragment = new WeatherInfoBeforeFragment(data);
		fragmentManager.beginTransaction()
				.add(R.id.layout_for_fragment_now, mWeatherBeforeFragment).commit();
	}

	if (mWeatherAfterFragment == null) {
		mWeatherAfterFragment = new WeatherInfoAfterFragment(data);
	}
	if (mWeatherScrollFragment == null) {
		mWeatherScrollFragment = new WeatherInfoScrollFragment(data);
		fragmentManager.beginTransaction()
				.replace(R.id.layout_for_fragment_scroll, mWeatherScrollFragment)
				.commit();
	}
	}
}
