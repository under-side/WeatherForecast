package cn.it.weatherforecast.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.activity.SelectAreasActivity;
import cn.it.weatherforecast.util.MyApplication;

public class EmptyWeatherInfoFragment extends Fragment {
	
	private Button mSwitchToArea;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_for_empty_weather, container,false);
		mSwitchToArea=(Button) view.findViewById(R.id.empty_switch_button);
		mSwitchToArea.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MyApplication.getContext(),SelectAreasActivity.class);
				startActivity(i);
			}
		});
		return view;
	}

}
