package cn.it.weatherforecast.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.fragment.adapter.AdapterForHourlyInfo;
import cn.it.weatherforecast.model.AdapterHourlyInfoModel;
import cn.it.weatherforecast.util.Utility;

public class WeatherInfoBeforeFragment extends Fragment {

	private TextView mCurrentDate;
	private TextView mUpdateTime;
	private TextView mCityName;
	private TextView mWeatherTxt;
	private ImageView mWeatherImage;
	private TextView mWeatherTemperture;
	private TextView mWeekTxt;
	private TextView mTopTemperture, mLowTemperture;
	private GridView mHorizontalGridViewForHourly;

	private static final String mPhtotURL = "http://files.heweather.com/cond_icon/";

	private String mSelectCityId;
	private Context mContext;

	//自定义构造函数，从托管Activity中获取必要数据
	public WeatherInfoBeforeFragment(Context context, String selectCityId) {
		mContext = context;
		mSelectCityId = selectCityId;
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_weatherinfo_before, container,false);

		initComponent(view);

		SharedPreferences data = mContext.getSharedPreferences("CN101310215",
				Context.MODE_PRIVATE);
		// SharedPreferences data = mContext.getSharedPreferences(mSelectCityId,
		// Context.MODE_PRIVATE);
		showDataView(data);

		return view;
	}

	private void showDataView(SharedPreferences data) {
		// TODO Auto-generated method stub
		mCurrentDate.setText(data.getString("daily_date0", ""));
		mUpdateTime.setText("更新于    "
				+ Utility.splitDateString(data.getString("basic_loc", ""), 1));
		mCityName.setText(data.getString("basic_city", ""));
		mWeatherTxt.setText(data.getString("now_txt", ""));
		mWeatherTemperture.setText(data.getString("now_tmp", "") + "°");
		mWeekTxt.setText(Utility.getWeekFromDate(Utility.splitDateString(
				data.getString("basic_loc", ""), 0)));
		mTopTemperture.setText(data.getString("daily_max0", "") + "°");
		mLowTemperture.setText(data.getString("daily_min0", "") + "°");

		final String url = mPhtotURL + data.getString("now_code", "") + ".png";
		Utility.setImageViewFromHttp(url, mWeatherImage);

		// 对ListView进行操作逻辑编写
		addOperationToList(data);
	}

	// 对ListView添加逻辑操作，对其进行赋值
	private void addOperationToList(SharedPreferences data) {

		// 获取小时天气预报的数据，将其存入到链表中，作为Adapter中的底层数据来源
		List<AdapterHourlyInfoModel> hourlyList = new ArrayList<AdapterHourlyInfoModel>();
		String date = "hourly_date";
		String pop = "hourly_pop";
		String sc = "hourly_sc";
		String tmp = "hourly_tmp";
		// 通過sharedPreference中獲取數據，為ListView獲取數據
		for (int i = 0; i < data.getInt("hourly_count", 0); i++) {
			String hourly_date = date + i;
			String hourly_pop = pop + i;
			String hourly_sc = sc + i;
			String hourly_tmp = tmp + i;
			String time = data.getString(hourly_date, "");
			AdapterHourlyInfoModel model = new AdapterHourlyInfoModel();
			model.setDate(Utility.splitDateString(time, 1));
			model.setPop(data.getString(hourly_pop, ""));
			model.setSc(data.getString(hourly_sc, ""));
			model.setTmp(data.getString(hourly_tmp, ""));
			hourlyList.add(model);
		}
		ListAdapter adapter = new AdapterForHourlyInfo(hourlyList, mContext);
		mHorizontalGridViewForHourly.setAdapter(adapter);
	}

	// 初始化各个组件
	private void initComponent(View v) {
		// TODO Auto-generated method stub
		mCurrentDate = (TextView) v.findViewById(R.id.current_data_text);
		mUpdateTime = (TextView) v.findViewById(R.id.publish_data_text);
		mCityName = (TextView) v.findViewById(R.id.city_name);
		mWeatherTxt = (TextView) v.findViewById(R.id.weather_info);
		mWeatherImage = (ImageView) v.findViewById(R.id.weather_info_image);
		mWeatherTemperture = (TextView) v
				.findViewById(R.id.weather_temperature);
		mWeekTxt = (TextView) v.findViewById(R.id.week_data);
		mTopTemperture = (TextView) v.findViewById(R.id.top_temperature);
		mLowTemperture = (TextView) v.findViewById(R.id.low_temperature);
		mHorizontalGridViewForHourly = (GridView) v
				.findViewById(R.id.horizontal_gridview_for_hourly);
	}

}