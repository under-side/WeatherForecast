package cn.it.weatherforecast.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.util.AsyncCallbackListenerForBitmap;
import cn.it.weatherforecast.util.DownloadBitmapForImage;
import cn.it.weatherforecast.util.LogUtil;
import cn.it.weatherforecast.util.MyApplication;

public class WeatherInfoAfterFragment extends Fragment {

	private TextView mCityName;
	private TextView mWeatherTxt;
	private ImageView mWeatherImage;
	private TextView mWeatherTemperture;
	
	private static final String mPhtotURL = "http://files.heweather.com/cond_icon/";

	private String mSelectCityId;
	private Context mContext;
	private LruCache<String, Bitmap> mMemoryCache;
	public WeatherInfoAfterFragment(Context context, String selectCityId) {
		mContext = context;
		mSelectCityId = selectCityId;
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
		View view = inflater.inflate(R.layout.fragment_weatherinfo_after,
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

		mCityName.setText(data.getString("basic_city", ""));
		mWeatherTxt.setText(data.getString("now_txt", ""));
		mWeatherTemperture.setText(data.getString("now_tmp", "")+"��");

		final String url = mPhtotURL + data.getString("now_code", "") + ".png";
        Bitmap imageBitmap=mMemoryCache.get(url);
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
							
							mMemoryCache.put(url, imageBitmap);
						}
						
						@Override
						public void onError(String errorMessage) {
							// TODO Auto-generated method stub
							LogUtil.d("WeatherInfoBeforeFragment", errorMessage);
						}
					});
			downloadImage.execute(url);
		}
	}
	 //�������ڽ��зָ�Ĵ����װ�����������ظ�ʹ��
		public String splitDateString(String time,int index)
		{
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
