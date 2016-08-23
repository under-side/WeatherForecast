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
	
	private ProgressDialog mProgressDialog;
	
	private ArrayAdapter<String> mAdapter;
	
	private List<String> mDataList=new ArrayList<String>();
	
	private List<Areas> mListArea;
	private Areas mSelectArea;
	
	boolean result=false;
	
	//获取城市信息的URL
    private static final String URL=
    		"https://api.heweather.com/x3/citylist?search=allchina&key=e880b41d75d840d7aaaad18356139993";
	
    //为EditText设置TextWatcher，监听EditText输入的动作变化，进行操作
    private TextWatcher mMyWatcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int count, int before) {
			// TODO Auto-generated method stub
			Toast.makeText(ChooseAreasActivity.this, s, Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			
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
		
		//根据选择的Item来获取指定城市的天气信息
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
	//该方法用于查询SQLite中的城市信息，如果没有则从HTPP中获取，并将其保存到SQLite中
	protected void queryAreas() {
		// TODO Auto-generated method stub
		mDB=WeatherForecastDB.getInstance(this);
		mListArea=mDB.loadAreas();
		if(mListArea.size()<-1)
		{
			getFromServer();
		}
		else{
			mDataList.clear();
			
			for (Areas areas : mListArea) {
				mDataList.add(areas.getCityName());
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	//运用封装的HTTP服务，从网上获取城市的信息
	private void getFromServer() {
		// TODO Auto-generated method stub

		showProgressDialog();
		
		HttpUtil.sendHttpRequest(URL, new HttpCallbaceListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				result=Utility.handleResponseByJSON(mDB, response);
				closeProgressDialog();
				if(result)
				{
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

	//封装的方法，对组件进行初始化操作
	public void initComponent()
	{
		mAreaList=(ListView) findViewById(R.id.list_area);
		mEditText=(EditText) findViewById(R.id.edit_area);
	}
	
	//当开启子线程，进行耗时操作时，打开一个进度条，进行人性化设计
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
	//关闭进度条
	private void closeProgressDialog()
	{
		if(mProgressDialog!=null)
		{
			mProgressDialog.dismiss();
		}
	}
}
