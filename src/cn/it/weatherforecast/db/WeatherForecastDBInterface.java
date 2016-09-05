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
	
	public void saveAreas(List<Areas> areas);
	
	public List<Areas> loadAreas();
	
	public List<Areas> loadAreas(String s);
	
	public List<SelectedAreas> loadSelectedAreas();
	
	public void saveSelectedAreaCode(String code,String name);
	
	public void deleteSelectedAreas(String selectId);
}
