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
 * ���õ������ģʽ����ȡ�ٶȶ�λLocationClientʵ������
 */
public class BDLocationClient {
	private static LocationClient mLocationClient=null;
	private static BDLocationListener mListener;
	
	//��ȡʵ������
	public static synchronized LocationClient getLocatinClientInstance()
	{
		mLocationClient=new LocationClient(MyApplication.getContext());
		mListener=new MyLocationListener();
		mLocationClient.registerLocationListener(mListener);
		initLocation();
		return mLocationClient;
	}
	
	//����LocationClient�е���������
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
	//��λ�ɹ��Ļص�����������Location�е�����
	@Override
	public void onReceiveLocation(BDLocation location) {
		// TODO Auto-generated method stub
		
		//������λ���{�����е�Location��Ϣ���@ȡ���س�����Ϣ����ƴ�Ӟ�SQLite�����ĳ������Q
		String[] cityArray=location.getCity().split("");
		StringBuilder builder=new StringBuilder();
		for(int i=0;i<cityArray.length-1;i++)
		{
			builder.append(cityArray[i]);
		}
		//�@ȡSQLite�еĳ�����Ϣ
		String locationCity=builder.toString();
		
		Areas areas=db.loadAreas(locationCity).get(0);
		String locationCityCode=areas.getCityId();
		List<SelectedAreas> selectedAreasList=db.loadSelectedAreas();
		
		//����Д�SQLite�x������б����Ƿ��ж�λ�ĳ��У��Єt�������
		for (SelectedAreas selectedAreas : selectedAreasList) {
			if(selectedAreas.getSelectedCode().equals(locationCityCode))
			{
				isHaveLocationCity=true;
				break;
			}
		}
		
		//�����жϣ�ѡ�����������Ƿ��ж�λ�ĳ��У�û�У�����ӵ�SQLite�е��б���ȥ
		if(!isHaveLocationCity)
		{
			db.saveSelectedAreasInfo(locationCityCode, locationCity, new Date().getTime());
		}
		
		//����SelectedAreasActivity�
		Intent i=new Intent(context,SelectAreasActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		context.startActivity(i);
		Toast.makeText(MyApplication.getContext(), 
				"��λ����"+location.getProvince()+location.getCity()+location.getDistrict(), Toast.LENGTH_LONG).show();
	}
}