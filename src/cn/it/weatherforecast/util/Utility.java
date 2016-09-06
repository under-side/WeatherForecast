package cn.it.weatherforecast.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import cn.it.weatherforecast.db.WeatherForecastDB;
import cn.it.weatherforecast.model.Areas;

/*
 * 该类封装了运用JSONObject的方法来解析JSON数据
 */
public class Utility {

	public static boolean handleJsonForDBAreas(WeatherForecastDB db, String json) {
		List<Areas> areas = new ArrayList<Areas>();
		try {
			JSONObject cityinfoJSON = new JSONObject(json);
			JSONArray cityinfoJsonArray = cityinfoJSON
					.getJSONArray("city_info");
			for (int i = 0; i < cityinfoJsonArray.length(); i++) {
				JSONObject cityJson = cityinfoJsonArray.getJSONObject(i);
				Areas area = new Areas();
				area.setCityName(cityJson.getString("city"));
				area.setCityId(cityJson.getString("id"));
				area.setCityLat(cityJson.getString("lat"));
				area.setCityLon(cityJson.getString("lon"));

				areas.add(area);
			}
			db.saveAreas(areas);
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static boolean handleWeatherInfoResponseByJSON(
			SharedPreferences data, String json) {
		try {
			JSONObject heWeather = new JSONObject(json);
			JSONArray heWeatherinfo = heWeather
					.getJSONArray("HeWeather data service 3.0");
			for (int i = 0; i < heWeatherinfo.length(); i++) {
				JSONObject heWeatherDetail = heWeatherinfo.getJSONObject(i);

				if (heWeatherDetail.getJSONObject("aqi") != null) {
					saveInfoAqi(data, heWeatherDetail.getJSONObject("aqi"));
				}

				saveInfoBasic(data, heWeatherDetail.getJSONObject("basic"));

				saveInfoDailyForecast(data,
						heWeatherDetail.getJSONArray("daily_forecast"));

				saveInfoHourlyForecast(data,
						heWeatherDetail.getJSONArray("hourly_forecast"));

				saveInfoNow(data, heWeatherDetail.getJSONObject("now"));

				saveInfoStatus(data, heWeatherDetail.getString("status"));

				saveInfoSuggestion(data,
						heWeatherDetail.getJSONObject("suggestion"));

			}
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private static void saveInfoSuggestion(SharedPreferences data,
			JSONObject jsonObject) {
		// TODO Auto-generated method stub
		Editor editor = data.edit();
		try {
			JSONObject comfObject = jsonObject.getJSONObject("comf");
			String comfBrf = comfObject.getString("brf");
			String comfTxt = comfObject.getString("txt");

			JSONObject cwObject = jsonObject.getJSONObject("cw");
			String cwBrf = cwObject.getString("brf");
			String cwTxt = cwObject.getString("txt");

			JSONObject drsgObject = jsonObject.getJSONObject("drsg");
			String drsgBrf = drsgObject.getString("brf");
			String drsgTxt = drsgObject.getString("txt");

			JSONObject fluObject = jsonObject.getJSONObject("flu");
			String fluBrf = fluObject.getString("brf");
			String fluTxt = fluObject.getString("txt");

			JSONObject sportObject = jsonObject.getJSONObject("sport");
			String sportBrf = sportObject.getString("brf");
			String sportTxt = sportObject.getString("txt");

			JSONObject travObject = jsonObject.getJSONObject("trav");
			String travBrf = travObject.getString("brf");
			String travTxt = travObject.getString("txt");

			JSONObject uvObject = jsonObject.getJSONObject("uv");
			String uvBrf = uvObject.getString("brf");
			String uvTxt = uvObject.getString("txt");

			editor.putString("suggestion_comf_brf", comfBrf);
			editor.putString("suggestion_comf_txt", comfTxt);
			editor.putString("suggestion_cw", cwBrf);
			editor.putString("suggestion_cw_txt", cwTxt);
			editor.putString("suggestion_drsg", drsgBrf);
			editor.putString("suggestion_drsg_txt", drsgTxt);
			editor.putString("suggestion_flu", fluBrf);
			editor.putString("suggestion_flu_txt", fluTxt);
			editor.putString("suggestion_sport", sportBrf);
			editor.putString("suggestion_sport_txt", sportTxt);
			editor.putString("suggestion_trav", travBrf);
			editor.putString("suggestion_trav_txt", travTxt);
			editor.putString("suggestion_uv", uvBrf);
			editor.putString("suggestion_uv_txt", uvTxt);
			editor.commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void saveInfoStatus(SharedPreferences data, String status) {
		// TODO Auto-generated method stub
		Editor editor = data.edit();
		editor.putString("status", status);
		editor.commit();
	}

	private static void saveInfoHourlyForecast(SharedPreferences data,
			JSONArray jsonArray) {
		// TODO Auto-generated method stub
		Editor editor = data.edit();
		editor.putInt("hourly_count", jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject info = jsonArray.getJSONObject(i);
				editor.putString("hourly_date" + i, info.getString("date"));
				editor.putString("hourly_pop" + i, info.getString("pop"));
				editor.putString("hourly_tmp" + i, info.getString("tmp"));
				JSONObject windObject = info.getJSONObject("wind");
				editor.putString("hourly_dir" + i, windObject.getString("dir"));
				editor.commit();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void saveInfoNow(SharedPreferences data,
			JSONObject jsonObject) {
		// TODO Auto-generated method stub

		Editor editor = data.edit();
		try {
			JSONObject codeObject = jsonObject.getJSONObject("cond");
			String code = codeObject.getString("code");
			String txt = codeObject.getString("txt");
			String fl = jsonObject.getString("fl");
			String tmp = jsonObject.getString("tmp");
			JSONObject windObject = jsonObject.getJSONObject("wind");
			String dir = windObject.getString("dir");
			String sc = windObject.getString("sc");
			String hum = jsonObject.getString("hum");
			editor.putString("now_code", code);
			editor.putString("now_txt", txt);
			editor.putString("now_fl", fl);
			editor.putString("now_tmp", tmp);
			editor.putString("now_dir", dir);
			editor.putString("now_sc", sc);
			editor.putString("now_hum", hum);
			editor.commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void saveInfoDailyForecast(SharedPreferences data,
			JSONArray jsonArray) {
		// TODO Auto-generated method stub

		Editor editor = data.edit();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject info = jsonArray.getJSONObject(i);

				JSONObject astroObject = info.getJSONObject("astro");
				// 只保存第一个的天气日落和日出时间
				if (i == 1) {
					String sr = astroObject.getString("sr");
					String ss = astroObject.getString("ss");
					editor.putString("daily_sr" + i, sr);
					editor.putString("daily_ss" + i, ss);
				}

				JSONObject condObject = info.getJSONObject("cond");
				String code_d = condObject.getString("code_d");
				String txt_d = condObject.getString("txt_d");

				String date = info.getString("date");

				JSONObject tmpObject = info.getJSONObject("tmp");
				String max = tmpObject.getString("max");
				String min = tmpObject.getString("min");

				editor.putString("daily_code_d" + i, code_d);
				editor.putString("daily_txt_d" + i, txt_d);
				editor.putString("daily_date" + i, date);
				editor.putString("daily_max" + i, max);
				editor.putString("daily_min" + i, min);

				editor.commit();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void saveInfoBasic(SharedPreferences data,
			JSONObject jsonObject) {
		// TODO Auto-generated method stub
		Editor editor = data.edit();
		try {
			String city = jsonObject.getString("city");
			String id = jsonObject.getString("id");
			String lat = jsonObject.getString("lat");
			String lon = jsonObject.getString("lon");
			JSONObject updateObject = jsonObject.getJSONObject("update");
			String loc = updateObject.getString("loc");
			editor.putString("basic_city", city);
			editor.putString("basic_id", id);
			editor.putString("basic_lat", lat);
			editor.putString("basic_lon", lon);
			editor.putString("basic_loc", loc);
			editor.commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// save weather info of aqi
	private static void saveInfoAqi(SharedPreferences data,
			JSONObject jsonObject) {
		// TODO Auto-generated method stub
		try {
			Editor editor = data.edit();

			JSONObject cityInfo = jsonObject.getJSONObject("city");

			String aqi = cityInfo.getString("aqi");
			String pm10 = cityInfo.getString("pm10");
			String pm25 = cityInfo.getString("pm25");
			String qlty = cityInfo.getString("qlty");

			editor.putString("city_aqi", aqi);
			editor.putString("city_pm10", pm10);
			editor.putString("city_pm25", pm25);
			editor.putString("city_aqi", aqi);
			editor.putString("city_qlty", qlty);

			editor.commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getWeekFromDate(String d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date date;
		try {
			date = sdf.parse(d);
			String[] weekOfDays = { "周末", "周一", "周二", "周三", "周四", "周五", "周六" };
			Calendar calendar = Calendar.getInstance();
			if (date != null) {
				calendar.setTime(date);
			}
			int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			if (w < 0) {
				w = 0;
			}
			return weekOfDays[w];
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 将对日期进行分割的代码封装起来，可以重复使用
	public static String splitDateString(String time, int index) {
		String[] splitDate = time.split(" ");
		return splitDate[index];
	}

	// 将从网上获取图片，并将其组件赋值操作封装起来，实现代码的重用性
	public static void setImageViewFromHttp(final String url,
			final ImageView weatherImage) {
		DownloadBitmapForImage downloadImage = new DownloadBitmapForImage(
				new AsyncCallbackListenerForBitmap() {

					@Override
					public void onFinish(byte[] photo) {
						// TODO Auto-generated method stub

						Bitmap imageBitmap = BitmapFactory.decodeByteArray(
								photo, 0, photo.length);
						weatherImage.setImageBitmap(imageBitmap);
						putImageBitmapToCache(url, imageBitmap);
					}

					@Override
					public void onError(String errorMessage) {
						// TODO Auto-generated method stub
						LogUtil.d("WeatherInfoBeforeFragment", errorMessage);
					}
				});
		downloadImage.execute(url);
	}

	// 保存在网上下载的图片到缓存中去
	private static void putImageBitmapToCache(String url, Bitmap bitmap) {
		MyApplication.getLruCache().put(url, bitmap);
	}

}
