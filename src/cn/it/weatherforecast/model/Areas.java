package cn.it.weatherforecast.model;

/*
 *����ʵ���࣬�������ݿ��е�����һһ��Ӧ���Ӷ������ݵķ��ʺ�̨���ݿ�
 *ʵ����SQLite���߼�����֮��ķ���
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
