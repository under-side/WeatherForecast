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

	// �Զ��幹�캯�������й�Activity�л�ȡ��Ҫ����
	public WeatherInfoFragment(Context context, String selectCityId) {
		mContext = context;
		mSelectCityId = selectCityId;
	}

	// ��fragment��activityʱ��ִ�н�����ļ���fragmentǶ���ڸ�fragment��
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		// fragmentǶ��fragmentʱ����Ҫ����getChildFragmentManager������ȡFragmentManager��ִ����Ӳ���
		fragmentManager = getChildFragmentManager();
		data = mContext.getSharedPreferences(mSelectCityId,
				Context.MODE_PRIVATE);
		addOperationToFragment();

	}

	// ������fragmentǶ���ڸ�fragment��
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

	// ������ͼ
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_weather_info, container,
				false);
		return view;
	}

	//���ݻ�õ��µ�SharedPreference���ݣ�������UI����
	public void updateView(SharedPreferences data) {
		mWeatherBeforeFragment.showDataView(data);
		mWeatherScrollFragment.showViewFromData(data);
	}
}
