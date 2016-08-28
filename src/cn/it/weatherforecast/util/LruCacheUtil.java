package cn.it.weatherforecast.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.Toast;

public class LruCacheUtil {
	private static LruCache<String, Bitmap> mMemoryCache;
    private static Context mContext=MyApplication.getContext();
    
    //�ڹ��캯���ж�LruCache���г�ʼ������
    public LruCacheUtil()
    {
    	if (mMemoryCache == null) {
			int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024);
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
    }
    //ʹ�õ������ģʽ��ʹֻ�ܻ�ȡһ��Ӧ�õ�Cache
	public static LruCacheUtil getLruCacheUtil() {
		
		return new LruCacheUtil();
	}
	
	public void clearCache() {
        if (mMemoryCache != null) {
            if (mMemoryCache.size() > 0) {
//                LogUtil.d("CacheUtils",
//                        "mMemoryCache.size() " + mMemoryCache.size());
                mMemoryCache.evictAll();
//                LogUtil.d("CacheUtils", "mMemoryCache.size()" + mMemoryCache.size());
            }
            mMemoryCache = null;
        }
    }

    public synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (mMemoryCache.get(key) == null) {
            if (key != null && bitmap != null)
                mMemoryCache.put(key, bitmap);
        } else{
        	Toast.makeText(mContext,
        			"the res is aready exits", Toast.LENGTH_SHORT).show();
        }
    }

    public synchronized Bitmap getBitmapFromMemCache(String key) {
        Bitmap bm = mMemoryCache.get(key);
        if (key != null) {
            return bm;
        }
        return null;
    }

    /**
     * �Ƴ�����
     * 
     * @param key
     */
    public synchronized  void removeImageCache(String key) {
        if (key != null) {
            if (mMemoryCache != null) {
                Bitmap bm = mMemoryCache.remove(key);
                if (bm != null)
                    bm.recycle();
            }
        }
    }

	
}
