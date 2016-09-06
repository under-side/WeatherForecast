package cn.it.weatherforecast.model;

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
