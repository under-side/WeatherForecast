package cn.it.weatherforecast.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.it.weatherforecast.R;
public class WeatherInfoScrollFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view =inflater.inflate(R.layout.fragment_weather_scroll, container);
		initComponent(view);
		return view;
	}

	private void initComponent(View view) {
		// TODO Auto-generated method stub
		
	}
}
