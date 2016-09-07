package cn.it.weatherforecast.activity;

import java.util.ArrayList;
import java.util.Date;
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

	private boolean isHaveCity = false;

	// ΪEditText����TextWatcher������EditText����Ķ����仯�����в���
	private TextWatcher mMyWatcher = new TextWatcher() {

		CharSequence temp;
		private int editStart;
		private int editEnd;

		// ����EditText�е�����״̬��������ʵʱ�������Ĺ���
		@Override
		public void onTextChanged(CharSequence s, int start, int count,
				int before) {
			// TODO Auto-generated method stub
			// �����������������SQLite�н���ģ����ѯ��������ʾ����
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

		// �÷��������ж��������ֵĸ����������������������
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			editStart = mEditText.getSelectionStart();
			editEnd = mEditText.getSelectionEnd();

			// ������ʽ����������editText�������������
			if (temp.length() > 20) {
				Toast.makeText(ChooseAreasActivity.this, "�������9������",
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
		mDB=MyApplication.getWeatherForecastDB();
		mListArea=new ArrayList<Areas>();
		ActivityCollector.addActivity(this);
		// ��Ӳ㼶��������
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);

		}

		// ��������г�ʼ������
		initComponent();

		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mDataList);
		mAreaList.setAdapter(mAdapter);

		// ����ѡ���Item����ȡָ�����е�������Ϣ
		mAreaList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Areas selectArea = mListArea.get(arg2);
				mSelectCityName = selectArea.getCityName();
				mSelectCityId = selectArea.getCityId();

				List<SelectedAreas> areas = mDB.loadSelectedAreas();
				/*
				 * �������ѡ�Ĳ�����SQLite��û�е�item����Ϣ�����SQLite�У���ʶΪ��ѡ���С�
				 * ���������жϣ��������ظ�����Ѿ�ѡ��ĳ��У������ظ����ء�
				 */
				if (areas.size() == 0) {
					// ��ӵ�SQLite��
					mDB.saveSelectedAreasInfo(mSelectCityId, mSelectCityName,
							(new Date()).getTime());
				} else {
					for (SelectedAreas selectedAreas : areas) {
						if (selectedAreas.getSelectedCode().equals(
								mSelectCityId)) {
							isHaveCity = true;
							Toast.makeText(ChooseAreasActivity.this,
									"�Ѿ�������ѡ����", Toast.LENGTH_SHORT).show();
						}
					}
					if (!isHaveCity) {
						// ��ӵ�SQLite��
						mDB.saveSelectedAreasInfo(mSelectCityId,
								mSelectCityName, (new Date()).getTime());
					}
				}
				// ����SelectedAreasActivity���������ݴ��͸���
				Intent intent = new Intent(ChooseAreasActivity.this,
						SelectAreasActivity.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});

		// ��M��EditText���ݔ��O �����O ݔ����еĠ�B׃��
		mEditText.addTextChangedListener(mMyWatcher);
		// ��ԃSQLite�еĔ��������@ʾ����
		queryAreas();
	}

	// ��װ�ķ�������������г�ʼ������
	public void initComponent() {
		mAreaList = (ListView) findViewById(R.id.list_area);

		mEditText = (EditText) findViewById(R.id.edit_area);

		mEmptyText = (TextView) findViewById(R.id.empty_text);

		mAreaList.setEmptyView(mEmptyText);
	}

	// �÷������ڲ�ѯSQLite�еĳ�����Ϣ�����û�����HTPP�л�ȡ�������䱣�浽SQLite��
	protected void queryAreas() {
		// TODO Auto-generated method stub
		/*
		 * ��Ϊ����������ͨ����̨����������صģ�Ϊ�˱�������û�д���SQLite���û�����ת�������б�activity
		 * �г��ֻ��ң���ͨ��һ��whileѭ��ȥ�ж��Ƿ����SQLite��ȥ�����û�д����У��򽫻����һ��ProgressDialog
		 * ��ʾ�������������У����������ˣ���ʼ��ȡ���ݣ�����ʾ��ListView�С�
		 */
		showProgressDialog();
		while(true)
		{
			if ((mDB.loadAreas()).size()> 0) {
				closeProgressDialog();
				break;
			}
		}
		mListArea = mDB.loadAreas();
		
		// ��ȡ���ݲ���ֵ��ListView�е�adapter
		mDataList.clear();

		for (Areas areas : mListArea) {
			mDataList.add(areas.getCityName());
		}
		mAdapter.notifyDataSetChanged();
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

	// ��ӵ������е��߼�����
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
