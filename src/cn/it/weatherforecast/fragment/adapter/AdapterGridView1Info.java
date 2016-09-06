package cn.it.weatherforecast.fragment.adapter;

import java.util.ArrayList;

import cn.it.weatherforecast.R;
import cn.it.weatherforecast.model.ModelForGrid1;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterGridView1Info extends BaseAdapter {

	private Context mContext;
	private ArrayList<ModelForGrid1> mGridView1List;

	public AdapterGridView1Info(Context context,
			ArrayList<ModelForGrid1> grid1List) {

		mContext = context;
		mGridView1List = grid1List;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mGridView1List.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mGridView1List.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		// TODO Auto-generated method stub
		ModelForGrid1 grid1 = mGridView1List.get(position);
		// 获取布局填充器的管理权
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.adapter_for_gridview1, null);
		Grid1ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new Grid1ViewHolder();
			viewHolder.image = (ImageView) view.findViewById(R.id.grid1_image1);

			viewHolder.text1 = (TextView) view
					.findViewById(R.id.gird1_text1_text);
			viewHolder.text1_detail = (TextView) view
					.findViewById(R.id.grid1_text1_detail);
			viewHolder.text2 = (TextView) view
					.findViewById(R.id.grid1_text2_text);
			viewHolder.text2_detail = (TextView) view
					.findViewById(R.id.grid1_text2_detail);

			// 做一个标记以便条目复用,将声明的hold存储进去
			view.setTag(viewHolder);
		} else {
			view = convertView;
			// 取出之前存储的hold
			viewHolder = (Grid1ViewHolder) view.getTag();
		}
		viewHolder.image.setImageResource(grid1.getId());
		viewHolder.text1.setText(grid1.getName1());
		viewHolder.text1_detail.setText(grid1.getName1_txt());
		viewHolder.text2.setText(grid1.getName2());
		viewHolder.text2_detail.setText(grid1.getName2_txt());
		return view;
	}

}

class Grid1ViewHolder {
	ImageView image;
	TextView text1, text1_detail, text2, text2_detail;
}
