package cn.it.weatherforecast.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.db.WeatherForecastDB;
import cn.it.weatherforecast.model.Areas;
import cn.it.weatherforecast.util.ActivityCollector;
import cn.it.weatherforecast.util.HttpCallbackListenerForJson;
import cn.it.weatherforecast.util.HttpUtilForDowloadJson;
import cn.it.weatherforecast.util.MyApplication;
import cn.it.weatherforecast.util.Utility;

public class ChooseAreasActivity extends Activity {

	private ListView mAreaList;

	private WeatherForecastDB mDB;

	private EditText mEditText;

	private TextView mEmptyText;

	private ProgressDialog mProgressDialog;

	private ArrayAdapter<String> mAdapter;

	private List<String> mDataList = new ArrayList<String>();

	private List<Areas> mListArea;

	private boolean isGetAreas = false;

	private String mSelectCityId;
	private String mSelectCityName;

	// 获取城市信息的URL
	private static final String URL = "https://api.heweather.com/x3/citylist?search=allchina&key=e880b41d75d840d7aaaad18356139993";
	// 为EditText设置TextWatcher，监听EditText输入的动作变化，进行操作
	private TextWatcher mMyWatcher = new TextWatcher() {

		CharSequence temp;
		private int editStart;
		private int editEnd;

		@Override
		public void onTextChanged(CharSequence s, int start, int count,
				int before) {
			// TODO Auto-generated method stub
			mListArea = mDB.loadAreas(s.toString());
			if (mListArea.size() != 0) {
				mDataList.clear();

				for (Areas areas : mListArea) {
					mDataList.add(areas.getCityName());
				}
				mAdapter.notifyDataSetChanged();

			} else {
				mDataList.clear();
				mAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			temp = arg0;
		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			editStart = mEditText.getSelectionStart();
			editEnd = mEditText.getSelectionEnd();

			// 此种形式，可以限制editText输入的字数个数
			if (temp.length() > 20) {
				Toast.makeText(ChooseAreasActivity.this, "最多输入9个汉字",
						Toast.LENGTH_SHORT).show();
				arg0.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				mEditText.setSelection(tempSelection);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_areas);
		ActivityCollector.addActivity(this);
		// 添加层级导航功能
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);

		}
		initComponent();
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mDataList);
		mAreaList.setAdapter(mAdapter);

		// 根据选择的Item来获取指定城市的天气信息
		mAreaList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Areas selectArea = mListArea.get(arg2);
				mSelectCityName = selectArea.getCityName();
				mSelectCityId = selectArea.getCityId();
				
				// 在子线程中下载指定的城市天气信息
				HttpUtilForDowloadJson.getWeatherInfoFromHttp(mSelectCityName,
						mSelectCityId, ChooseAreasActivity.this);
				
				mDB.saveSelectedAreaCode(mSelectCityId,mSelectCityName);
				
				// 启动SelectedAreasActivity，并将数据传送给他
				Intent intent = new Intent(ChooseAreasActivity.this,
						SelectAreasActivity.class);
				
				startActivity(intent);
				finish();
			}
		});

		mEditText.addTextChangedListener(mMyWatcher);

		queryAreas();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	// 封装的方法，对组件进行初始化操作
	public void initComponent() {
		mAreaList = (ListView) findViewById(R.id.list_area);

		mEditText = (EditText) findViewById(R.id.edit_area);

	}

	// 该方法用于查询SQLite中的城市信息，如果没有则从HTPP中获取，并将其保存到SQLite中
	protected void queryAreas() {
		// TODO Auto-generated method stub
		mDB = MyApplication.getWeatherForecastDB();
		mListArea = mDB.loadAreas();
		if (mListArea.size() == 0) {
			if (isGetAreas) {
				mAreaList.setEmptyView(mEmptyText);
			} else {
				getFromServer();
			}
		} else {
			isGetAreas = true;

			mDataList.clear();

			for (Areas areas : mListArea) {
				mDataList.add(areas.getCityName());
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	// 运用封装的HTTP服务，从网上获取城市的信息
	private void getFromServer() {
		// TODO Auto-generated method stub

		showProgressDialog();

		HttpUtilForDowloadJson.getAreasFromHttp(URL,
				new HttpCallbackListenerForJson() {
					boolean result = false;

					@Override
					public void onFinish(String response) {
						// TODO Auto-generated method stub
						result = Utility.handleResponseByJSON(mDB, response);
						closeProgressDialog();
						if (result) {
							isGetAreas = true;
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									queryAreas();
								}
							});
						}
					}

					@Override
					public void onError(Exception e) {
						// TODO Auto-generated method stub
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								closeProgressDialog();
								Toast.makeText(ChooseAreasActivity.this,
										"Loading error", Toast.LENGTH_SHORT)
										.show();
							}
						});
						e.printStackTrace();
					}
				});
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
