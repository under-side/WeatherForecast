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
 * 该类实现的功能：
 * 1，显示SQLite中存储的选中的城市列表的天气详细信息
 * 2，手动刷新
 * 3，ViewPager添加pagerchangedListener，活动刷新
 * 4，设置更新时间，每个十五分钟，才会再次刷新
 */
public class WeatherInfo extends FragmentActivity {

	private Button mSelectAreasButton;

	// 当前ViewPager显示的城市信息的Code
	private int mCurrentIndex = 0;
	private SharedPreferences mIndexRember;
	private String LAST_INDEX = "Last_Index";
	// ViewPager组件及适配器
	private ViewPager mWeatherInfoPager;
	private AdapterForWeatherInfoPager mWeatherInfoPagerdapter;
	// 获取数据库，并取出其中的数据
	private WeatherForecastDB mDB;
	// 选择的城市的fragment
	private List<Fragment> mSelectCityFragment;

	// 两个标志变量，用于判断该活动从什么地方跳转
	protected static String FROM_DIALOG = "FromDialog";
	protected static final String FROM_SELECTED_AREA = "FromSelectedArea";
	protected static final String START_AUTO_UPDATE = "StartAutoService";

	// 從SQLite中獲取已經選擇的城市列表
	private List<SelectedAreas> mSelectedAreas;

	// 用於加載數據時的進度條
	private ProgressDialog mProgressDialog;

	// 下載天氣信息的URL地址
	private static final String url = "https://api.heweather.com/x3/weather?cityid=";
	private static final String key = "&key=e880b41d75d840d7aaaad18356139993";

	// 獲取指定的天氣存儲的SharedPreference數據
	private SharedPreferences mWeatherInforData;

	// 此標誌變量用於表示是否存在Fragment，然後去更新天氣
	private boolean isNullFragment = true;

	// 执行初始化操作
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_info);

		mSelectAreasButton = (Button) findViewById(R.id.weatherinfo_add_city);
		mWeatherInfoPager = (ViewPager) findViewById(R.id.weather_info_pager);

		mDB = MyApplication.getWeatherForecastDB();
		mSelectCityFragment = new ArrayList<Fragment>();
		// 获取SQLite中存储的选择的城市ID
		mSelectedAreas = mDB.loadSelectedAreas();

		// 获取存储index文件
		mIndexRember = getSharedPreferences("settinig", Context.MODE_PRIVATE);

		// 从文件获取上次关闭时记录的index
		mCurrentIndex = mIndexRember.getInt(LAST_INDEX, 0);
		// 执行初始化操作，完成该活动中的逻辑操作
		addOperationToComponent();
	}

	// 向该活动中的组件添加逻辑处理
	private void addOperationToComponent() {
		/*
		 * 向button按钮添加监听事件处理
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
		 * 添加对ViewPager的操作 1、根据SQLite中的数据来获取ViewPager的Adapter数据
		 * 2、向ViewPager中添加onPagerChangeListener
		 */
		if (mSelectedAreas.size() == 0) {
			EmptyWeatherInfoFragment emptyFragment = new EmptyWeatherInfoFragment();
			mSelectCityFragment.add(emptyFragment);
			mSelectAreasButton.setVisibility(View.INVISIBLE);
		} else {
			isNullFragment = false;
			// 獲取其他活動通過Intent傳送過來的數據
			String codeFromDialog = getIntent().getStringExtra(FROM_DIALOG);
			String codeFromSelectedArea = getIntent().getStringExtra(
					FROM_SELECTED_AREA);

			// 从SQLite中获取所选中的城市，添加到ViewPager
			for (int i = 0; i < mSelectedAreas.size(); i++) {
				SelectedAreas model = mSelectedAreas.get(i);
				String selectedCode = model.getSelectedCode();

				// 判断是否从其他地方跳转过来，是则设置当前页为选定的页，否则默认为第一页
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
	 * 在activity的onResume方法中当该活动已经全部准备好与用户显示时去后台下载城市名(non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 加入判断，使只有第一次运行的时候才去下载数据，避免了重复下载的问题
		if (mDB.loadAreas().size() == 0) {
			// 当activity创建时，创建一个后台服务，用于下载城市信息
			Intent intent = new Intent(this, DownAreasService.class);
			startService(intent);
		}
		// 判断当前不为空fragment时，才开启后台更新服务
		if (!isNullFragment) {
			// 当activity到达前台与用户交互时，开启当前的城市后台更新服务
			Intent i = new Intent(WeatherInfo.this,
					AutoUpdateWeatherService.class);
			i.putExtra(START_AUTO_UPDATE, mSelectedAreas.get(mCurrentIndex)
					.getSelectedCode());
			startService(i);
		}

	}

	// 当当前的活动不可见时，记录当前的Index，回复时获取该index
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mIndexRember.edit().putInt(LAST_INDEX, mCurrentIndex).commit();
	}

	// 得到自定义的菜单视图
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_weather_info, menu);
		return true;
	}

	// 设置菜单中的item监听处理事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_item_referesh_city:
			/*
			 * 在更新时间之前，先判断前后更新时间差是否在30分钟，是则更新，不是则不跟新 这种方法，避免了来回重复更新的问题
			 */
			// 顯示progressdialog，用於等待線程下載
			if (!isNullFragment) {
				showProgressDialog();
				downloadAndUpdateWeatherInfo();
			} else {
				Toast.makeText(this, "还没有选择天气", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
		return true;
	}

	/*
	 * 此方法用於封裝下載的當前城市的天氣信息，并根據下載的數據跟新UI界面
	 */
	private void downloadAndUpdateWeatherInfo() {

		String currentAreaCode = mSelectedAreas.get(mCurrentIndex)
				.getSelectedCode();

		// 一旦執行更新數據操作，則將當前的更新時間同步到SQLite中該Code下
		mDB.updateSelectedAreasTime(currentAreaCode, new Date().getTime());

		// 点击刷新后，重新下载当前的城市数据，并去重新绘制界面
		mWeatherInforData = getSharedPreferences(currentAreaCode,
				Context.MODE_PRIVATE);
		// 先删除文件中的数据
		mWeatherInforData.edit().clear().commit();
		// 組裝下載指定天氣的URL路徑
		String weatherUrl = url
				+ mSelectedAreas.get(mCurrentIndex).getSelectedCode() + key;
		// 開啟一個線程，重新下載指定城市的天氣信息
		HttpUtilForDowloadJson.getJsonFromHttp(weatherUrl,
				new HttpCallbackListenerForJson() {

					@Override
					public void onFinish(String response) {
						// TODO Auto-generated method stub
						boolean result = false;
						result = Utility.handleWeatherInfoResponseByJSON(
								mWeatherInforData, response);
						// 判断是否数据已经下载完成，并存储在指定的SharedPreference中
						if (result) {
							// 切换到主线程中去执行相应的UI更新
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

	// 根据获取的数据更新指定的fragment中的UI组件
	private void updateUI() {
		// 获取ViewPager中的Fragment，并跟新其数据
		FragmentManager manager = this.getSupportFragmentManager();
		WeatherInfoFragment weatherInfoFragment = (WeatherInfoFragment) manager
				.findFragmentByTag("android:switcher:"
						+ R.id.weather_info_pager + ":" + mCurrentIndex);
		weatherInfoFragment.updateView(mWeatherInforData);
		Toast.makeText(this, "刷新完成", Toast.LENGTH_SHORT).show();
	}

	// 当开启子线程，进行耗时操作时，打开一个进度条，进行人性化设计
	private void showProgressDialog() {
		// TODO Auto-generated method stub

		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		mProgressDialog.show();
	}

	// 关闭进度条
	private void closeProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

}
