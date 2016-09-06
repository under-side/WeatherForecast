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
	private static int index=0;
	private static boolean isOk=false;
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
	//获取全局Context
	public static Context getContext()
	{
		return mContext;
	}
	//整个应用共享一个LruCache文件
	public static LruCache<String, Bitmap> getLruCache()
	{
		return mMemoryCache;
	}
	//全局获取应用中的SQLite文件，并对其执行各自的操作，加同步锁，避免多线程对SQLite造成错误
	public synchronized static WeatherForecastDB getWeatherForecastDB()
	{
		return mDB;
	}
	
	//获取和设置当前的页数和数据是否下载完成标记
	public static int getIndex() {
		return index;
	}
	public static void setIndex(int index) {
		MyApplication.index = index;
	}
	public static boolean isOk() {
		return isOk;
	}
	public static void setOk(boolean isOk) {
		MyApplication.isOk = isOk;
	}
	
}
