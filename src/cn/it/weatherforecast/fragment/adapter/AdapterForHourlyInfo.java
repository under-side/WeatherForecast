package cn.it.weatherforecast.fragment.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.model.AdapterHourlyInfoModel;

public class AdapterForHourlyInfo extends BaseAdapter {

	List<AdapterHourlyInfoModel> mHourlyInfo;
	Context mContext;
	public AdapterForHourlyInfo(List<AdapterHourlyInfoModel> data,Context context)
	{
		mHourlyInfo=data;
		mContext=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mHourlyInfo.size();
	}

	@Override
	public Object getItem(int p) {
		// TODO Auto-generated method stub
		return mHourlyInfo.get(p);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		// TODO Auto-generated method stub
		AdapterHourlyInfoModel hourly=mHourlyInfo.get(position);
		//获取布局填充器的管理权
	    LayoutInflater inflater = LayoutInflater.from(mContext);
	    View view=inflater.inflate(R.layout.adapter_for_hourly_info, null);
	    ViewHolder viewHolder;
	    if(convertView==null){
			viewHolder=new ViewHolder();
			//获取填充布局内的组件也就本例中list_item布局中的组件，所以需要用之前
			//填充的对象名view来调用findViewById，找到的是填充布局中的组件
			viewHolder.hourly_date=(TextView) view.findViewById(R.id.hourly_date);
			viewHolder.hourly_pop=(TextView) view.findViewById(R.id.hourly_persent);
			viewHolder.hourly_dir=(TextView) view.findViewById(R.id.hourly_flow);
			viewHolder.hourly_tmp=(TextView) view.findViewById(R.id.hourly_temperature);
			//做一个标记以便条目复用,将声明的hold存储进去
			view.setTag(viewHolder);
		}else {
			view =convertView;
			//取出之前存储的hold
			viewHolder=(ViewHolder) view.getTag();
		}
	    //开始设置组件内容
	    viewHolder.hourly_date.setText(hourly.getDate());
	    viewHolder.hourly_pop.setText(hourly.getPop());
	    viewHolder.hourly_dir.setText(hourly.getDir());
	    viewHolder.hourly_tmp.setText(hourly.getTmp());
		return view;
	}
}
class ViewHolder
{
	TextView hourly_date,hourly_pop,hourly_dir,hourly_tmp;
}