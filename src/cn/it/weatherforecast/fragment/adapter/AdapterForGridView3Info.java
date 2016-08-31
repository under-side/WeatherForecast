package cn.it.weatherforecast.fragment.adapter;

import java.util.ArrayList;

import cn.it.weatherforecast.R;
import cn.it.weatherforecast.model.ModelForGrid2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterForGridView3Info extends BaseAdapter {

	private Context mContext;
	private ArrayList<ModelForGrid2> mGrid3List;
	
	public AdapterForGridView3Info(Context context,ArrayList<ModelForGrid2> list)
	{
		mContext=context;
		mGrid3List=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mGrid3List.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mGrid3List.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		// TODO Auto-generated method stub
		ModelForGrid2 grid3=mGrid3List.get(position);
		//获取布局填充器的管理权
	    LayoutInflater inflater = LayoutInflater.from(mContext);
	    View view=inflater.inflate(R.layout.adapter_for_gridview2, null);
	    Grid2ViewHolder viewHolder;
	    if(convertView==null)
	    {
	    	viewHolder=new Grid2ViewHolder();
	    	viewHolder.image=(ImageView) view.findViewById(R.id.grid2_image);
	    	
	    	viewHolder.text=(TextView) view.findViewById(R.id.grid2_text);
	    	viewHolder.text_detail=(TextView) view.findViewById(R.id.grid2_text_detail);
	    	
	    	//做一个标记以便条目复用,将声明的hold存储进去
			view.setTag(viewHolder);
	    }else {
			view =convertView;
			//取出之前存储的hold
			viewHolder=(Grid2ViewHolder) view.getTag();
		}
	    viewHolder.image.setImageResource(grid3.getId());
	    viewHolder.text.setText(grid3.getName());
	    viewHolder.text_detail.setText(grid3.getName_txt());
		return view;
	}
}
