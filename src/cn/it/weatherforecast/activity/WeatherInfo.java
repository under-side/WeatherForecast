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

	// 当前ViewPager显示的城市信息的Code
	private int mCurrentIndex = 0;
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

	private List<SelectedAreas> mSelectedAreas;

	private ProgressDialog mProgressDialog;

	 private int bmpW;//横线图片宽度  
	 private int offset;//图片移动的偏移量 
	//执行初始化操作
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_info);
		ActivityCollector.addActivity(this);

		mSelectAreasButton = (Button) findViewById(R.id.weatherinfo_add_city);
		mWeatherInfoPager = (ViewPager) findViewById(R.id.weather_info_pager);
		
		mDB = MyApplication.getWeatherForecastDB();
	    mSelectCityFragment = new ArrayList<Fragment>();
	    // 获取SQLite中存储的选择的城市ID
        mSelectedAreas = mDB.loadSelectedAreas();
        
        //从Application中获取上次存储的数值
        mCurrentIndex=MyApplication.getIndex();
		
        InitImage();
	}
	  /* 
     * 初始化图片的位移像素 
     */  
    public void InitImage(){  
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.pager_select).getWidth();  
        DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        int screenW = dm.widthPixels;  
        offset = (screenW/4 - bmpW)/2;  
          
        //imgageview设置平移，使下划线平移到初始位置（平移一个offset）  
        Matrix matrix = new Matrix();  
        matrix.postTranslate(offset, 0);  
    }  
	//执行具体的逻辑操作
@Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	// 执行初始化操作，完成该活动中的逻辑操作
	addOperationToComponent();
}
	// 向该活动中的组件添加逻辑处理
	private void addOperationToComponent() {
		// TODO Auto-generated method stub
      //向button按钮添加监听事件处理
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

		// 添加对ViewPager的操作
		if (mSelectedAreas.size() == 0) {
			EmptyWeatherInfoFragment emptyFragment = new EmptyWeatherInfoFragment();
			mSelectCityFragment.add(emptyFragment);
			mSelectAreasButton.setVisibility(View.INVISIBLE);
		} else {
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

		// 向ViewPager中添加监听器，监听滑动状态变化
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyApplication.setIndex(mCurrentIndex);
	}

	/*
	 * 当活动的启动方式为simpleTask时，如果该Activity已经存在，则getIntent接收的是上一个Activity的Intent，
	 * Intent并没有更新，即还是上一次的Intent数据。如果不存在，则会得到最新的Intent。
	 * 覆盖onNewIntent方法是为了更新Intent protected void onNewIntent(Intent intent) { //
	 * TODO Auto-generated method stub super.onNewIntent(intent);
	 * setIntent(intent); getIntent().putExtras(intent); }
	 */
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
			// 点击刷新后，重新下载当前的城市数据，并去重新绘制界面
			SharedPreferences data = getSharedPreferences(
					mSelectedAreas.get(mCurrentIndex).getSelectedCode(),
					Context.MODE_PRIVATE);
			// 先删除文件中的数据
			data.edit().clear().commit();
			showProgressDialog();
			HttpUtilForDowloadJson.getWeatherInfoFromHttp(
					mSelectedAreas.get(mCurrentIndex).getSelectedName(),
					mSelectedAreas.get(mCurrentIndex).getSelectedCode(),
					WeatherInfo.this);
			// 在while循环中判断，确保只有一次showProgress方法被执行了
			while (true) {
				if (data.getString("status", "").equals("ok")) {
					closeProgressDialog();
					break;
				}
			}
			// 获取ViewPager中的Fragment，并跟新其数据
			FragmentManager manager = this.getSupportFragmentManager();
			WeatherInfoFragment weatherInfoFragment = (WeatherInfoFragment) manager
					.findFragmentByTag("android:switcher:"
							+ R.id.weather_info_pager + ":" + mCurrentIndex);
			weatherInfoFragment.updateView(data);
			Toast.makeText(this, "刷新完成", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return true;
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
