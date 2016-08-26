package cn.it.weatherforecast.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import cn.it.weatherforecast.util.HttpCallbaceListener;
import cn.it.weatherforecast.util.HttpUtil;
import cn.it.weatherforecast.util.LogUtil;
import cn.it.weatherforecast.util.Utility;

public class ChooseAreasActivity extends Activity {

	private ListView mAreaList;
	
	private WeatherForecastDB mDB;
	
	private EditText mEditText;
	
	private TextView mEmptyText;
	
	private ProgressDialog mProgressDialog;
	
	private ArrayAdapter<String> mAdapter;
	
	private List<String> mDataList=new ArrayList<String>();
	
	private List<Areas> mListArea;
	
	private Areas mSelectArea;
	
	private boolean isGetAreas=false;
	
	
	//��ȡ������Ϣ��URL
    private static final String URL=
    		"https://api.heweather.com/x3/citylist?search=allchina&key=e880b41d75d840d7aaaad18356139993";
	
    //ΪEditText����TextWatcher������EditText����Ķ����仯�����в���
    private TextWatcher mMyWatcher=new TextWatcher() {
		
    	CharSequence temp;
    	private int editStart ;
        private int editEnd ;
		@Override
		public void onTextChanged(CharSequence s, int start, int count, int before) {
			// TODO Auto-generated method stub
			mListArea=mDB.loadAreas(s.toString());
			if(mListArea.size()!=0)
			{
				mDataList.clear();
				
				for (Areas areas : mListArea) {
					mDataList.add(areas.getCityName());
				}
				mAdapter.notifyDataSetChanged();
				
			}
			else{
			     mDataList.clear();
			     mAdapter.notifyDataSetChanged();
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			temp=arg0;
		}
		
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			editStart=mEditText.getSelectionStart();
			editEnd=mEditText.getSelectionEnd();
			
			//������ʽ����������editText�������������
			if(temp.length()>20)
			{
				Toast.makeText(ChooseAreasActivity.this, "�������9������", Toast.LENGTH_SHORT).show();
				arg0.delete(editStart-1, editEnd);
				int tempSelection=editStart;
				mEditText.setSelection(tempSelection);
			}
		}
	};
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_areas);
		LogUtil.LEVEL=LogUtil.DEBUG;
		initComponent();
		mAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mDataList);
		mAreaList.setAdapter(mAdapter);
		
		//����ѡ���Item����ȡָ�����е�������Ϣ
		mAreaList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				mSelectArea=mListArea.get(arg2);
			}
		});
		
		mEditText.addTextChangedListener(mMyWatcher);
		
		
		queryAreas();
	}
	
	//��װ�ķ�������������г�ʼ������
	public void initComponent()
	{
		mAreaList=(ListView) findViewById(R.id.list_area);
		
		mAreaList.setEmptyView(mEmptyText);
		
		mEditText=(EditText) findViewById(R.id.edit_area);
		
	}
	
	//�÷������ڲ�ѯSQLite�еĳ�����Ϣ�����û�����HTPP�л�ȡ�������䱣�浽SQLite��
	protected void queryAreas() {
		// TODO Auto-generated method stub
		mDB=WeatherForecastDB.getInstance(this);
		mListArea=mDB.loadAreas();
		if(mListArea.size()==0)
		{
			if(isGetAreas)
			{
				mAreaList.setEmptyView(mEmptyText);
			}
			else
			{
				getFromServer();
			}
		}
		else{
			isGetAreas=true;
			
			mDataList.clear();
			
			for (Areas areas : mListArea) {
				mDataList.add(areas.getCityName());
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	//���÷�װ��HTTP���񣬴����ϻ�ȡ���е���Ϣ
	private void getFromServer() {
		// TODO Auto-generated method stub
		
		showProgressDialog();
		
		HttpUtil.sendHttpRequest(URL, new HttpCallbaceListener() {
			boolean result=false;
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				result=Utility.handleResponseByJSON(mDB, response);
				closeProgressDialog();
				if(result)
				{
					isGetAreas=true;
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
						Toast.makeText(ChooseAreasActivity.this, "Loading error", Toast.LENGTH_SHORT).show();
					}
				});
                e.printStackTrace();
			}
		});
	}


	
	//���������̣߳����к�ʱ����ʱ����һ�����������������Ի����
	private void showProgressDialog() {
		// TODO Auto-generated method stub
		if(mProgressDialog==null)
		{
			mProgressDialog=new ProgressDialog(this);
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		mProgressDialog.show();
	}
	//�رս�����
	private void closeProgressDialog()
	{
		if(mProgressDialog!=null)
		{
			mProgressDialog.dismiss();
		}
	}
}
