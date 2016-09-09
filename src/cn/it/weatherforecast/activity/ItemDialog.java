package cn.it.weatherforecast.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cn.it.weatherforecast.R;
import cn.it.weatherforecast.db.WeatherForecastDB;
import cn.it.weatherforecast.util.MyApplication;

/*
 * 自定义一个Dialog，设置在当长按SelecedActivity中的item时，将会弹出一个dialog。
 * 该dialog用于执行item的点击操作。实现了查看和删除指定item的功能
 */
public class ItemDialog extends Dialog {

	private Context mContext;
	private ListView mItemList;
	private String[] data;
	// SharedPreference存儲路徑
	private String mSelectId;
	// 加一个标志，表示者该dialog是否点击确定按键，删除指定的item
	private boolean isSureDeleteItem = false;

	public ItemDialog(Context context, String selectId) {
		super(context);
		mContext = context;
		mSelectId = selectId;
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

		mItemList = (ListView) findViewById(R.id.list_dialog);

		// 向ListView中添加操作
		addOperationToListView();
	}

	private void addOperationToListView() {
		// TODO Auto-generated method stub
		data = new String[] { "查看", "删除" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_list_item_1, data);
		mItemList.setAdapter(adapter);

		mItemList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (data[position].equals("查看")) {
					Intent i = new Intent(mContext, WeatherInfo.class);
					i.putExtra(WeatherInfo.FROM_DIALOG, mSelectId);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					mContext.startActivity(i);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setMessage("确定要删除该城市吗？？")
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface arg0, int arg1) {
											// 关闭该dialog
											dismiss();
										};
									})
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface arg0, int arg1) {
											// 关闭该dialog
											dismiss();
											// 通过editor来删除文件中的内容，释放资源
											SharedPreferences data = mContext
													.getSharedPreferences(
															mSelectId,
															Context.MODE_PRIVATE);
											// 加入判断是否存在该SharedPreference文件，存在则删除
											if (data.getString("status", "")
													.equals("ok")) {
												// 清空指定code中的数据
												Editor editor = data.edit();
												editor.clear();
												editor.commit();
												// 通过db操作，删除SQLite中所选城市的列表数目
												WeatherForecastDB db = MyApplication
														.getWeatherForecastDB();
												db.deleteSelectedAreas(mSelectId);
												Toast.makeText(mContext,
														"已经删除",
														Toast.LENGTH_SHORT)
														.show();

												isSureDeleteItem = true;
												// 在重新建立SelectedAreasActivity活动，刷新数据
												Intent i = new Intent(
														mContext,
														SelectAreasActivity.class);
												i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
												mContext.startActivity(i);
											}
										};
									});
					AlertDialog dialog = builder.create();
					dialog.setCanceledOnTouchOutside(false);
					dialog.setCancelable(false);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.show();
				}
			}
		});
	}

	public boolean getIsSureDeleteItem() {
		return isSureDeleteItem;
	}
}
