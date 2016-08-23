package cn.it.weatherforecast.db;

import java.util.List;

import cn.it.weatherforecast.model.Areas;

/*
 * �ýӿ���Ϊ��SQLite���ݽ��в�����һ���淶��ֻ��ʹ�ö���ķ����������ݽ��в���
 * ��SQLite�����ݵĲ�����������DAO���ģʽ
 * һ���������ݿ���
 * һ�������ݿ�������һһ��Ӧ��ʵ����
 * һ�������ݿ�����Ĺ淶�ӿ�
 * һ��ʵ�ָýӿڵ�ʵ����
 * һ����ȡʵ����Ĺ�����
 */

public interface WeatherForecastDBInterface {
	
	public void saveAreas(List<Areas> areas);
	
	public List<Areas> loadAreas();
}
