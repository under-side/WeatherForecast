package cn.it.weatherforecast.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
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

public class WeatherInfo extends FragmentActivity {

	private Button mSelectAreasButton;

	// ��ǰViewPager��ʾ�ĳ�����Ϣ��Code
	private int mCurrentIndex = 0;
	// ViewPager�����������
	private ViewPager mWeatherInfoPager;
	private AdapterForWeatherInfoPager mWeatherInfoPagerdapter;
	// ��ȡ���ݿ⣬��ȡ�����е�����
	private WeatherForecastDB mDB;
	// ѡ��ĳ��е�fragment
	private List<Fragment> mSelectCityFragment;

	// ������־�����������жϸû��ʲô�ط���ת
	protected static String FROM_DIALOG = "FromDialog";
	protected static final String FROM_SELECTED_AREA = "FromSelectedArea";

	private List<SelectedAreas> mSelectedAreas;

	private ProgressDialog mProgressDialog;

	 private int bmpW;//����ͼƬ���  
	 private int offset;//ͼƬ�ƶ���ƫ���� 
	//ִ�г�ʼ������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_info);
		ActivityCollector.addActivity(this);

		mSelectAreasButton = (Button) findViewById(R.id.weatherinfo_add_city);
		mWeatherInfoPager = (ViewPager) findViewById(R.id.weather_info_pager);
		
		mDB = MyApplication.getWeatherForecastDB();
	    mSelectCityFragment = new ArrayList<Fragment>();
	    // ��ȡSQLite�д洢��ѡ��ĳ���ID
        mSelectedAreas = mDB.loadSelectedAreas();
        
        //��Application�л�ȡ�ϴδ洢����ֵ
        mCurrentIndex=MyApplication.getIndex();
		
        InitImage();
	}
	  /* 
     * ��ʼ��ͼƬ��λ������ 
     */  
    public void InitImage(){  
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.pager_select).getWidth();  
        DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        int screenW = dm.widthPixels;  
        offset = (screenW/4 - bmpW)/2;  
          
        //imgageview����ƽ�ƣ�ʹ�»���ƽ�Ƶ���ʼλ�ã�ƽ��һ��offset��  
        Matrix matrix = new Matrix();  
        matrix.postTranslate(offset, 0);  
    }  
	//ִ�о�����߼�����
@Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	// ִ�г�ʼ����������ɸû�е��߼�����
	addOperationToComponent();
}
	// ��û�е��������߼�����
	private void addOperationToComponent() {
		// TODO Auto-generated method stub
      //��button��ť��Ӽ����¼�����
		mSelectAreasButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(WeatherInfo.this,
						SelectAreasActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});

		// ��Ӷ�ViewPager�Ĳ���
		if (mSelectedAreas.size() == 0) {
			EmptyWeatherInfoFragment emptyFragment = new EmptyWeatherInfoFragment();
			mSelectCityFragment.add(emptyFragment);
			mSelectAreasButton.setVisibility(View.INVISIBLE);
		} else {
			// �@ȡ�������ͨ�^Intent�����^��Ĕ���
			String codeFromDialog = getIntent().getStringExtra(FROM_DIALOG);
			String codeFromSelectedArea = getIntent().getStringExtra(
					FROM_SELECTED_AREA);

			// ��SQLite�л�ȡ��ѡ�еĳ��У���ӵ�ViewPager
			for (int i = 0; i < mSelectedAreas.size(); i++) {
				SelectedAreas model = mSelectedAreas.get(i);
				String selectedCode = model.getSelectedCode();

				// �ж��Ƿ�������ط���ת�������������õ�ǰҳΪѡ����ҳ������Ĭ��Ϊ��һҳ
				if (codeFromDialog != null || codeFromSelectedArea != null) {
					if (selectedCode.equals(codeFromDialog)) {
						mCurrentIndex = i;
					} else if (selectedCode.equals(codeFromSelectedArea)) {
						mCurrentIndex = i;
					}
				}
				mSelectCityFragment.add(new WeatherInfoFragment(this,
						selectedCode));
			}
		}
		mWeatherInfoPagerdapter = new AdapterForWeatherInfoPager(
				getSupportFragmentManager(), mSelectCityFragment);
		mWeatherInfoPager.setAdapter(mWeatherInfoPagerdapter);
		mWeatherInfoPager.setCurrentItem(mCurrentIndex);

		// ��ViewPager����Ӽ���������������״̬�仯
		mWeatherInfoPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				 mCurrentIndex = position;
			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
				// TODO Auto-generated method stub
				mCurrentIndex = position;
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}
		});
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyApplication.setIndex(mCurrentIndex);
	}

	/*
	 * �����������ʽΪsimpleTaskʱ�������Activity�Ѿ����ڣ���getIntent���յ�����һ��Activity��Intent��
	 * Intent��û�и��£���������һ�ε�Intent���ݡ���������ڣ����õ����µ�Intent��
	 * ����onNewIntent������Ϊ�˸���Intent protected void onNewIntent(Intent intent) { //
	 * TODO Auto-generated method stub super.onNewIntent(intent);
	 * setIntent(intent); getIntent().putExtras(intent); }
	 */
	// �õ��Զ���Ĳ˵���ͼ
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_weather_info, menu);
		return true;
	}

	// ���ò˵��е�item���������¼�
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_item_referesh_city:
			// ���ˢ�º��������ص�ǰ�ĳ������ݣ���ȥ���»��ƽ���
			SharedPreferences data = getSharedPreferences(
					mSelectedAreas.get(mCurrentIndex).getSelectedCode(),
					Context.MODE_PRIVATE);
			// ��ɾ���ļ��е�����
			data.edit().clear().commit();
			showProgressDialog();
			HttpUtilForDowloadJson.getWeatherInfoFromHttp(
					mSelectedAreas.get(mCurrentIndex).getSelectedName(),
					mSelectedAreas.get(mCurrentIndex).getSelectedCode(),
					WeatherInfo.this);
			// ��whileѭ�����жϣ�ȷ��ֻ��һ��showProgress������ִ����
			while (true) {
				if (data.getString("status", "").equals("ok")) {
					closeProgressDialog();
					break;
				}
			}
			// ��ȡViewPager�е�Fragment��������������
			FragmentManager manager = this.getSupportFragmentManager();
			WeatherInfoFragment weatherInfoFragment = (WeatherInfoFragment) manager
					.findFragmentByTag("android:switcher:"
							+ R.id.weather_info_pager + ":" + mCurrentIndex);
			weatherInfoFragment.updateView(data);
			Toast.makeText(this, "ˢ�����", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return true;
	}

	// ���������̣߳����к�ʱ����ʱ����һ�����������������Ի����
	private void showProgressDialog() {
		// TODO Auto-generated method stub

		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		mProgressDialog.show();
	}

	// �رս�����
	private void closeProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

}
