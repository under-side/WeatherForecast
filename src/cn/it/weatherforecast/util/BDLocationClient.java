package cn.it.weatherforecast.util;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import cn.it.weatherforecast.activity.SelectAreasActivity;
import cn.it.weatherforecast.db.WeatherForecastDB;
import cn.it.weatherforecast.model.Areas;
import cn.it.weatherforecast.model.SelectedAreas;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/*
 * 运用单例设计模式，获取百度定位LocationClient实例对象
 */
public class BDLocationClient {
	private static LocationClient mLocationClient=null;
	private static BDLocationListener mListener;
	
	//获取实例对象
	public static synchronized LocationClient getLocatinClientInstance()
	{
		mLocationClient=new LocationClient(MyApplication.getContext());
		mListener=new MyLocationListener();
		mLocationClient.registerLocationListener(mListener);
		initLocation();
		return mLocationClient;
	}
	
	//设置LocationClient中的设置属性
	 private static void initLocation()
	    {
	    	LocationClientOption option=new LocationClientOption();
	    	option.setLocationMode(LocationMode.Device_Sensors);
	    	option.setIsNeedAddress(true);
	    	option.setIsNeedLocationDescribe(true);
	    	option.setIgnoreKillProcess(false);
	    	mLocationClient.setLocOption(option);
	    }
}

class MyLocationListener implements BDLocationListener
{
	WeatherForecastDB db=MyApplication.getWeatherForecastDB();
	Context context=MyApplication.getContext();
	boolean isHaveLocationCity=false;
	//定位成功的回调函数，处理Location中的数据
	@Override
	public void onReceiveLocation(BDLocation location) {
		// TODO Auto-generated method stub
		
		//根據定位回調函數中的Location信息，獲取當地城市信息，在拼接為SQLite對應的城市名稱
		String[] cityArray=location.getCity().split("");
		StringBuilder builder=new StringBuilder();
		for(int i=0;i<cityArray.length-1;i++)
		{
			builder.append(cityArray[i]);
		}
		//獲取SQLite中的城市信息
		String locationCity=builder.toString();
		
		Areas areas=db.loadAreas(locationCity).get(0);
		String locationCityCode=areas.getCityId();
		List<SelectedAreas> selectedAreasList=db.loadSelectedAreas();
		
		//用於判斷SQLite選擇城市列表中是否含有定位的城市，有則不再添加
		for (SelectedAreas selectedAreas : selectedAreasList) {
			if(selectedAreas.getSelectedCode().equals(locationCityCode))
			{
				isHaveLocationCity=true;
				break;
			}
		}
		
		//加入判断，选择城市类表中是否有定位的城市，没有，则添加到SQLite中的列表中去
		if(!isHaveLocationCity)
		{
			db.saveSelectedAreasInfo(locationCityCode, locationCity, new Date().getTime());
		}
		
		//启动SelectedAreasActivity活动
		Intent i=new Intent(context,SelectAreasActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		context.startActivity(i);
		Toast.makeText(MyApplication.getContext(), 
				"定位到："+location.getProvince()+location.getCity()+location.getDistrict(), Toast.LENGTH_LONG).show();
	}
}