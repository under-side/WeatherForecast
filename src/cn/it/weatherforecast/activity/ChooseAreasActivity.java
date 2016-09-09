package cn.it.weatherforecast.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.fragment.HotCitySelectedFragment;
import cn.it.weatherforecast.fragment.SearchSelectedFragment;

/*
 * ����ʵ�ֵĹ��ܣ�
 * 1����ʾ���ų����б�����г����б�
 * 2���������itemʱ��������ѡ������Ϣ��SQLite��
 * 3����ת��SelectedAreasActivity��
 */
public class ChooseAreasActivity extends FragmentActivity {

	//����ȫ�ֱ���
	private FragmentManager mFragmentManager;
	private Fragment mHotCityFragment;
	private Fragment mSearchCityFragment;
	private Button mSearchButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_areas);
		// ��Ӳ㼶��������
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);

		}
		//��ȡ��ťʵ��������Ӱ�ť�������
		mSearchButton=(Button) findViewById(R.id.switch_search_button);
		mSearchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//�������ťʱ����������ų����л�����������
				mFragmentManager.beginTransaction()
				.replace(R.id.list_areas_container, mSearchCityFragment)
				.commit();
				mSearchButton.setVisibility(View.INVISIBLE);
			}
		});
        //��ȡFragmentManagerʵ���������������fragment
		mFragmentManager = getSupportFragmentManager();
		mHotCityFragment = mFragmentManager
				.findFragmentById(R.id.list_areas_container);
		mSearchCityFragment = mFragmentManager
				.findFragmentById(R.id.list_areas_container);

		if (mHotCityFragment == null) {
			mHotCityFragment = new HotCitySelectedFragment(
					ChooseAreasActivity.this);
			mFragmentManager.beginTransaction()
					.add(R.id.list_areas_container, mHotCityFragment).commit();
		}
		if (mSearchCityFragment == null) {
			mSearchCityFragment = new SearchSelectedFragment(
					ChooseAreasActivity.this);
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
