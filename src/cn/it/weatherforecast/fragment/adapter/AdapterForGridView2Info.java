package cn.it.weatherforecast.fragment.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.model.ModelForGrid2;

public class AdapterForGridView2Info extends BaseAdapter {

	private Context mContext;
	private ArrayList<ModelForGrid2> mGrid2Info;
	
	public AdapterForGridView2Info(Context context,ArrayList<ModelForGrid2> gridList) {
		mContext=context;
		mGrid2Info=gridList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mGrid2Info.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mGrid2Info.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		// TODO Auto-generated method stub
		
		ModelForGrid2 grid2=mGrid2Info.get(position);
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
	    viewHolder.image.setImageResource(grid2.getId());
	    viewHolder.text.setText(grid2.getName());
	    viewHolder.text_detail.setText(grid2.getName_txt());
		return view;
	}

}
class Grid2ViewHolder
{
	ImageView image;
	TextView text,text_detail;
}
