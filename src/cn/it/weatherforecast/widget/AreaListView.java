package cn.it.weatherforecast.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

//����Ϊ�Զ���ListView����������Listview�ս���
public class AreaListView extends ListView {

	public AreaListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AreaListView(Context context) {
		super(context);
	}

	public AreaListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setEmptyView(View emptyView) {
		// TODO Auto-generated method stub
	}

}
