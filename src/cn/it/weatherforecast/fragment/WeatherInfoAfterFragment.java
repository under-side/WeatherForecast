package cn.it.weatherforecast.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.util.MyApplication;
import cn.it.weatherforecast.util.Utility;

public class WeatherInfoAfterFragment extends Fragment {

	private TextView mCityName;
	private TextView mWeatherTxt;
	private ImageView mWeatherImage;
	private TextView mWeatherTemperture;

	private static final String mPhtotURL = "http://files.heweather.com/cond_icon/";

	private LruCache<String, Bitmap> mMemoryCache;
	SharedPreferences mSharedPreferences;

	public WeatherInfoAfterFragment(SharedPreferences data) {
		mSharedPreferences = data;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		// 获取该应用的缓存文件
		mMemoryCache = MyApplication.getLruCache();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_weatherinfo_after,
				container, false);

		initComponent(view);

		showDataView(mSharedPreferences);

		return view;
	}

	private void showDataView(SharedPreferences data) {
		// TODO Auto-generated method stub

		mCityName.setText(data.getString("basic_city", ""));
		mWeatherTxt.setText(data.getString("now_txt", ""));
		mWeatherTemperture.setText(data.getString("now_tmp", "") + "°");

		final String url = mPhtotURL + data.getString("now_code", "") + ".png";
		Bitmap imageBitmap = mMemoryCache.get(url);
		// 从Cache中如果直接获取到指定的图片，则直接使用
		if (imageBitmap != null) {
			mWeatherImage.setImageBitmap(imageBitmap);
		}
		// 如果Cache中没有指定的Bitmap时，从网上获取资源，并将其存入Cache中，以备后面使用
		else {
			Utility.setImageViewFromHttp(url, mWeatherImage);
		}
	}

	// 将对日期进行分割的代码封装起来，可以重复使用
	public String splitDateString(String time, int index) {
		String[] splitDate = time.split(" ");
		return splitDate[index];
	}

	private void initComponent(View v) {
		// TODO Auto-generated method stub
		mCityName = (TextView) v.findViewById(R.id.city_name);
		mWeatherTxt = (TextView) v.findViewById(R.id.weather_info);
		mWeatherImage = (ImageView) v.findViewById(R.id.weather_info_image);
		mWeatherTemperture = (TextView) v
				.findViewById(R.id.weather_temperature);
	}
}
