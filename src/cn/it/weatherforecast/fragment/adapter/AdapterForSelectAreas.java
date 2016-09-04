package cn.it.weatherforecast.fragment.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.model.ModelForSelectAreas;
import cn.it.weatherforecast.util.MyApplication;

public class AdapterForSelectAreas extends BaseAdapter {

	List<ModelForSelectAreas> mSelectAreas;

	public AdapterForSelectAreas(List<ModelForSelectAreas> list) {
		mSelectAreas = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mSelectAreas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mSelectAreas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ModelForSelectAreas model = mSelectAreas.get(position);
		// 获取布局填充器的管理权
		LayoutInflater inflater = LayoutInflater.from(MyApplication
				.getContext());
		View view = inflater.inflate(R.layout.adapter_for_list_select_areas,
				parent, false);
		AreaViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new AreaViewHolder();

			viewHolder.name = (TextView) view.findViewById(R.id.area_name);
			viewHolder.temp = (TextView) view.findViewById(R.id.area_temp);
			viewHolder.weather = (TextView) view
					.findViewById(R.id.area_weather);
			// 做一个标记以便条目复用,将声明的hold存储进去
			view.setTag(viewHolder);
		} else {
			view = convertView;
			// 取出之前存储的hold
			viewHolder = (AreaViewHolder) view.getTag();
		}
		viewHolder.name.setText(model.getName());
		viewHolder.temp.setText(model.getTemp());
		viewHolder.weather.setText(model.getWeather());
		return view;
	}

}

class AreaViewHolder {
	TextView name, weather, temp;
}
