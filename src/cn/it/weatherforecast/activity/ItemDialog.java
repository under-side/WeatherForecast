package cn.it.weatherforecast.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.it.weatherforecast.R;

public class ItemDialog extends Dialog {

	private Context mContext;
	private ListView mItemList;
	private String mSelectId;
	private String[] data;
	
	public ItemDialog(Context context,String selectId) {
		super(context);
		mContext=context;
		mSelectId=selectId;
	}
	  public ItemDialog(Context context) {
	        super(context);
	        this.mContext = context;
	    }
	    
	    public ItemDialog(Context context, int theme) {
	        super(context, theme);
	        this.mContext = context;
	    }
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	// TODO Auto-generated method stub
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.layout_for_dialog);
	    	
	    	mItemList=(ListView) findViewById(R.id.list_dialog);
	    	
	    	
	    	//向ListView中添加操作
	    	addOperationToListView();
	    }
		private void addOperationToListView() {
			// TODO Auto-generated method stub
			data=new String[]{"查看","删除"};
	    	ArrayAdapter<String> adapter=new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,data);
	    	mItemList.setAdapter(adapter);
	    	
	    	mItemList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if(data[position].equals("查看"))
					{
						Intent i=new Intent(mContext,WeatherInfo.class);
						i.putExtra(WeatherInfo.FROM_DIALOG, mSelectId);
						mContext.startActivity(i);
					}
					else{
						AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
						builder.setTitle("test");
						AlertDialog dialog=builder.create();
						dialog.show();
						SharedPreferences data=mContext.
								getSharedPreferences(mSelectId, Context.MODE_PRIVATE);
					}
				}
			});
		}
}
