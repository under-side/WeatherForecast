package cn.it.weatherforecast.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.fragment.adapter.AdapterForHourlyInfo;
import cn.it.weatherforecast.model.AdapterHourlyInfoModel;
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
	private GridView mHorizontalGridViewForHourly;
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
		//��ȡ��Ӧ�õĻ����ļ�
		mMemoryCache=MyApplication.getLruCache();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_weatherinfo_before, container,false);

		initComponent(view);

		showDataView(sharedPreference);
		return view;
	}

	
	private void showDataView(SharedPreferences data) {
		// TODO Auto-generated method stub
		mCurrentDate.setText(data.getString("daily_date0", ""));
		
		SimpleDateFormat format=new SimpleDateFormat("HH:mm",Locale.CHINA);
		mUpdateTime.setText("������  "+format.format(new Date()));
		
		mCityName.setText(data.getString("basic_city", ""));
		mWeatherTxt.setText(data.getString("now_txt", ""));
		mWeatherTemperture.setText(data.getString("now_tmp", "") + "��");
		mWeekTxt.setText(Utility.getWeekFromDate(Utility.splitDateString(
				data.getString("basic_loc", ""), 0)));
		mTopTemperture.setText(data.getString("daily_max0", "") + "��");
		mLowTemperture.setText(data.getString("daily_min0", "") + "��");

		 final String url = mPhtotURL + data.getString("now_code", "") + ".png";
		 Bitmap imageBitmap=mMemoryCache.get(url);
		 //��Cache�����ֱ�ӻ�ȡ��ָ����ͼƬ����ֱ��ʹ��
		if(imageBitmap!=null)
		{
			mWeatherImage.setImageBitmap(imageBitmap);
		}
		else{
			//���Cache��û��ָ����Bitmapʱ�������ϻ�ȡ��Դ�����������Cache�У��Ա�����ʹ��
			Utility.setImageViewFromHttp(url, mWeatherImage);
		}
		// ��ListView���в����߼���д
		addOperationToList(data);
	}

	// ��ListView����߼�������������и�ֵ
	private void addOperationToList(SharedPreferences data) {

		// ��ȡСʱ����Ԥ�������ݣ�������뵽�����У���ΪAdapter�еĵײ�������Դ
		List<AdapterHourlyInfoModel> hourlyList = new ArrayList<AdapterHourlyInfoModel>();
		String date = "hourly_date";
		String pop = "hourly_pop";
		String dir = "hourly_dir";
		String tmp = "hourly_tmp";
		// ͨ�^sharedPreference�Ы@ȡ��������ListView�@ȡ����
		for (int i = 0; i <5 ; i++) {
			String hourly_date = date + i;
			String hourly_pop = pop + i;
			String hourly_dir = dir + i;
			String hourly_tmp = tmp + i;
			String time = data.getString(hourly_date, "");
			AdapterHourlyInfoModel model = new AdapterHourlyInfoModel();
			if(i>=data.getInt("hourly_count", 0))
			{
				model.setPop("��\n��\n��");
				hourlyList.add(model);
			}
			else
			{
				model.setDate(Utility.splitDateString(time, 1));
				model.setPop(data.getString(hourly_pop, "")+"%");
				model.setDir(data.getString(hourly_dir, ""));
				model.setTmp(data.getString(hourly_tmp, "")+"��");
				hourlyList.add(model);
			}
		}
		ListAdapter adapter = new AdapterForHourlyInfo(hourlyList, MyApplication.getContext());
		mHorizontalGridViewForHourly.setAdapter(adapter);
	}

	// ��ʼ���������
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
		mHorizontalGridViewForHourly.setVerticalScrollBarEnabled(false);
	}

}