package cn.it.weatherforecast.fragment;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.activity.SelectAreasActivity;
import cn.it.weatherforecast.db.WeatherForecastDB;
import cn.it.weatherforecast.model.Areas;
import cn.it.weatherforecast.model.SelectedAreas;
import cn.it.weatherforecast.util.MyApplication;

public class HotCitySelectedFragment extends Fragment {

	private ListView mHotCityList;
	private Context mContext;
	private WeatherForecastDB mDB;
	private String[] mHotCityData;
	protected String mSelectCityName;
	protected String mSelectCityId;
	protected boolean isHaveCity=false;
	public HotCitySelectedFragment(Context context)
	{
		mContext=context;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_selected_hotcity, container,false);
		mDB=MyApplication.getWeatherForecastDB();
		initComponent(view);
		
		return view;
	}

	private void initComponent(View view) {
		// TODO Auto-generated method stub
		mHotCityList=(ListView) view.findViewById(R.id.list_hotcity);
		mHotCityList.setVerticalScrollBarEnabled(false);
		
		//添加热门城市数据，用于列表数据
		mHotCityData=new String[]{"北京","上海","广州","深圳","天津","杭州","南京","济南","重庆","青岛",
				"厦门","武汉","哈尔滨","郑州","长沙","苏州","成都","西安"};
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,mHotCityData);
		mHotCityList.setAdapter(adapter);
		//添加点击热门城市后的操作
		mHotCityList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Areas selectArea=mDB.loadAreas(mHotCityData[position]).get(0);
				mSelectCityName = selectArea.getCityName();
				mSelectCityId = selectArea.getCityId();

				List<SelectedAreas> areas = mDB.loadSelectedAreas();
				/*
				 * 将点击所选的并且在SQLite中没有的item的信息存放在SQLite中，标识为所选城市。
				 * 加上这种判断，避免了重复点击已经选择的城市，导致重复加载。
				 */
				if (areas.size() == 0) {
					// 添加到SQLite中
					mDB.saveSelectedAreasInfo(mSelectCityId, mSelectCityName,
							(new Date()).getTime());
				} else {
					for (SelectedAreas selectedAreas : areas) {
						if (selectedAreas.getSelectedCode().equals(
								mSelectCityId)) {
							isHaveCity = true;
							Toast.makeText(mContext,
									"已经存在所选城市", Toast.LENGTH_SHORT).show();
						}
					}
					if (!isHaveCity) {
						// 添加到SQLite中
						mDB.saveSelectedAreasInfo(mSelectCityId,
								mSelectCityName, (new Date()).getTime());
					}
				}
				// 启动SelectedAreasActivity，并将数据传送给他
				Intent intent = new Intent(mContext,
						SelectAreasActivity.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
}
