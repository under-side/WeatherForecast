package cn.it.weatherforecast.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.fragment.HotCitySelectedFragment;
import cn.it.weatherforecast.fragment.SearchSelectedFragment;

/*
 * 该类实现的功能：
 * 1，显示热门城市列表或所有城市列表
 * 2，点击城市item时，保存所选城市信息到SQLite中
 * 3，跳转到SelectedAreasActivity中
 */
public class ChooseAreasActivity extends FragmentActivity {

	//定义全局变量
	private FragmentManager mFragmentManager;
	private Fragment mHotCityFragment;
	private Fragment mSearchCityFragment;
	private Button mSearchButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_areas);
		// 添加层级导航功能
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);

		}
		//获取按钮实例，并添加按钮点击处理
		mSearchButton=(Button) findViewById(R.id.switch_search_button);
		mSearchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//当点击按钮时，将会从热门城市切换到搜索界面
				mFragmentManager.beginTransaction()
				.replace(R.id.list_areas_container, mSearchCityFragment)
				.commit();
				mSearchButton.setVisibility(View.INVISIBLE);
			}
		});
        //获取FragmentManager实例，并向其中添加fragment
		mFragmentManager = getSupportFragmentManager();
		mHotCityFragment = mFragmentManager
				.findFragmentById(R.id.list_areas_container);
		mSearchCityFragment = mFragmentManager
				.findFragmentById(R.id.list_areas_container);

		if (mHotCityFragment == null) {
			mHotCityFragment = new HotCitySelectedFragment(
					ChooseAreasActivity.this);
			mFragmentManager.beginTransaction()
					.add(R.id.list_areas_container, mHotCityFragment).commit();
		}
		if (mSearchCityFragment == null) {
			mSearchCityFragment = new SearchSelectedFragment(
					ChooseAreasActivity.this);
		}
	}

	// 添加导航栏中的逻辑操作
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(this) != null) {
				NavUtils.navigateUpFromSameTask(this);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
