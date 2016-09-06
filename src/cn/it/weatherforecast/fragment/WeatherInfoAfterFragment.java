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
		// ��ȡ��Ӧ�õĻ����ļ�
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
		mWeatherTemperture.setText(data.getString("now_tmp", "") + "��");

		final String url = mPhtotURL + data.getString("now_code", "") + ".png";
		Bitmap imageBitmap = mMemoryCache.get(url);
		// ��Cache�����ֱ�ӻ�ȡ��ָ����ͼƬ����ֱ��ʹ��
		if (imageBitmap != null) {
			mWeatherImage.setImageBitmap(imageBitmap);
		}
		// ���Cache��û��ָ����Bitmapʱ�������ϻ�ȡ��Դ�����������Cache�У��Ա�����ʹ��
		else {
			Utility.setImageViewFromHttp(url, mWeatherImage);
		}
	}

	// �������ڽ��зָ�Ĵ����װ�����������ظ�ʹ��
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
