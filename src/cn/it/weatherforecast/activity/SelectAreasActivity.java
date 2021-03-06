package cn.it.weatherforecast.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
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
import android.widget.TextView;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.db.WeatherForecastDB;
import cn.it.weatherforecast.fragment.adapter.AdapterForSelectAreas;
import cn.it.weatherforecast.model.ModelForSelectAreas;
import cn.it.weatherforecast.model.SelectedAreas;
import cn.it.weatherforecast.util.BDLocationClient;
import cn.it.weatherforecast.util.MyApplication;

import com.baidu.location.LocationClient;
/*
 * 该类实现的功能：
 * 1，显示SQLite中存储的选择的城市信息
 * 2，点击跳转到WeatherInfoActivity中
 * 3，长按item实现弹窗，执行查看和删除指定的城市功能
 * 4，实现百度定位服务
 */
public class SelectAreasActivity extends Activity {

	private ListView mSelectAreasList;
	private Button mAddAreasButton;
	private BaseAdapter adapter;
	private WeatherForecastDB mDB;
	//模型实体类链表
	private List<SelectedAreas> mSelectedAreas;
	private List<ModelForSelectAreas> mModelSelectedAreas;
	
	//ListView的空View视图
	private TextView mEmptyViewText;

	//运用百度定位服务实现定位功能
	private LocationClient mBDLocation;
	
	//该变量用于判断是否没有选择的fragment，如果是则弹出一个对话框，提醒用户右上角的定位按钮
	private boolean isNullFragment;
	AlertDialog dialog=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_city);
		mEmptyViewText = (TextView) findViewById(R.id.empty_for_selected_list);
		mSelectAreasList = (ListView) findViewById(R.id.select_city_list);
		mSelectAreasList.setEmptyView(mEmptyViewText);
		// 取消ListView的垂直滑動條
		mSelectAreasList.setVerticalScrollBarEnabled(false);
		mAddAreasButton = (Button) findViewById(R.id.add_city);
		mDB = MyApplication.getWeatherForecastDB();

		// 添加层级导航功能
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(this) != null) {
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
        
		/*
		 * 制定弹窗，提醒用户右上角的定位功能。
		 * 此功能只会在SQLite中没有已经选择城市列表时触发。
		 */
		isNullFragment=getIntent().getBooleanExtra("isNullFragment", false);
		if(isNullFragment)
		{
			AlertDialog.Builder builder=new AlertDialog.Builder(SelectAreasActivity.this);
			builder
			.setMessage("点击右上角，可实现定位。")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			dialog=builder.create();
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
		}
		
		//向该activity中的组件添加处理用户点击逻辑
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
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				// finish();
			}
		});

		// 向ListView中添加操作
		mModelSelectedAreas = new ArrayList<ModelForSelectAreas>();

		// 启动时去获取数据库中存储的所选的城市信息,并加载数据
		mSelectedAreas = mDB.loadSelectedAreas();
		for (int i = 0; i < mSelectedAreas.size(); i++) {
			SelectedAreas selectedArea = mSelectedAreas.get(i);
			SharedPreferences data = this.getSharedPreferences(
					selectedArea.getSelectedCode(), Context.MODE_PRIVATE);
			ModelForSelectAreas model = new ModelForSelectAreas();
			if (data.getString("status", "").equals("ok")) {
				model.setName(data.getString("basic_city", ""));
				model.setCode(data.getString("basic_id", ""));
				model.setWeather(data.getString("now_txt", ""));
				model.setTemp(data.getString("now_tmp", "") + "°");
				mModelSelectedAreas.add(model);
			} else {
				model.setName(selectedArea.getSelectedName());
				model.setWeather("无数据");
				model.setTemp("N/A°");
				mModelSelectedAreas.add(model);
			}
		}

		adapter = new AdapterForSelectAreas(mModelSelectedAreas);
		mSelectAreasList.setAdapter(adapter);
		// 每次activity创建获取数据，并刷新显示ListView
		adapter.notifyDataSetChanged();

		// 设置ListView的item点击事件处理
		mSelectAreasList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SelectAreasActivity.this,
						WeatherInfo.class);
				i.putExtra(WeatherInfo.FROM_SELECTED_AREA,
						mSelectedAreas.get(position).getSelectedCode());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				// finish();
			}
		});

		// 设置item长按删除处理
		mSelectAreasList
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						// 当长按item时，弹出dialog，进行操作
						ItemDialog dialog = new ItemDialog(
								SelectAreasActivity.this, mModelSelectedAreas
										.get(position).getCode());
						// 运用代码取消dialog中的标题栏
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.show();

						return true;
					}
				});
	}

	// 得到自定义的菜单视图
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_selected_areas, menu);
		return true;
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
		case R.id.menu_item_location_city:
			mBDLocation = BDLocationClient.getLocatinClientInstance();
			mBDLocation.start();
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mBDLocation != null) {
			mBDLocation.stop();
		}
	}
}
