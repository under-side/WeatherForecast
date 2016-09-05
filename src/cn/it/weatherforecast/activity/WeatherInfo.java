package cn.it.weatherforecast.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.db.WeatherForecastDB;
import cn.it.weatherforecast.fragment.EmptyWeatherInfoFragment;
import cn.it.weatherforecast.fragment.WeatherInfoFragment;
import cn.it.weatherforecast.fragment.adapter.AdapterForWeatherInfoPager;
import cn.it.weatherforecast.model.SelectedAreas;
import cn.it.weatherforecast.service.DownAreasService;
import cn.it.weatherforecast.util.ActivityCollector;
import cn.it.weatherforecast.util.HttpUtilForDowloadJson;
import cn.it.weatherforecast.util.MyApplication;

public class WeatherInfo extends FragmentActivity{


	private Button mSelectAreasButton;
	
	// 当前ViewPager显示的城市信息的Code
	private String mCurrentCityId;

	private ViewPager mWeatherInfoPager;
	private WeatherForecastDB mDB;
	private List<Fragment> mSelectCityFragment;
	private AdapterForWeatherInfoPager mWeatherInfoPagerdapter;
	private String mCurrentName;

	protected static String FROM_DIALOG="FromDialog";
	protected static final String FROM_SELECTED_AREA = "FromSelectedArea";
	
	private int mCurrentIndex=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_info);
		ActivityCollector.addActivity(this);

		mDB = MyApplication.getWeatherForecastDB();

		// 执行初始化操作，完成该活动中的逻辑操作
		addOperationToComponent();
	}

	// 向该活动中的组件添加逻辑处理
	private void addOperationToComponent() {
		// TODO Auto-generated method stub

		mSelectAreasButton = (Button) findViewById(R.id.weatherinfo_add_city);
		mSelectAreasButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(WeatherInfo.this, SelectAreasActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});

		initViewPager();
	}

	// 添加对ViewPager的操作
	private void initViewPager() {
		// TODO Auto-generated method stub

		mWeatherInfoPager = (ViewPager) findViewById(R.id.weather_info_pager);

		mSelectCityFragment = new ArrayList<Fragment>();
		// 获取SQLite中存储的选择的城市ID
		List<SelectedAreas> selectedAreas = mDB.loadSelectedAreas();
		if (selectedAreas.size() == 0) {
			EmptyWeatherInfoFragment emptyFragment = new EmptyWeatherInfoFragment();
			mSelectCityFragment.add(emptyFragment);
			mSelectAreasButton.setVisibility(View.INVISIBLE);
		} else {
			//獲取其他活動通過Intent傳送過來的數據
			String codeFromDialog=getIntent().getStringExtra(FROM_DIALOG);
			String codeFromSelectedArea=getIntent().getStringExtra(FROM_SELECTED_AREA);
			
			//从SQLite中获取所选中的城市，添加到ViewPager
			for (int i=0;i<selectedAreas.size();i++) 
			{
				SelectedAreas model=selectedAreas.get(i);
				String selectedCode=model.getSelectedCode();
				
				
				if(codeFromDialog!=null||codeFromSelectedArea!=null)
				{
					if(selectedCode.equals(codeFromDialog))
					{
						mCurrentIndex=i;
					}
					else if(selectedCode.equals(codeFromSelectedArea))
					{
						mCurrentIndex=i;
					}
				}
				mSelectCityFragment.add(new WeatherInfoFragment(this, selectedCode));
			}
		}
		mWeatherInfoPagerdapter = new AdapterForWeatherInfoPager(
				getSupportFragmentManager(), mSelectCityFragment);
		mWeatherInfoPager.setAdapter(mWeatherInfoPagerdapter);
		mWeatherInfoPager.setCurrentItem(mCurrentIndex);
	}

	// 在activity的onResume方法中当该活动已经全部准备好与用户显示时去后台下载城市名
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// 加入判断，使只有第一次运行的时候才去下载数据，避免了重复下载的问题
		if (mDB.loadAreas().size() == 0) {
			// 当activity创建时，创建一个后台服务，用于下载城市信息
			Intent intent = new Intent(this, DownAreasService.class);
			startService(intent);
		}
		super.onResume();
	}

	/*
	 * 当活动的启动方式为simpleTask时，如果该Activity已经存在，则getIntent接收的是上一个Activity的Intent，
	 * Intent并没有更新，即还是上一次的Intent数据。如果不存在，则会得到最新的Intent。
	 * 覆盖onNewIntent方法是为了更新Intent
	   protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		getIntent().putExtras(intent);
	    }
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_weather_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_item_referesh_city:
			// 点击刷新后，重新下载当前的城市数据，并去重新绘制界面
			HttpUtilForDowloadJson.getWeatherInfoFromHttp(mCurrentName,
					mCurrentCityId, WeatherInfo.this);
			Intent i = new Intent(this, WeatherInfo.class);
			startActivity(i);
			//銷毀當前的額activity，然後自己調用自己，在開啟重新加載
			finish();
			break;
		default:
			break;
		}
		return true;
	}

}
