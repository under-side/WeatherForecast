package cn.it.weatherforecast.util;

import android.content.Context;
import android.content.SharedPreferences;


/*
 * 该类封了运用HttpURLConnection来获取网络数据
 */
public class HttpUtilForDowloadJson {

	private static final String url="https://api.heweather.com/x3/weather?cityid=";
    private static final String key="&key=e880b41d75d840d7aaaad18356139993";

	public static void getAreasFromHttp(final String address,
			final HttpCallbackListenerForJson listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				String response = HttpDownloadUtil
						.downTextByHttpUrlConnection(address);
				if (response != null) {
					listener.onFinish(response);
				}
			}
		}).start();
	}
	
	public static boolean getWeatherInfoFromHttp(String selectCityName,final String selectId,final Context context)
	{
		String weatherUrl=url+selectId+key;
		HttpUtilForDowloadJson.getAreasFromHttp(weatherUrl,
				new HttpCallbackListenerForJson() {

					@Override
					public void onFinish(String response) {
						// TODO Auto-generated method stub
						SharedPreferences data = context.getSharedPreferences(selectId, Context.MODE_PRIVATE);
						Utility.handleWeatherInfoResponseByJSON(data,response);
					}
					
					@Override
					public void onError(Exception e) {
						// TODO Auto-generated method stub
						e.printStackTrace();
					}
				});
		return true;
	}
}