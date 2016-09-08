package cn.it.weatherforecast.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.model.ModelForGrid2;

public class SuggestionDetailActivity extends Activity {

	private ImageView mSuggestionImage;
	private TextView mSuggestionTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_for_suggestion_detail);
		// 添加层级导航功能
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);

		}

		ModelForGrid2 model = (ModelForGrid2) getIntent().getSerializableExtra(
				"ClickItem");

		mSuggestionImage = (ImageView) findViewById(R.id.activity_suggestion_image);
		mSuggestionTxt = (TextView) findViewById(R.id.activity_suggestion_txt);

		mSuggestionImage.setImageResource(model.getId());
		mSuggestionTxt.setText("今天：" + model.getName_txt_detail());

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(this) != null) {
				NavUtils.navigateUpFromSameTask(this);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}
}
