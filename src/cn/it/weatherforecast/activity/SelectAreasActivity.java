package cn.it.weatherforecast.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.db.WeatherForecastDB;
import cn.it.weatherforecast.fragment.adapter.AdapterForSelectAreas;
import cn.it.weatherforecast.model.ModelForSelectAreas;
import cn.it.weatherforecast.model.SelectedAreas;
import cn.it.weatherforecast.util.ActivityCollector;
import cn.it.weatherforecast.util.MyApplication;

public class SelectAreasActivity extends Activity {

	private ListView mSelectAreasList;
	private String mSelectId;
	private Button mAddAreasButton;
	private BaseAdapter adapter;
	private WeatherForecastDB mDB;
	private List<ModelForSelectAreas> mSelectAreas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("1", "onCreate call");
		setContentView(R.layout.activity_list_city);
		ActivityCollector.addActivity(this);

		// 添加层级导航功能
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);

		}

		mSelectAreasList = (ListView) findViewById(R.id.select_city_list);
		mAddAreasButton = (Button) findViewById(R.id.add_city);
		mDB = MyApplication.getWeatherForecastDB();

		// 添加层级导航功能
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(this) != null) {
				getActionBar().setDisplayHomeAsUpEnabled(true);
				// NavUtils.navigateUpFromSameTask(this);
			}
		}

		addOperationForComponent();
	}

	// 向组件中添加操作
	private void addOperationForComponent() {

		// 向按钮添加跳转操作
		mAddAreasButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SelectAreasActivity.this,
						ChooseAreasActivity.class);
				startActivityForResult(intent, 0);
				finish();
			}
		});

		// 向ListView中添加操作
		mSelectAreas = new ArrayList<ModelForSelectAreas>();
		
		// 启动时去获取数据库中存储的所选的城市信息,并加载数据
		List<SelectedAreas> selectedAreas = mDB.loadSelectedAreas();
		for (int i=0;i<selectedAreas.size();i++) {
			SelectedAreas selectedArea=selectedAreas.get(i);
			SharedPreferences data = this.getSharedPreferences(selectedArea.getSelectedCode(),
					Context.MODE_PRIVATE);
			ModelForSelectAreas model = new ModelForSelectAreas();
			if (data.getString("status", "").equals("ok")) {
				model.setName(data.getString("basic_city", ""));
				model.setWeather(data.getString("now_txt", ""));
				model.setTemp(data.getString("now_tmp", "") + "°");
				mSelectAreas.add(model);
			} else {
				model.setName(selectedArea.getSelectedName());
				model.setWeather("无数据");
				model.setTemp("N/A°");
				mSelectAreas.add(model);
			}
		}
		adapter = new AdapterForSelectAreas(mSelectAreas);
		mSelectAreasList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
		//设置ListView的item点击事件处理
		mSelectAreasList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SelectAreasActivity.this,
						WeatherInfo.class);
				i.putExtra(WeatherInfo.FROM_SELECTED_AREA, mSelectId);
				startActivity(i);
			}
		});
		
		//设置item长按删除处理
		mSelectAreasList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//当长按item时，弹出dialog，进行操作
				ItemDialog dialog=new ItemDialog(SelectAreasActivity.this, mSelectId);
				//运用代码取出标题栏
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.show();
				//Toast.makeText(SelectAreasActivity.this, "press long time"+position, Toast.LENGTH_LONG).show();
				return true;
			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub

		super.onNewIntent(intent);
		setIntent(intent);
		getIntent().putExtras(intent);
	}


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
