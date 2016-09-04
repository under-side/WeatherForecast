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
		//��ȡ����������Ĺ���Ȩ
	    LayoutInflater inflater = LayoutInflater.from(mContext);
	    View view=inflater.inflate(R.layout.adapter_for_hourly_info, null);
	    ViewHolder viewHolder;
	    if(convertView==null){
			viewHolder=new ViewHolder();
			//��ȡ��䲼���ڵ����Ҳ�ͱ�����list_item�����е������������Ҫ��֮ǰ
			//���Ķ�����view������findViewById���ҵ�������䲼���е����
			viewHolder.hourly_date=(TextView) view.findViewById(R.id.hourly_date);
			viewHolder.hourly_pop=(TextView) view.findViewById(R.id.hourly_persent);
			viewHolder.hourly_dir=(TextView) view.findViewById(R.id.hourly_flow);
			viewHolder.hourly_tmp=(TextView) view.findViewById(R.id.hourly_temperature);
			//��һ������Ա���Ŀ����,��������hold�洢��ȥ
			view.setTag(viewHolder);
		}else {
			view =convertView;
			//ȡ��֮ǰ�洢��hold
			viewHolder=(ViewHolder) view.getTag();
		}
	    //��ʼ�����������
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