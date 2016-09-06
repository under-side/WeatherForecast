package cn.it.weatherforecast.model;

/*
 *建立实体类，来和数据库中的数据一一对应，从而方便快捷的访问后台数据库
 *实现了SQLite与逻辑代码之间的分离
 */
public class Areas {

	private String mCityName;
	private String mCityId;
	private String mCityLat;
	private String mCityLon;

	public String getCityName() {
		return mCityName;
	}

	public void setCityName(String cityName) {
		mCityName = cityName;
	}

	public String getCityId() {
		return mCityId;
	}

	public void setCityId(String cityId) {
		mCityId = cityId;
	}

	public String getCityLat() {
		return mCityLat;
	}

	public void setCityLat(String cityLat) {
		mCityLat = cityLat;
	}

	public String getCityLon() {
		return mCityLon;
	}

	public void setCityLon(String cityLon) {
		mCityLon = cityLon;
	}

}
