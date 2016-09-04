package cn.it.weatherforecast.util;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import cn.it.weatherforecast.db.WeatherForecastDB;

/*
 * 运用全局Application类，来执行存储一些全局需要共同使用的数据和资源
 */
public class MyApplication extends Application {

	private static Context mContext;
	private static LruCache<String , Bitmap> mMemoryCache;
	private static WeatherForecastDB mDB;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext=getApplicationContext();
		
		int maxSize = (int) ((Runtime.getRuntime().maxMemory())/(1024*1024))/10;
		mMemoryCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// TODO Auto-generated method stub
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}

			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				// TODO Auto-generated method stub
				super.entryRemoved(evicted, key, oldValue, newValue);
//				Toast.makeText(mContext,
//						"hard cache is full", Toast.LENGTH_SHORT).show();
			}
		};
		mDB=WeatherForecastDB.getInstance(mContext);
	}
	public static Context getContext()
	{
		return mContext;
	}
	public static LruCache<String, Bitmap> getLruCache()
	{
		return mMemoryCache;
	}
	public static WeatherForecastDB getWeatherForecastDB()
	{
		return mDB;
	}
	
}
