package cn.it.weatherforecast.fragment.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.model.AdapterDailyInfoModel;
import cn.it.weatherforecast.util.Utility;

public class AdapterForDailyInfo extends BaseAdapter {

	private Context mContext;
	private ArrayList<AdapterDailyInfoModel> mDailyInfo;
	private static final String mPhtotURL = "http://files.heweather.com/cond_icon/";
	private LruCache<String, Bitmap> mMemoryCache;

	public AdapterForDailyInfo(Context context,
			ArrayList<AdapterDailyInfoModel> dailyList) {
		mContext = context;
		mDailyInfo = dailyList;

		int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8;
		mMemoryCache = new LruCache<String, Bitmap>(maxSize);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDailyInfo.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mDailyInfo.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		// TODO Auto-generated method stub
		AdapterDailyInfoModel dailyData = mDailyInfo.get(position);
		// ��ȡ����������Ĺ���Ȩ
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.adapter_for_daily_info,
				container, false);

		DailyViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new DailyViewHolder();
			viewHolder.date = (TextView) view.findViewById(R.id.daily_time);
			viewHolder.lowTmp = (TextView) view
					.findViewById(R.id.daily_low_temperature);
			viewHolder.topTmp = (TextView) view
					.findViewById(R.id.daily_top_temperature);
			viewHolder.weatherTxt = (TextView) view
					.findViewById(R.id.daily_txt);
			viewHolder.image = (ImageView) view.findViewById(R.id.daily_image);
			// ��һ������Ա���Ŀ����,��������hold�洢��ȥ
			view.setTag(viewHolder);
		} else {
			view = convertView;
			// ȡ��֮ǰ�洢��hold
			viewHolder = (DailyViewHolder) view.getTag();
		}
		// ��ʼ��ֵ���
		viewHolder.date.setText(dailyData.getDate());
		viewHolder.lowTmp.setText(dailyData.getLowTemperture() + "��");
		viewHolder.topTmp.setText(dailyData.getTopTemperture() + "��");
		viewHolder.weatherTxt.setText(dailyData.getWeatherTxt());
		final String url = mPhtotURL + dailyData.getCode() + ".png";
		// ��Cache�����ֱ�ӻ�ȡ��ָ����ͼƬ����ֱ��ʹ��
		Bitmap imageBitmap = mMemoryCache.get(url);
		if (imageBitmap != null) {
			viewHolder.image.setImageBitmap(imageBitmap);
		} else {
			Utility.setImageViewFromHttp(url, viewHolder.image);
		}
		return view;
	}
}

class DailyViewHolder {
	TextView date, lowTmp, topTmp, weatherTxt;
	ImageView image;
}