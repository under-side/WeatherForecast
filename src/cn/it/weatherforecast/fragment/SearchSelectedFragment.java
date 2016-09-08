package cn.it.weatherforecast.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.activity.SelectAreasActivity;
import cn.it.weatherforecast.db.WeatherForecastDB;
import cn.it.weatherforecast.model.Areas;
import cn.it.weatherforecast.model.SelectedAreas;
import cn.it.weatherforecast.util.MyApplication;

public class SearchSelectedFragment extends Fragment {
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

	private boolean isHaveCity = false;

	private Context mContext;

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
				Toast.makeText(mContext, "最多输入9个汉字", Toast.LENGTH_SHORT).show();
				arg0.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				mEditText.setSelection(tempSelection);
			}
		}
	};

	//构造函数，用于获取context
	public SearchSelectedFragment(Context context) {
		mContext = context;
	}

	public View onCreateView(LayoutInflater inflater,
			android.view.ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_selected_search,
				container, false);
		mDB = MyApplication.getWeatherForecastDB();
		mListArea = new ArrayList<Areas>();
		// 对组件进行初始化操作
		initComponent(view);
		
		//向组件中添加操作
		addOperationToComponent();

		// 查詢SQLite中的數據，并顯示出來
		queryAreas();
		return view;
	}

	// 封装的方法，对组件进行初始化操作
	public void initComponent(View view) {
		mAreaList = (ListView) view.findViewById(R.id.list_area);

		mEditText = (EditText) view.findViewById(R.id.edit_area);

		mEmptyText = (TextView) view.findViewById(R.id.empty_text);

		mAreaList.setEmptyView(mEmptyText);
	}

	private void addOperationToComponent() {

		// 向組件EditText添加輸入監聽器，監聽輸入欄中的狀態變化
		mEditText.addTextChangedListener(mMyWatcher);

		/*
		 * 向ListView中添加操作
		 */
		mAdapter = new ArrayAdapter<String>(mContext,
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

				List<SelectedAreas> areas = mDB.loadSelectedAreas();
				/*
				 * 将点击所选的并且在SQLite中没有的item的信息存放在SQLite中，标识为所选城市。
				 * 加上这种判断，避免了重复点击已经选择的城市，导致重复加载。
				 */
				if (areas.size() == 0) {
					// 添加到SQLite中
					mDB.saveSelectedAreasInfo(mSelectCityId, mSelectCityName,
							(new Date()).getTime());
				} else {
					for (SelectedAreas selectedAreas : areas) {
						if (selectedAreas.getSelectedCode().equals(
								mSelectCityId)) {
							isHaveCity = true;
							Toast.makeText(mContext, "已经存在所选城市",
									Toast.LENGTH_SHORT).show();
						}
					}
					if (!isHaveCity) {
						// 添加到SQLite中
						mDB.saveSelectedAreasInfo(mSelectCityId,
								mSelectCityName, (new Date()).getTime());
					}
				}
				// 启动SelectedAreasActivity，并将数据传送给他
				Intent intent = new Intent(mContext, SelectAreasActivity.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}

	// 该方法用于查询SQLite中的城市信息，如果没有则从HTPP中获取，并将其保存到SQLite中
	protected void queryAreas() {
		// TODO Auto-generated method stub
		/*
		 * 因为城市数据是通过后台服务进行下载的，为了避免数据没有存入SQLite中用户又跳转到城市列表activity
		 * 中出现混乱，则通过一个while循环去判断是否存入SQLite中去。如果没有存入中，则将会出现一个ProgressDialog
		 * 提示数据正在下载中，当有数据了，则开始获取数据，并显示在ListView中。
		 */
		showProgressDialog();
		while (true) {
			if ((mDB.loadAreas()).size() > 0) {
				closeProgressDialog();
				break;
			}
		}
		mListArea = mDB.loadAreas();

		// 获取数据并赋值给ListView中的adapter
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
			mProgressDialog = new ProgressDialog(mContext);
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