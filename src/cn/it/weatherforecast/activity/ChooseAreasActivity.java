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
import cn.it.weatherforecast.model.SelectedAreas;
import cn.it.weatherforecast.util.ActivityCollector;
import cn.it.weatherforecast.util.HttpUtilForDowloadJson;
import cn.it.weatherforecast.util.MyApplication;

public class ChooseAreasActivity extends Activity {

	private ListView mAreaList;

	private WeatherForecastDB mDB;

	private EditText mEditText;

	private TextView mEmptyText;

	private ProgressDialog mProgressDialog;

	private ArrayAdapter<String> mAdapter;

	private List<String> mDataList = new ArrayList<String>();

	private List<Areas> mListArea;

	private String mSelectCityId;

	private String mSelectCityName;
	
	private boolean isHaveCity=false;

	// 为EditText设置TextWatcher，监听EditText输入的动作变化，进行操作
	private TextWatcher mMyWatcher = new TextWatcher() {

		CharSequence temp;
		private int editStart;
		private int editEnd;

		// 监听EditText中的输入状态，来进行实时的搜索的功能
		@Override
		public void onTextChanged(CharSequence s, int start, int count,
				int before) {
			// TODO Auto-generated method stub
			// 根据输入的数字来在SQLite中进行模糊查询操作，显示出来
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

		// 该方法用于判断输入数字的个数，进行限制输入的字数
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

		// 对组件进行初始化操作
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
				List<SelectedAreas> areas = mDB.loadSelectedAreas();
				/*
				 * 将点击所选的并且在SQLite中没有的item的信息存放在SQLite中，标识为所选城市。
				 * 加上这种判断，避免了重复点击已经选择的城市，导致重复加载。
				 */
				if (areas.size() == 0) {
					// 添加到SQLite中
					mDB.saveSelectedAreaCode(mSelectCityId, mSelectCityName);
					Toast.makeText(ChooseAreasActivity.this,
							mDB.loadSelectedAreas().size(), Toast.LENGTH_LONG)
							.show();
				} else {
					for (SelectedAreas selectedAreas : areas) {
						if (selectedAreas.getSelectedCode().equals(
								mSelectCityId)) {
							isHaveCity=true;
							Toast.makeText(ChooseAreasActivity.this,
									"已经存在所选城市", Toast.LENGTH_SHORT).show();
						} 
					}
					if(!isHaveCity)
					{
						// 添加到SQLite中
						mDB.saveSelectedAreaCode(mSelectCityId,
								mSelectCityName);
					}
				}
				// 启动SelectedAreasActivity，并将数据传送给他
				Intent intent = new Intent(ChooseAreasActivity.this,
						SelectAreasActivity.class);
           
			    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				// 当该活动去开启另一个活动时，调用finish方法，使其自结束，避免了开启多个activity
				//finish();
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

		mEmptyText = (TextView) findViewById(R.id.empty_text);

		mAreaList.setEmptyView(mEmptyText);
	}

	// 该方法用于查询SQLite中的城市信息，如果没有则从HTPP中获取，并将其保存到SQLite中
	protected void queryAreas() {
		// TODO Auto-generated method stub
		mDB = MyApplication.getWeatherForecastDB();
		/*
		 * 因为城市数据是通过后台服务进行下载的，为了避免数据没有存入SQLite中用户又跳转到城市列表activity
		 * 中出现混乱，则通过一个while循环去判断是否存入SQLite中去。如果没有存入中，则将会出现一个ProgressDialog
		 * 提示数据正在下载中，当有数据了，则开始获取数据，并显示在ListView中。
		 */
		while ((mDB.loadAreas()).size() == 0) {
			showProgressDialog();
		}
		closeProgressDialog();
		mListArea = mDB.loadAreas();

		mDataList.clear();

		for (Areas areas : mListArea) {
			mDataList.add(areas.getCityName());
		}
		mAdapter.notifyDataSetChanged();
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
