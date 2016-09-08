package cn.it.weatherforecast.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.it.weatherforecast.R;

public class WeatherInfoFragment extends Fragment {

	private FragmentManager fragmentManager;
	private WeatherInfoBeforeFragment mWeatherBeforeFragment;
	private WeatherInfoScrollFragment mWeatherScrollFragment;

	private Context mContext;
	private String mSelectCityId;
	private SharedPreferences data;

	// 自定义构造函数，从托管Activity中获取必要数据
	public WeatherInfoFragment(Context context, String selectCityId) {
		mContext = context;
		mSelectCityId = selectCityId;
	}

	// 在fragment绑定activity时，执行将另外的几个fragment嵌套在该fragment中
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		// fragment嵌套fragment时，需要调用getChildFragmentManager方法获取FragmentManager，执行添加操作
		fragmentManager = getChildFragmentManager();
		data = mContext.getSharedPreferences(mSelectCityId,
				Context.MODE_PRIVATE);
		addOperationToFragment();

	}

	// 将其他fragment嵌套在该fragment中
	private void addOperationToFragment() {
		mWeatherBeforeFragment = (WeatherInfoBeforeFragment) fragmentManager
				.findFragmentById(R.id.layout_for_fragment_now);
		mWeatherScrollFragment = (WeatherInfoScrollFragment) fragmentManager
				.findFragmentById(R.id.layout_for_fragment_scroll);

		if (mWeatherBeforeFragment == null) {
			mWeatherBeforeFragment = new WeatherInfoBeforeFragment(data);
			fragmentManager.beginTransaction()
					.add(R.id.layout_for_fragment_now, mWeatherBeforeFragment)
					.commit();
		}

		if (mWeatherScrollFragment == null) {
			mWeatherScrollFragment = new WeatherInfoScrollFragment(data);
			fragmentManager
					.beginTransaction()
					.replace(R.id.layout_for_fragment_scroll,
							mWeatherScrollFragment).commit();
		}
	}

	// 创建视图
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_weather_info, container,
				false);
		return view;
	}

	//根据获得的新的SharedPreference数据，来更新UI界面
	public void updateView(SharedPreferences data) {
		mWeatherBeforeFragment.showDataView(data);
		mWeatherScrollFragment.showViewFromData(data);
	}
}
