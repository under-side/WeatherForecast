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
import cn.it.weatherforecast.util.ActivityCollector;
import cn.it.weatherforecast.util.MyApplication;

public class SelectAreasActivity extends Activity {

	private ListView mSelectAreasList;
	private Button mAddAreasButton;
	private BaseAdapter adapter;
	private WeatherForecastDB mDB;
	private List<ModelForSelectAreas> mSelectAreas;
	private TextView mEmptyViewText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_city);
		ActivityCollector.addActivity(this);
        mEmptyViewText=(TextView) findViewById(R.id.empty_for_selected_list);
		mSelectAreasList = (ListView) findViewById(R.id.select_city_list);
		mSelectAreasList.setEmptyView(mEmptyViewText);
		//ȡ��ListView�Ĵ�ֱ���ӗl
		mSelectAreasList.setVerticalScrollBarEnabled(false);
		mAddAreasButton = (Button) findViewById(R.id.add_city);
		mDB = MyApplication.getWeatherForecastDB();

		// ��Ӳ㼶��������
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(this) != null) {
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		addOperationForComponent();
	}

	// ���������Ӳ���
	private void addOperationForComponent() {

		// ��ť�����ת����
		mAddAreasButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SelectAreasActivity.this,
						ChooseAreasActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				//finish();
			}
		});

		// ��ListView����Ӳ���
		mSelectAreas = new ArrayList<ModelForSelectAreas>();
		
		// ����ʱȥ��ȡ���ݿ��д洢����ѡ�ĳ�����Ϣ,����������
		List<SelectedAreas> selectedAreas = mDB.loadSelectedAreas();
		for (int i=0;i<selectedAreas.size();i++) {
			SelectedAreas selectedArea=selectedAreas.get(i);
			SharedPreferences data = this.getSharedPreferences(selectedArea.getSelectedCode(),
					Context.MODE_PRIVATE);
			ModelForSelectAreas model = new ModelForSelectAreas();
			if (data.getString("status", "").equals("ok")) {
				model.setName(data.getString("basic_city", ""));
				model.setCode(data.getString("basic_id", ""));
				model.setWeather(data.getString("now_txt", ""));
				model.setTemp(data.getString("now_tmp", "") + "��");
				mSelectAreas.add(model);
			} else {
				model.setName(selectedArea.getSelectedName());
				model.setWeather("������");
				model.setTemp("N/A��");
				mSelectAreas.add(model);
			}
		}
		
		adapter = new AdapterForSelectAreas(mSelectAreas);
		mSelectAreasList.setAdapter(adapter);
		//ÿ��activity������ȡ���ݣ���ˢ����ʾListView
        adapter.notifyDataSetChanged();
        
		//����ListView��item����¼�����
		mSelectAreasList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SelectAreasActivity.this,
						WeatherInfo.class);
				i.putExtra(WeatherInfo.FROM_SELECTED_AREA, mSelectAreas.get(position).getCode());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				//finish();
			}
		});
		
		//����item����ɾ������
		mSelectAreasList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//������itemʱ������dialog�����в���
				ItemDialog dialog=new ItemDialog(SelectAreasActivity.this, mSelectAreas.get(position).getCode());
				//���ô���ȡ��dialog�еı�����
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.show();
				
				return true;
			}
		});
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
