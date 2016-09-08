package cn.it.weatherforecast.db;

import java.util.List;

import cn.it.weatherforecast.model.Areas;
import cn.it.weatherforecast.model.SelectedAreas;

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

	//���������Ϣ��SQLite��
	public void saveAreas(List<Areas> areas);

	//��SQLite�л�ȡ���б�
	public List<Areas> loadAreas();

	//����ָ�����ƣ�ģ����ȡ���������ĳ����б�
	public List<Areas> loadAreas(String s);

	//��ȡ�Ѿ�ѡ������б��SQLite��
	public List<SelectedAreas> loadSelectedAreas();

	//����ָ���ĳ�����Ϣ��SQLite�е�ѡ������б���
	public void saveSelectedAreasInfo(String code, String name, long updateTime);

	//����ָ���ĳ���code��ɾ��SQLite�е�ѡ�����
	public void deleteSelectedAreas(String selectId);
	
	//����ָ������code�и���ʱ��
	public void updateSelectedAreasTime(String updateCode,long updateTime);
}
