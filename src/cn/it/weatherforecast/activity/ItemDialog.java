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
 * �Զ���һ��Dialog�������ڵ�����SelecedActivity�е�itemʱ�����ᵯ��һ��dialog��
 * ��dialog����ִ��item�ĵ��������ʵ���˲鿴��ɾ��ָ��item�Ĺ���
 */
public class ItemDialog extends Dialog {

	private Context mContext;
	private ListView mItemList;
	private String[] data;
	// SharedPreference�惦·��
	private String mSelectId;
	// ��һ����־����ʾ�߸�dialog�Ƿ���ȷ��������ɾ��ָ����item
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

		// ��ListView����Ӳ���
		addOperationToListView();
	}

	private void addOperationToListView() {
		// TODO Auto-generated method stub
		data = new String[] { "�鿴", "ɾ��" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_list_item_1, data);
		mItemList.setAdapter(adapter);

		mItemList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (data[position].equals("�鿴")) {
					Intent i = new Intent(mContext, WeatherInfo.class);
					i.putExtra(WeatherInfo.FROM_DIALOG, mSelectId);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					mContext.startActivity(i);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setMessage("ȷ��Ҫɾ���ó����𣿣�")
							.setNegativeButton("ȡ��",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface arg0, int arg1) {
											// �رո�dialog
											dismiss();
										};
									})
							.setPositiveButton("ȷ��",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface arg0, int arg1) {
											// �رո�dialog
											dismiss();
											// ͨ��editor��ɾ���ļ��е����ݣ��ͷ���Դ
											SharedPreferences data = mContext
													.getSharedPreferences(
															mSelectId,
															Context.MODE_PRIVATE);
											// �����ж��Ƿ���ڸ�SharedPreference�ļ���������ɾ��
											if (data.getString("status", "")
													.equals("ok")) {
												// ���ָ��code�е�����
												Editor editor = data.edit();
												editor.clear();
												editor.commit();
												// ͨ��db������ɾ��SQLite����ѡ���е��б���Ŀ
												WeatherForecastDB db = MyApplication
														.getWeatherForecastDB();
												db.deleteSelectedAreas(mSelectId);
												Toast.makeText(mContext,
														"�Ѿ�ɾ��",
														Toast.LENGTH_SHORT)
														.show();

												isSureDeleteItem = true;
												// �����½���SelectedAreasActivity���ˢ������
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
