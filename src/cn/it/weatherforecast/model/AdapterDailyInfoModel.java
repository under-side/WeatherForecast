package cn.it.weatherforecast.model;

public class AdapterDailyInfoModel {

	String date, topTemperture, lowTemperture, weatherTxt;
	String code;

	public String getWeatherTxt() {
		return weatherTxt;
	}

	public void setWeatherTxt(String weatherTxt) {
		this.weatherTxt = weatherTxt;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTopTemperture() {
		return topTemperture;
	}

	public void setTopTemperture(String topTemperture) {
		this.topTemperture = topTemperture;
	}

	public String getLowTemperture() {
		return lowTemperture;
	}

	public void setLowTemperture(String lowTemperture) {
		this.lowTemperture = lowTemperture;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
