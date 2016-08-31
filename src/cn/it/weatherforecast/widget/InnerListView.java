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
			// 当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview 停住不能滚动
			setParentScrollAble(false);

			break;
		case MotionEvent.ACTION_CANCEL:
			// 当手指松开时，让父ScrollView重新拿到onTouch权限
			setParentScrollAble(true);
			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);

	}

	/**
	 * 是否把滚动事件交给父scrollview
	 */
	private void setParentScrollAble(boolean flag) {
		// 这里的parentScrollView就是listview外面的那个scrollview
		getParent().getParent().requestDisallowInterceptTouchEvent(!flag);
	}
}
