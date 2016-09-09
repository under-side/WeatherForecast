package cn.it.weatherforecast.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import cn.it.weatherforecast.service.AutoUpdateWeatherService;
import cn.it.weatherforecast.service.DownAreasService;
import cn.it.weatherforecast.util.HttpCallbackListenerForJson;
import cn.it.weatherforecast.util.HttpUtilForDowloadJson;
import cn.it.weatherforecast.util.MyApplication;
import cn.it.weatherforecast.util.Utility;
/*
 * ����ʵ�ֵĹ��ܣ�
 * 1����ʾSQLite�д洢��ѡ�еĳ����б��������ϸ��Ϣ
 * 2���ֶ�ˢ��
 * 3��ViewPager���pagerchangedListener���ˢ��
 * 4�����ø���ʱ�䣬ÿ��ʮ����ӣ��Ż��ٴ�ˢ��
 */
public class WeatherInfo extends FragmentActivity {

	private Button mSelectAreasButton;

	// ��ǰViewPager��ʾ�ĳ�����Ϣ��Code
	private int mCurrentIndex = 0;
	private SharedPreferences mIndexRember;
	private String LAST_INDEX = "Last_Index";
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
	protected static final String START_AUTO_UPDATE = "StartAutoService";

	// ��SQLite�Ы@ȡ�ѽ��x��ĳ����б�
	private List<SelectedAreas> mSelectedAreas;

	// ��춼��d�����r���M�ȗl
	private ProgressDialog mProgressDialog;

	// ���d�����Ϣ��URL��ַ
	private static final String url = "https://api.heweather.com/x3/weather?cityid=";
	private static final String key = "&key=e880b41d75d840d7aaaad18356139993";

	// �@ȡָ�������惦��SharedPreference����
	private SharedPreferences mWeatherInforData;

	// �˘��I׃����춱�ʾ�Ƿ����Fragment��Ȼ��ȥ�������
	private boolean isNullFragment = true;

	// ִ�г�ʼ������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_info);

		mSelectAreasButton = (Button) findViewById(R.id.weatherinfo_add_city);
		mWeatherInfoPager = (ViewPager) findViewById(R.id.weather_info_pager);

		mDB = MyApplication.getWeatherForecastDB();
		mSelectCityFragment = new ArrayList<Fragment>();
		// ��ȡSQLite�д洢��ѡ��ĳ���ID
		mSelectedAreas = mDB.loadSelectedAreas();

		// ��ȡ�洢index�ļ�
		mIndexRember = getSharedPreferences("settinig", Context.MODE_PRIVATE);

		// ���ļ���ȡ�ϴιر�ʱ��¼��index
		mCurrentIndex = mIndexRember.getInt(LAST_INDEX, 0);
		// ִ�г�ʼ����������ɸû�е��߼�����
		addOperationToComponent();
	}

	// ��û�е��������߼�����
	private void addOperationToComponent() {
		/*
		 * ��button��ť��Ӽ����¼�����
		 */
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
		addFragmentToViewPagerAdapter();

		addPagerChangerListener();

		if (!isNullFragment) {
			downloadAndUpdateWeatherInfo();
		}
	}

	private void addFragmentToViewPagerAdapter() {
		// TODO Auto-generated method stub
		/*
		 * ��Ӷ�ViewPager�Ĳ��� 1������SQLite�е���������ȡViewPager��Adapter����
		 * 2����ViewPager�����onPagerChangeListener
		 */
		if (mSelectedAreas.size() == 0) {
			EmptyWeatherInfoFragment emptyFragment = new EmptyWeatherInfoFragment();
			mSelectCityFragment.add(emptyFragment);
			mSelectAreasButton.setVisibility(View.INVISIBLE);
		} else {
			isNullFragment = false;
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
	}

	private void addPagerChangerListener() {

		mWeatherInfoPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				mCurrentIndex = position;
				showProgressDialog();
				long currentTime = System.currentTimeMillis();
				long lastUpdateTime = mSelectedAreas.get(mCurrentIndex)
						.getUpdateTime();
				int timeDifference = (int) ((currentTime - lastUpdateTime) / (1000 * 60));
				if (timeDifference >= 15) {
					downloadAndUpdateWeatherInfo();
				} else {
					closeProgressDialog();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}
		});
	}

	/*
	 * ��activity��onResume�����е��û�Ѿ�ȫ��׼�������û���ʾʱȥ��̨���س�����(non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// �����жϣ�ʹֻ�е�һ�����е�ʱ���ȥ�������ݣ��������ظ����ص�����
		if (mDB.loadAreas().size() == 0) {
			// ��activity����ʱ������һ����̨�����������س�����Ϣ
			Intent intent = new Intent(this, DownAreasService.class);
			startService(intent);
		}
		// �жϵ�ǰ��Ϊ��fragmentʱ���ſ�����̨���·���
		if (!isNullFragment) {
			// ��activity����ǰ̨���û�����ʱ��������ǰ�ĳ��к�̨���·���
			Intent i = new Intent(WeatherInfo.this,
					AutoUpdateWeatherService.class);
			i.putExtra(START_AUTO_UPDATE, mSelectedAreas.get(mCurrentIndex)
					.getSelectedCode());
			startService(i);
		}

	}

	// ����ǰ�Ļ���ɼ�ʱ����¼��ǰ��Index���ظ�ʱ��ȡ��index
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mIndexRember.edit().putInt(LAST_INDEX, mCurrentIndex).commit();
	}

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
			/*
			 * �ڸ���ʱ��֮ǰ�����ж�ǰ�����ʱ����Ƿ���30���ӣ�������£������򲻸��� ���ַ����������������ظ����µ�����
			 */
			// �@ʾprogressdialog����춵ȴ��������d
			if (!isNullFragment) {
				showProgressDialog();
				downloadAndUpdateWeatherInfo();
			} else {
				Toast.makeText(this, "��û��ѡ������", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
		return true;
	}

	/*
	 * �˷�����춷��b���d�Į�ǰ���е������Ϣ�����������d�Ĕ�������UI����
	 */
	private void downloadAndUpdateWeatherInfo() {

		String currentAreaCode = mSelectedAreas.get(mCurrentIndex)
				.getSelectedCode();

		// һ�����и������������t����ǰ�ĸ��r�gͬ����SQLite��ԓCode��
		mDB.updateSelectedAreasTime(currentAreaCode, new Date().getTime());

		// ���ˢ�º��������ص�ǰ�ĳ������ݣ���ȥ���»��ƽ���
		mWeatherInforData = getSharedPreferences(currentAreaCode,
				Context.MODE_PRIVATE);
		// ��ɾ���ļ��е�����
		mWeatherInforData.edit().clear().commit();
		// �M�b���dָ������URL·��
		String weatherUrl = url
				+ mSelectedAreas.get(mCurrentIndex).getSelectedCode() + key;
		// �_��һ�����̣��������dָ�����е������Ϣ
		HttpUtilForDowloadJson.getJsonFromHttp(weatherUrl,
				new HttpCallbackListenerForJson() {

					@Override
					public void onFinish(String response) {
						// TODO Auto-generated method stub
						boolean result = false;
						result = Utility.handleWeatherInfoResponseByJSON(
								mWeatherInforData, response);
						// �ж��Ƿ������Ѿ�������ɣ����洢��ָ����SharedPreference��
						if (result) {
							// �л������߳���ȥִ����Ӧ��UI����
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									closeProgressDialog();
									updateUI();
								}
							});
						}
					}

					@Override
					public void onError(Exception e) {
						// TODO Auto-generated method stub
						e.printStackTrace();
					}
				});
	}

	// ���ݻ�ȡ�����ݸ���ָ����fragment�е�UI���
	private void updateUI() {
		// ��ȡViewPager�е�Fragment��������������
		FragmentManager manager = this.getSupportFragmentManager();
		WeatherInfoFragment weatherInfoFragment = (WeatherInfoFragment) manager
				.findFragmentByTag("android:switcher:"
						+ R.id.weather_info_pager + ":" + mCurrentIndex);
		weatherInfoFragment.updateView(mWeatherInforData);
		Toast.makeText(this, "ˢ�����", Toast.LENGTH_SHORT).show();
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
