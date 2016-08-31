package cn.it.weatherforecast.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class InnerListView extends ListView {

	public InnerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InnerListView(Context context) {
		super(context);
	}

	public InnerListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ����ָ����listview��ʱ���ø�ScrollView����ontouchȨ�ޣ�Ҳ�����ø�scrollview ͣס���ܹ���
			setParentScrollAble(false);

			break;
		case MotionEvent.ACTION_CANCEL:
			// ����ָ�ɿ�ʱ���ø�ScrollView�����õ�onTouchȨ��
			setParentScrollAble(true);
			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);

	}

	/**
	 * �Ƿ�ѹ����¼�������scrollview
	 */
	private void setParentScrollAble(boolean flag) {
		// �����parentScrollView����listview������Ǹ�scrollview
		getParent().getParent().requestDisallowInterceptTouchEvent(!flag);
	}
}
