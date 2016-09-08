package cn.it.weatherforecast.model;

/*
 * 此为从数据库中获取的已经选择的城市信息建立模型实体类
 */
public class SelectedAreas {

	private String mSelectedCode;
	private String mSelectedName;
	private long mUpdateTime;

	public long getUpdateTime() {
		return mUpdateTime;
	}

	public void setUpdateTime(long updateTime) {
		mUpdateTime = updateTime;
	}

	public String getSelectedCode() {
		return mSelectedCode;
	}

	public void setSelectedCode(String selectedCode) {
		mSelectedCode = selectedCode;
	}

	public String getSelectedName() {
		return mSelectedName;
	}

	public void setSelectedName(String selectedName) {
		mSelectedName = selectedName;
	}
}
