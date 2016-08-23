package cn.it.weatherforecast.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.it.weatherforecast.model.Areas;


//针对数据库操作规范接口来实现接口中定义的方法
public class WeatherForecastDB implements WeatherForecastDBInterface 
{

	public static final String DB_NAME="weather_forecast";
	public static final int VERSION=1;
	private static WeatherForecastDB mWeatherForecastDB;
	private SQLiteDatabase mDB;
	
	private WeatherForecastDB(Context context)
	{
		WeatherForecastDBOpenHelper dbHelper=new WeatherForecastDBOpenHelper
				(context, DB_NAME, null, VERSION);
		
		mDB=dbHelper.getWritableDatabase();
	}

	public synchronized static WeatherForecastDB getInstance(Context context)
	{
		if(mWeatherForecastDB==null)
		{
			mWeatherForecastDB=new WeatherForecastDB(context);
		}
		return mWeatherForecastDB;
	}
	@Override
	public void saveAreas(List<Areas> areas) {
		if(mDB!=null)
		{
			for (Areas area : areas) {
				ContentValues values=new ContentValues();
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
		if(mDB!=null)
		{
			Cursor cursor=mDB.query("City", null, null, null, null, null, null, null);
			List<Areas> areasList=null;
			if(cursor!=null)
			{
				areasList=new ArrayList<Areas>();
				if(cursor.moveToFirst())
				{
					do{
						Areas areas=new Areas();
						areas.setCityName(cursor.getString
								(cursor.getColumnIndex("city_name")));
						areas.setCityId(cursor.getString
								(cursor.getColumnIndex("city_id")));
						areas.setCityLat(cursor.getString
								(cursor.getColumnIndex("city_lat")));
						areas.setCityLon(cursor.getString
								(cursor.getColumnIndex("city_lon")));
						areasList.add(areas);
					}while(cursor.moveToNext());
				}
			}
			cursor.close();
			return areasList;
		}
		return null;
	}

}
