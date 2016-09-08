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
				Toast.makeText(mContext, "�������9������", Toast.LENGTH_SHORT).show();
				arg0.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				mEditText.setSelection(tempSelection);
			}
		}
	};

	//���캯�������ڻ�ȡcontext
	public SearchSelectedFragment(Context context) {
		mContext = context;
	}

	public View onCreateView(LayoutInflater inflater,
			android.view.ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_selected_search,
				container, false);
		mDB = MyApplication.getWeatherForecastDB();
		mListArea = new ArrayList<Areas>();
		// ��������г�ʼ������
		initComponent(view);
		
		//���������Ӳ���
		addOperationToComponent();

		// ��ԃSQLite�еĔ��������@ʾ����
		queryAreas();
		return view;
	}

	// ��װ�ķ�������������г�ʼ������
	public void initComponent(View view) {
		mAreaList = (ListView) view.findViewById(R.id.list_area);

		mEditText = (EditText) view.findViewById(R.id.edit_area);

		mEmptyText = (TextView) view.findViewById(R.id.empty_text);

		mAreaList.setEmptyView(mEmptyText);
	}

	private void addOperationToComponent() {

		// ��M��EditText���ݔ��O �����O ݔ����еĠ�B׃��
		mEditText.addTextChangedListener(mMyWatcher);

		/*
		 * ��ListView����Ӳ���
		 */
		mAdapter = new ArrayAdapter<String>(mContext,
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
							Toast.makeText(mContext, "�Ѿ�������ѡ����",
									Toast.LENGTH_SHORT).show();
						}
					}
					if (!isHaveCity) {
						// ��ӵ�SQLite��
						mDB.saveSelectedAreasInfo(mSelectCityId,
								mSelectCityName, (new Date()).getTime());
					}
				}
				// ����SelectedAreasActivity���������ݴ��͸���
				Intent intent = new Intent(mContext, SelectAreasActivity.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
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
		while (true) {
			if ((mDB.loadAreas()).size() > 0) {
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
			mProgressDialog = new ProgressDialog(mContext);
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