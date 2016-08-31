package cn.it.weatherforecast.activity;

import cn.it.weatherforecast.R;
import cn.it.weatherforecast.model.ModelForGrid2;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SuggestionDetailActivity extends Activity {

	private ImageView mSuggestionImage;
	private TextView mSuggestionTxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_for_suggestion_detail);
		
		ModelForGrid2 model=(ModelForGrid2) getIntent().getSerializableExtra("ClickItem");
		
		mSuggestionImage=(ImageView) findViewById(R.id.activity_suggestion_image);
		mSuggestionTxt=(TextView) findViewById(R.id.activity_suggestion_txt);
		
		mSuggestionImage.setImageResource(model.getId());
		mSuggestionTxt.setText("½ñÌì£º"+model.getName_txt_detail());
	}
}
