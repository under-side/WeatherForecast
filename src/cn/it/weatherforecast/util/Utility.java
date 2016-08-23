package cn.it.weatherforecast.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.it.weatherforecast.db.WeatherForecastDB;
import cn.it.weatherforecast.model.Areas;


/*
 * 该类封装了运用JSONObject的方法来解析JSON数据
 */
public class Utility {

	public static boolean handleResponseByJSON(WeatherForecastDB db,String json)
	{
		List<Areas> areas=new ArrayList<Areas>();
		try {
			JSONObject cityinfoJSON=new JSONObject(json);
			JSONArray cityinfoJsonArray=cityinfoJSON.getJSONArray("city_info");
			for(int i=0;i<cityinfoJsonArray.length();i++)
			{
				JSONObject cityJson=cityinfoJsonArray.getJSONObject(i);
				Areas area=new Areas();
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
}
