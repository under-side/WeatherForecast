package cn.it.weatherforecast.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.model.AdapterHourlyInfoModel;
import cn.it.weatherforecast.util.AsyncCallbackListenerForBitmap;
import cn.it.weatherforecast.util.DownloadBitmapForImage;
import cn.it.weatherforecast.util.HorizontalListView;
import cn.it.weatherforecast.util.LogUtil;
import cn.it.weatherforecast.util.LruCacheUtil;

public class WeatherInfoBeforeFragment extends Fragment {

	private TextView mCurrentDate;
	private TextView mUpdateTime;
	private TextView mCityName;
	private TextView mWeatherTxt;
	private ImageView mWeatherImage;
	private TextView mWeatherTemperture;
	private TextView mWeekTxt;
	private TextView mTopTemperture, mLowTemperture;
	private HorizontalListView mHourlyInfoList;

	private static final String mPhtotURL = "http://files.heweather.com/cond_icon/";

	private String mSelectCityId;
	private Context mContext;
	private LruCacheUtil mMemoryCacheUtil;

	public WeatherInfoBeforeFragment(Context context, String selectCityId) {
		mContext = context;
		mSelectCityId = selectCityId;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		//��ȡ��Ӧ�õĻ����ļ�
		mMemoryCacheUtil=LruCacheUtil.getLruCacheUtil();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_weatherinfo_before,
				null);

		initComponent(view);

		SharedPreferences data = mContext.getSharedPreferences("CN101310215",
				Context.MODE_PRIVATE);
//		SharedPreferences data = mContext.getSharedPreferences(mSelectCityId,
//				Context.MODE_PRIVATE);
		showDataView(data);
		
		return view;
	}

	private void showDataView(SharedPreferences data) {
		// TODO Auto-generated method stub
		mCurrentDate.setText(data.getString("daily_date1", ""));

		mUpdateTime.setText("������    "+splitDateString(data.getString("basic_loc", ""), 1));
		mCityName.setText(data.getString("basic_city", ""));
		mWeatherTxt.setText(data.getString("now_txt", ""));
		mWeatherTemperture.setText(data.getString("now_tmp", "")+"��");
		mWeekTxt.setText(getWeekFromDate(splitDateString(data.getString("basic_loc", ""), 0)));
		mTopTemperture.setText(data.getString("daily_max1", "")+"��");
		mLowTemperture.setText(data.getString("daily_min1", "")+"��");

		final String url = mPhtotURL + data.getString("now_code", "") + ".png";
        Bitmap imageBitmap=mMemoryCacheUtil.getBitmapFromMemCache(url);
        //��Cache�����ֱ�ӻ�ȡ��ָ����ͼƬ����ֱ��ʹ��
		if(imageBitmap!=null)
		{
			mWeatherImage.setImageBitmap(imageBitmap);
		}
		//���Cache��û��ָ����Bitmapʱ�������ϻ�ȡ��Դ�����������Cache�У��Ա�����ʹ��
		else{
			
			DownloadBitmapForImage downloadImage = new DownloadBitmapForImage(
					new AsyncCallbackListenerForBitmap() {
						
						@Override
						public void onFinish(byte[] photo) {
							// TODO Auto-generated method stub
							
							Bitmap imageBitmap = BitmapFactory.decodeByteArray(photo, 0,
									photo.length);
							mWeatherImage.setImageBitmap(imageBitmap);
							
							mMemoryCacheUtil.addBitmapToMemoryCache(url, imageBitmap);
						}
						
						@Override
						public void onError(String errorMessage) {
							// TODO Auto-generated method stub
							LogUtil.d("WeatherInfoBeforeFragment", errorMessage);
						}
					});
			downloadImage.execute(url);
		}
		// ��ListView���в����߼���д
		addOperationToList(data);
	}
    //�������ڽ��зָ�Ĵ����װ�����������ظ�ʹ��
	public String splitDateString(String time,int index)
	{
		String[] splitDate = time.split(" ");
		return splitDate[index];
	}
	
	//��ListView����߼�������������и�ֵ
	private void addOperationToList(SharedPreferences data) {

		// ��ȡСʱ����Ԥ�������ݣ�������뵽�����У���ΪAdapter�еĵײ�������Դ
		List<AdapterHourlyInfoModel> hourlyList = new ArrayList<AdapterHourlyInfoModel>();
		String date = "hourly_date";
		String pop = "hourly_pop";
		String sc = "hourly_sc";
		String tmp = "hourly_tmp";
		//�ֽ����Ϊi=0 �ȻὫ��Ĺ���
		for (int i = 0; i < 5; i++) {
			String hourly_date = date + i;
			String hourly_pop = pop + i;
			String hourly_sc = sc + i;
			String hourly_tmp = tmp + i;
			AdapterHourlyInfoModel model = new AdapterHourlyInfoModel();
			model.setDate(splitDateString(data.getString(hourly_date, ""), 1));
			model.setPop(data.getString(hourly_pop, ""));
			model.setSc(data.getString(hourly_sc, ""));
			model.setTmp(data.getString(hourly_tmp, ""));
			hourlyList.add(model);
		}
		ListAdapter adapter = new AdapterForHourlyInfo(hourlyList, mContext);
		mHourlyInfoList.setAdapter(adapter);
	}

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
		mHourlyInfoList = (HorizontalListView) v.findViewById(R.id.daily_horizon_listview);
	}

	public String getWeekFromDate(String d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date date;
		try {
			date = sdf.parse(d);
			String[] weekOfDays = { "������", "����һ", "���ڶ�", "������", "������", "������",
					"������" };
			Calendar calendar = Calendar.getInstance();
			if (date != null) {
				calendar.setTime(date);
			}
			int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			if (w < 0) {
				w = 0;
			}
			return weekOfDays[w];
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}