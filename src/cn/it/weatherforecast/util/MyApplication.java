package cn.it.weatherforecast.util;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.Toast;

public class MyApplication extends Application {

	private static Context mContext;
	private static LruCache<String , Bitmap> mMemoryCache;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext=getApplicationContext();
		
		int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024)/8;
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
				Toast.makeText(mContext,
						"hard cache is full", Toast.LENGTH_SHORT).show();
			}
		};
		
	}
	public static Context getContext()
	{
		return mContext;
	}
	public static LruCache<String, Bitmap> getLruCache()
	{
		return mMemoryCache;
	}
}
