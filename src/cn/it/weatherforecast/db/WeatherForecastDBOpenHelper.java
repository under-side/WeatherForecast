package cn.it.weatherforecast.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

//利用SQLiteOpenHelper来创建SQLite对象和对数据库操作的对象
public class WeatherForecastDBOpenHelper extends SQLiteOpenHelper {

	// 建表语句
	private static final String CREATE_CITY = "create table City("
			+ "id integer primary key autoincrement," + "city_name text,"
			+ "city_id text," + "city_lat  text," + "city_lon  text)";

	// 建表语句
	private static final String CREATE_SELECT_AREA = "create table SelectedAreas("
			+ "id integer primary key autoincrement,"
			+ "select_area_name text,"
			+ "select_area_update_time long,"
			+ "select_area_code  text)";

	public WeatherForecastDBOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_SELECT_AREA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
