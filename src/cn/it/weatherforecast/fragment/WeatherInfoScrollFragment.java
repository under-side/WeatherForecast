package cn.it.weatherforecast.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.activity.SuggestionDetailActivity;
import cn.it.weatherforecast.fragment.adapter.AdapterForDailyInfo;
import cn.it.weatherforecast.fragment.adapter.AdapterForGridView2Info;
import cn.it.weatherforecast.fragment.adapter.AdapterForHourlyInfo;
import cn.it.weatherforecast.fragment.adapter.AdapterGridView1Info;
import cn.it.weatherforecast.model.AdapterDailyInfoModel;
import cn.it.weatherforecast.model.AdapterHourlyInfoModel;
import cn.it.weatherforecast.model.ModelForGrid1;
import cn.it.weatherforecast.model.ModelForGrid2;
import cn.it.weatherforecast.util.MyApplication;
import cn.it.weatherforecast.util.Utility;
import cn.it.weatherforecast.widget.InnerListView;

public class WeatherInfoScrollFragment extends Fragment {

	private InnerListView mDailyInfoList;
	private TextView mWeatherDescribe;
	private GridView mGridView1, mGridView2, mGridView3;
	private GridView mHorizontalGridViewForHourly;
	private ScrollView mScrollView;
	SharedPreferences mSharedPreferences;

	public WeatherInfoScrollFragment(SharedPreferences data) {
		mSharedPreferences = data;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_weather_scroll,
				container, false);
		initComponent(view);

		return view;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		showViewFromData(mSharedPreferences);
	}

	// ��������data��������fragment�еĸ���������и�ֵ
	protected void showViewFromData(SharedPreferences data) {
		// TODO Auto-generated method stub

		mWeatherDescribe.setText("���죺"
				+ data.getString("suggestion_comf_txt", ""));

		// ��ÿСʱ����Ԥ��List���UI����
		addOperationToList(data);

		// ��ÿ������Ԥ��List���UI����
		addOperationForDailyList(data);

		// ��GridView���UI����
		addOperationForGridView1(data);

		addOperationForGridView2(data);

		addOperationForGridView3(data);

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
		for (int i = 0; i < 5; i++) {
			String hourly_date = date + i;
			String hourly_pop = pop + i;
			String hourly_dir = dir + i;
			String hourly_tmp = tmp + i;
			String time = data.getString(hourly_date, "");
			AdapterHourlyInfoModel model = new AdapterHourlyInfoModel();
			if (i >= data.getInt("hourly_count", 0)) {
				model.setPop("��\n��\n��");
				hourlyList.add(model);
			} else {
				model.setDate(Utility.splitDateString(time, 1));
				model.setPop(data.getString(hourly_pop, "") + "%");
				model.setDir(data.getString(hourly_dir, ""));
				model.setTmp(data.getString(hourly_tmp, "") + "��");
				hourlyList.add(model);
			}
		}
		ListAdapter adapter = new AdapterForHourlyInfo(hourlyList,
				MyApplication.getContext());
		mHorizontalGridViewForHourly.setAdapter(adapter);
	}

	private void addOperationForGridView3(SharedPreferences data) {
		ArrayList<ModelForGrid2> grid3List = new ArrayList<ModelForGrid2>();
		for (int i = 0; i < 6; i++) {
			ModelForGrid2 model = new ModelForGrid2();
			switch (i) {
			case 0:
				model.setId(R.drawable.clothes);
				model.setName("����ָ��");
				model.setName_txt(data.getString("suggestion_drsg", ""));
				model.setName_txt_detail(data.getString("suggestion_drsg_txt",
						""));
				grid3List.add(model);
				break;
			case 1:
				model.setId(R.drawable.flu);
				model.setName("����ָ��");
				model.setName_txt(data.getString("suggestion_flu", ""));
				model.setName_txt_detail(data.getString("suggestion_flu_txt",
						""));
				grid3List.add(model);
				break;
			case 2:
				model.setId(R.drawable.travel);
				model.setName("����ָ��");
				model.setName_txt(data.getString("suggestion_trav", ""));
				model.setName_txt_detail(data.getString("suggestion_trav_txt",
						""));
				grid3List.add(model);
				break;
			case 3:
				model.setId(R.drawable.sport);
				model.setName("�˶�ָ��");
				model.setName_txt(data.getString("suggestion_sport", ""));
				model.setName_txt_detail(data.getString("suggestion_sport_txt",
						""));
				grid3List.add(model);
				break;
			case 4:
				model.setId(R.drawable.car);
				model.setName("ϴ��ָ��");
				model.setName_txt(data.getString("suggestion_cw", ""));
				model.setName_txt_detail(data
						.getString("suggestion_cw_txt", ""));
				grid3List.add(model);
				break;
			case 5:
				model.setId(R.drawable.shan);
				model.setName("��ɹָ��");
				model.setName_txt(data.getString("suggestion_uv", ""));
				model.setName_txt_detail(data
						.getString("suggestion_uv_txt", ""));
				grid3List.add(model);
				break;
			default:
				break;
			}
			addDetailTxtActivityForGridView3(grid3List);
			BaseAdapter adapter = new AdapterForGridView2Info(
					MyApplication.getContext(), grid3List);
			mGridView3.setAdapter(adapter);
		}
	}

	// �O��GridView3��item���c���¼�̎��
	private void addDetailTxtActivityForGridView3(
			final ArrayList<ModelForGrid2> list) {
		mGridView3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int positon,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyApplication.getContext(),
						SuggestionDetailActivity.class);
				ModelForGrid2 model = list.get(positon);
				intent.putExtra("ClickItem", model);
				startActivity(intent);
			}
		});
	}

	private void addOperationForGridView2(SharedPreferences data) {

		ArrayList<ModelForGrid2> grid2List = new ArrayList<ModelForGrid2>();
		for (int i = 0; i < 2; i++) {
			ModelForGrid2 model = new ModelForGrid2();
			if (i == 0) {
				model.setId(R.drawable.water);
				model.setName("ʪ��");
				model.setName_txt(data.getString("now_hum", "") + "%");
			} else {
				model.setId(R.drawable.tmp);
				model.setName("����¶�");
				model.setName_txt(data.getString("now_fl", "") + "��");
			}
			grid2List.add(model);
		}
		BaseAdapter adapter = new AdapterForGridView2Info(
				MyApplication.getContext(), grid2List);
		mGridView2.setAdapter(adapter);
	}

	private void addOperationForGridView1(SharedPreferences data) {
		// TODO Auto-generated method stub
		ArrayList<ModelForGrid1> grid1List = new ArrayList<ModelForGrid1>();
		for (int i = 0; i < 2; i++) {
			ModelForGrid1 model = new ModelForGrid1();
			if (i == 0) {
				model.setId(R.drawable.flow);
				model.setName1("����");
				model.setName1_txt(data.getString("now_dir", ""));
				model.setName2("����");
				model.setName2_txt(data.getString("now_sc", ""));
			} else {
				model.setId(R.drawable.sun);
				model.setName1("�ճ�");
				model.setName1_txt(data.getString("daily_sr1", ""));
				model.setName2("����");
				model.setName2_txt(data.getString("daily_ss1", ""));
			}
			grid1List.add(model);

		}
		BaseAdapter adapter = new AdapterGridView1Info(
				MyApplication.getContext(), grid1List);
		mGridView1.setAdapter(adapter);
	}

	private void addOperationForDailyList(SharedPreferences data) {
		// TODO Auto-generated method stub
		ArrayList<AdapterDailyInfoModel> dailyList = new ArrayList<AdapterDailyInfoModel>();
		String txt_d = "daily_txt_d";
		String date = "daily_date";
		String max = "daily_max";
		String min = "daily_min";
		String code = "daily_code_d";
		for (int i = 0; i < 7; i++) {
			AdapterDailyInfoModel model = new AdapterDailyInfoModel();
			String daily_txt_d = txt_d + i;
			String daily_date = date + i;
			String daily_max = max + i;
			String daily_min = min + i;
			String daily_code_d = code + i;
			if (i == 0) {
				model.setDate("����");
			} else {
				model.setDate(Utility.getWeekFromDate(Utility.splitDateString(
						data.getString(daily_date, ""), 0)));
			}
			model.setLowTemperture(data.getString(daily_min, ""));
			model.setTopTemperture(data.getString(daily_max, ""));
			model.setWeatherTxt(data.getString(daily_txt_d, ""));
			model.setCode(data.getString(daily_code_d, ""));

			dailyList.add(model);
		}
		BaseAdapter adapter = new AdapterForDailyInfo(
				MyApplication.getContext(), dailyList);
		mDailyInfoList.setAdapter(adapter);
	}

	private void initComponent(View v) {
		// TODO Auto-generated method stub
		mDailyInfoList = (InnerListView) v.findViewById(R.id.daily_list_view);
		mDailyInfoList.setVerticalScrollBarEnabled(false);
		mWeatherDescribe = (TextView) v.findViewById(R.id.weather_describe);
		mGridView1 = (GridView) v.findViewById(R.id.frist_grid_view);
		mGridView2 = (GridView) v.findViewById(R.id.second_grid_view);
		mGridView3 = (GridView) v.findViewById(R.id.thrid_grid_view);
		mScrollView = (ScrollView) v.findViewById(R.id.weather_info_scroll);
		mScrollView.setVerticalScrollBarEnabled(false);
		mHorizontalGridViewForHourly = (GridView) v
				.findViewById(R.id.horizontal_gridview_for_hourly);
		mHorizontalGridViewForHourly.setVerticalScrollBarEnabled(false);
	}
}