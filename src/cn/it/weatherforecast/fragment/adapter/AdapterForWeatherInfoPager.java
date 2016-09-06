package cn.it.weatherforecast.fragment.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdapterForWeatherInfoPager extends FragmentPagerAdapter {

	List<Fragment> mSelectedCityFragment;

	public AdapterForWeatherInfoPager(FragmentManager fm, List<Fragment> list) {
		super(fm);
		mSelectedCityFragment = list;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return mSelectedCityFragment.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mSelectedCityFragment.size();
	}

}
