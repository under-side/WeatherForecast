package cn.it.weatherforecast.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.it.weatherforecast.model.Areas;
import cn.it.weatherforecast.model.SelectedAreas;

//������ݿ�����淶�ӿ���ʵ�ֽӿ��ж���ķ���
public class WeatherForecastDB implements WeatherForecastDBInterface {

	public static final String DB_NAME = "weather_forecast";
	public static final int VERSION = 1;
	private static WeatherForecastDB mWeatherForecastDB;
	private SQLiteDatabase mDB;

	private WeatherForecastDB(Context context) {
		WeatherForecastDBOpenHelper dbHelper = new WeatherForecastDBOpenHelper(
				context, DB_NAME, null, VERSION);

		mDB = dbHelper.getWritableDatabase();
	}

	// ʹ��ͬ���������ƶ��̵߳ķ��ʣ���ֹ�����̻߳���
	public synchronized static WeatherForecastDB getInstance(Context context) {
		if (mWeatherForecastDB == null) {
			mWeatherForecastDB = new WeatherForecastDB(context);
		}
		return mWeatherForecastDB;
	}

	@Override
	public void saveAreas(List<Areas> areas) {
		if (mDB != null) {
			for (Areas area : areas) {
				ContentValues values = new ContentValues();
				values.put("city_name", area.getCityName());
				values.put("city_id", area.getCityId());
				values.put("city_lat", area.getCityLat());
				values.put("city_lon", area.getCityLon());

				mDB.insert("City", null, values);
			}

		}
	}

	@Override
	public List<Areas> loadAreas() {
		if (mDB != null) {
			Cursor cursor = mDB.query("City", null, null, null, null, null,
					"city_name");
			List<Areas> areasList = null;
			if (cursor != null) {
				areasList = new ArrayList<Areas>();
				if (cursor.moveToFirst()) {
					do {
						Areas areas = new Areas();
						areas.setCityName(cursor.getString(cursor
								.getColumnIndex("city_name")));
						areas.setCityId(cursor.getString(cursor
								.getColumnIndex("city_id")));
						areas.setCityLat(cursor.getString(cursor
								.getColumnIndex("city_lat")));
						areas.setCityLon(cursor.getString(cursor
								.getColumnIndex("city_lon")));
						areasList.add(areas);
					} while (cursor.moveToNext());
				}
			}
			cursor.close();
			return areasList;
		}
		return null;
	}

	// ����ģ����ѯ����SQLite�в�ѯ���������ĳ�������
	@Override
	public List<Areas> loadAreas(String s) {
		// TODO Auto-generated method stub
		if (mDB != null) {
			Cursor cursor = mDB.query("City", null, "city_name=?",
					new String[] { s }, null, null, "city_name");
			List<Areas> areasList = null;
			if (cursor.getCount() == 0) {
				cursor = mDB
						.query("City", null, "city_name LIKE ?",
								new String[] { "%" + s + "%" }, null, null,
								"city_name");
			}
			if (cursor != null) {
				areasList = new ArrayList<Areas>();
				if (cursor.moveToFirst()) {
					do {
						Areas areas = new Areas();
						areas.setCityName(cursor.getString(cursor
								.getColumnIndex("city_name")));
						areas.setCityId(cursor.getString(cursor
								.getColumnIndex("city_id")));
						areas.setCityLat(cursor.getString(cursor
								.getColumnIndex("city_lat")));
						areas.setCityLon(cursor.getString(cursor
								.getColumnIndex("city_lon")));
						areasList.add(areas);
					} while (cursor.moveToNext());
				}
			}
			cursor.close();
			return areasList;
		}
		return null;
	}

	// ��ȡ�ܹ�ѡ��ĳ���Code
	@Override
	public List<SelectedAreas> loadSelectedAreas() {
		// TODO Auto-generated method stub
		if (mDB != null) {
			Cursor cursor = mDB.query("SelectedAreas", null, null, null, null,
					null, null);
			List<SelectedAreas> selectedAreasList = null;
			if (cursor != null) {
				selectedAreasList = new ArrayList<SelectedAreas>();
				if (cursor.moveToFirst()) {
					do {
						String areaCode = cursor.getString(cursor
								.getColumnIndex("select_area_code"));
						String areaName = cursor.getString(cursor
								.getColumnIndex("select_area_name"));
						long areaUpdateTime=cursor
								.getLong(cursor.getColumnIndex("select_area_update_time"));
						SelectedAreas model = new SelectedAreas();
						model.setSelectedCode(areaCode);
						model.setSelectedName(areaName);
						model.setUpdateTime(areaUpdateTime);
						selectedAreasList.add(model);
					} while (cursor.moveToNext());
				}
			}
			cursor.close();
			return selectedAreasList;
		}
		return null;
	}

	// ����ѡ��ĳ��д���
	@Override
	public void saveSelectedAreasInfo(String code, String name, long updateTime) {
		// TODO Auto-generated method stub
		if (mDB != null) {
			ContentValues values = new ContentValues();
			values.put("select_area_code", code);
			values.put("select_area_name", name);
			values.put("select_area_update_time", updateTime);
			mDB.insert("SelectedAreas", null, values);
		}

	}

	// ɾ��ָ���ĳ���
	@Override
	public void deleteSelectedAreas(String selectId) {
		// TODO Auto-generated method stub
		mDB.delete("SelectedAreas", "select_area_code=?",
				new String[] { selectId });
	}

	@Override
	public void updateSelectedAreasTime(String updateCode, long updateTime) {
		// TODO Auto-generated method stub
		ContentValues values=new ContentValues();
		values.put("select_area_update_time", updateTime);
		mDB.update("SelectedAreas", values, "select_area_code=?", new String[] { updateCode });
	}

}
