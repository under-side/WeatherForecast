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
	
	// ��ǰViewPager��ʾ�ĳ�����Ϣ��Code
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

		// ִ�г�ʼ����������ɸû�е��߼�����
		addOperationToComponent();
	}

	// ��û�е��������߼�����
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

	// ��Ӷ�ViewPager�Ĳ���
	private void initViewPager() {
		// TODO Auto-generated method stub

		mWeatherInfoPager = (ViewPager) findViewById(R.id.weather_info_pager);

		mSelectCityFragment = new ArrayList<Fragment>();
		// ��ȡSQLite�д洢��ѡ��ĳ���ID
		List<SelectedAreas> selectedAreas = mDB.loadSelectedAreas();
		if (selectedAreas.size() == 0) {
			EmptyWeatherInfoFragment emptyFragment = new EmptyWeatherInfoFragment();
			mSelectCityFragment.add(emptyFragment);
			mSelectAreasButton.setVisibility(View.INVISIBLE);
		} else {
			//�@ȡ�������ͨ�^Intent�����^��Ĕ���
			String codeFromDialog=getIntent().getStringExtra(FROM_DIALOG);
			String codeFromSelectedArea=getIntent().getStringExtra(FROM_SELECTED_AREA);
			
			//��SQLite�л�ȡ��ѡ�еĳ��У���ӵ�ViewPager
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

	// ��activity��onResume�����е��û�Ѿ�ȫ��׼�������û���ʾʱȥ��̨���س�����
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// �����жϣ�ʹֻ�е�һ�����е�ʱ���ȥ�������ݣ��������ظ����ص�����
		if (mDB.loadAreas().size() == 0) {
			// ��activity����ʱ������һ����̨�����������س�����Ϣ
			Intent intent = new Intent(this, DownAreasService.class);
			startService(intent);
		}
		super.onResume();
	}

	/*
	 * �����������ʽΪsimpleTaskʱ�������Activity�Ѿ����ڣ���getIntent���յ�����һ��Activity��Intent��
	 * Intent��û�и��£���������һ�ε�Intent���ݡ���������ڣ����õ����µ�Intent��
	 * ����onNewIntent������Ϊ�˸���Intent
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
			// ���ˢ�º��������ص�ǰ�ĳ������ݣ���ȥ���»��ƽ���
			HttpUtilForDowloadJson.getWeatherInfoFromHttp(mCurrentName,
					mCurrentCityId, WeatherInfo.this);
			Intent i = new Intent(this, WeatherInfo.class);
			startActivity(i);
			//�N����ǰ���~activity��Ȼ���Լ��{���Լ������_�����¼��d
			finish();
			break;
		default:
			break;
		}
		return true;
	}

}
