package cn.it.weatherforecast.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import cn.it.weatherforecast.db.WeatherForecastDB;
import cn.it.weatherforecast.model.Areas;

/*
 * 该类封装了运用JSONObject的方法来解析JSON数据
 */
public class Utility {

	public static boolean handleResponseByJSON(WeatherForecastDB db, String json) {
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

	public static void handleWeatherInfoResponseByJSON(SharedPreferences data,
			Context context, String json) {
		try {
			JSONObject heWeather = new JSONObject(json);
			JSONArray heWeatherinfo = heWeather
					.getJSONArray("HeWeather data service 3.0");
			for (int i = 0; i < heWeatherinfo.length(); i++) {
				JSONObject heWeatherDetail = heWeatherinfo.getJSONObject(i);

				if(heWeatherDetail.getJSONObject("aqi")!=null)
				{
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			String cwfBrf = cwObject.getString("brf");

			JSONObject drsgObject = jsonObject.getJSONObject("drsg");
			String drsgBrf = drsgObject.getString("brf");

			JSONObject fluObject = jsonObject.getJSONObject("flu");
			String fluBrf = fluObject.getString("brf");

			JSONObject sportObject = jsonObject.getJSONObject("sport");
			String sportBrf = sportObject.getString("brf");

			JSONObject travObject = jsonObject.getJSONObject("trav");
			String travBrf = travObject.getString("brf");

			JSONObject uvObject = jsonObject.getJSONObject("uv");
			String uvBrf = uvObject.getString("brf");

			editor.putString("suggestion_comf_brf", comfBrf);
			editor.putString("suggestion_comf_txt", comfTxt);
			editor.putString("suggestion_cw", cwfBrf);
			editor.putString("suggestion_drsg", drsgBrf);
			editor.putString("suggestion_flu", fluBrf);
			editor.putString("suggestion_sport", sportBrf);
			editor.putString("suggestion_trav", travBrf);
			editor.putString("suggestion_uv", uvBrf);
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
		for (int i = 1; i <= jsonArray.length(); i++) {
			try {
				JSONObject info = jsonArray.getJSONObject(i);
				editor.putString("hourly_date" + i, info.getString("date"));
				editor.putString("hourly_pop" + i, info.getString("pop"));
				editor.putString("hourly_tmp" + i, info.getString("tmp"));
				
				JSONObject windObject=info.getJSONObject("wind");
				editor.putString("hourly_sc"+i, windObject.getString("sc"));
				editor.commit();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void saveInfoNow(SharedPreferences data, JSONObject jsonObject) {
		// TODO Auto-generated method stub

		Editor editor = data.edit();
		try {
			JSONObject codeObject = jsonObject.getJSONObject("cond");
			String code = codeObject.getString("code");
			String txt = codeObject.getString("txt");
			String fl = jsonObject.getString("fl");
			String tmp = jsonObject.getString("tmp");
			editor.putString("now_code", code);
			editor.putString("now_txt", txt);
			editor.putString("now_fl", fl);
			editor.putString("now_tmp", tmp);
			editor.commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void saveInfoDailyForecast(SharedPreferences data,
			JSONArray jsonArray) {
		// TODO Auto-generated method stub
		for (int i = 1; i <= jsonArray.length(); i++) {
			try {
				JSONObject info = jsonArray.getJSONObject(i);

				JSONObject astroObject = info.getJSONObject("astro");
				String sr = astroObject.getString("sr");
				String ss = astroObject.getString("ss");

				JSONObject condObject = info.getJSONObject("cond");
				String code_d = condObject.getString("code_d");
				String txt_d = condObject.getString("txt_d");

				String date = info.getString("date");

				JSONObject tmpObject = info.getJSONObject("tmp");
				String max = tmpObject.getString("max");
				String min = tmpObject.getString("min");

				Editor editor = data.edit();
				editor.putString("daily_sr"+i, sr);
				editor.putString("daily_ss"+i, ss);
				editor.putString("daily_code_d"+i, code_d);
				editor.putString("daily_txt_d"+i, txt_d);
				editor.putString("daily_date"+i, date);
				editor.putString("daily_max"+i, max);
				editor.putString("daily_min"+i, min);
				
				editor.commit();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void saveInfoBasic(SharedPreferences data, JSONObject jsonObject) {
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
	private static void saveInfoAqi(SharedPreferences data, JSONObject jsonObject) {
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
}
