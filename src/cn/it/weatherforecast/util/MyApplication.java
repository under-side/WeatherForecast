package cn.it.weatherforecast.util;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import cn.it.weatherforecast.db.WeatherForecastDB;

/*
 * ����ȫ��Application�࣬��ִ�д洢һЩȫ����Ҫ��ͬʹ�õ����ݺ���Դ
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
	//��ȡȫ��Context
	public static Context getContext()
	{
		return mContext;
	}
	//����Ӧ�ù���һ��LruCache�ļ�
	public static LruCache<String, Bitmap> getLruCache()
	{
		return mMemoryCache;
	}
	//ȫ�ֻ�ȡӦ���е�SQLite�ļ���������ִ�и��ԵĲ�������ͬ������������̶߳�SQLite��ɴ���
	public synchronized static WeatherForecastDB getWeatherForecastDB()
	{
		return mDB;
	}
	
	//��ȡ�����õ�ǰ��ҳ���������Ƿ�������ɱ��
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
