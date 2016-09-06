package cn.it.weatherforecast.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

public class WeatherInfoBeforeFragment extends Fragment {

	private TextView mCurrentDate;
	private TextView mUpdateTime;
	private TextView mCityName;
	private TextView mWeatherTxt;
	private ImageView mWeatherImage;
	private TextView mWeatherTemperture;
	private TextView mWeekTxt;
	private TextView mTopTemperture, mLowTemperture;

	private LruCache<String, Bitmap> mMemoryCache;

	private static final String mPhtotURL = "http://files.heweather.com/cond_icon/";

	
    SharedPreferences sharedPreference;
    public WeatherInfoBeforeFragment(SharedPreferences data)
    {
    	sharedPreference=data;
    }
	
    @Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		//获取该应用的缓存文件
		mMemoryCache=MyApplication.getLruCache();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_weatherinfo_before, container,false);

		initComponent(view);

		return view;
	}
     
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		showDataView(sharedPreference);
		super.onStart();
	}
	
protected void showDataView(SharedPreferences data) {
		// TODO Auto-generated method stub
		mCurrentDate.setText(data.getString("daily_date0", ""));
		
		SimpleDateFormat format=new SimpleDateFormat("HH:mm",Locale.CHINA);
		mUpdateTime.setText("更新于  "+format.format(new Date()));
		
		mCityName.setText(data.getString("basic_city", ""));
		mWeatherTxt.setText(data.getString("now_txt", ""));
		mWeatherTemperture.setText(data.getString("now_tmp", "") + "°");
		mWeekTxt.setText(Utility.getWeekFromDate(Utility.splitDateString(
				data.getString("basic_loc", ""), 0)));
		mTopTemperture.setText(data.getString("daily_max0", "") + "°");
		mLowTemperture.setText(data.getString("daily_min0", "") + "°");

		 final String url = mPhtotURL + data.getString("now_code", "") + ".png";
		 Bitmap imageBitmap=mMemoryCache.get(url);
		 //从Cache中如果直接获取到指定的图片，则直接使用
		if(imageBitmap!=null)
		{
			mWeatherImage.setImageBitmap(imageBitmap);
		}
		else{
			//如果Cache中没有指定的Bitmap时，从网上获取资源，并将其存入Cache中，以备后面使用
			Utility.setImageViewFromHttp(url, mWeatherImage);
		}
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
		
	}

}