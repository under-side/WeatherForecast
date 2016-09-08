package cn.it.weatherforecast.db;

import java.util.List;

import cn.it.weatherforecast.model.Areas;
import cn.it.weatherforecast.model.SelectedAreas;

/*
 * 该接口是为对SQLite数据进行操作的一个规范，只能使用定义的方法对其数据进行操作
 * 对SQLite中数据的操作，类似于DAO设计模式
 * 一个创建数据库类
 * 一个与数据库中数据一一对应的实体类
 * 一个对数据库操作的规范接口
 * 一个实现该接口的实现类
 * 一个获取实现类的工厂类
 */

public interface WeatherForecastDBInterface {

	//保存城市信息到SQLite中
	public void saveAreas(List<Areas> areas);

	//从SQLite中获取城市表
	public List<Areas> loadAreas();

	//根据指定名称，模糊获取符合条件的城市列表
	public List<Areas> loadAreas(String s);

	//获取已经选择城市列表的SQLite中
	public List<SelectedAreas> loadSelectedAreas();

	//保存指定的城市信息到SQLite中的选择城市列表中
	public void saveSelectedAreasInfo(String code, String name, long updateTime);

	//根据指定的城市code，删除SQLite中的选择城市
	public void deleteSelectedAreas(String selectId);
	
	//更新指定城市code中更新时间
	public void updateSelectedAreasTime(String updateCode,long updateTime);
}
