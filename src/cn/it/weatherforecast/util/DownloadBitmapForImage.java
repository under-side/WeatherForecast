package cn.it.weatherforecast.util;

import android.os.AsyncTask;

public class DownloadBitmapForImage extends AsyncTask<String, Integer, byte[]> {

	AsyncCallbackListenerForBitmap mCallbackListener;
	String errorMessage;

	public DownloadBitmapForImage(AsyncCallbackListenerForBitmap listener) {
		mCallbackListener = listener;
	}

	@Override
	protected byte[] doInBackground(String... url) {
		// TODO Auto-generated method stub
		if (url == null) {
			return null;
		}
		byte[] photo = null;

		try {
			photo = HttpDownloadUtil.downTextByHttpUrlConnectx(url[0]);
		} catch (Exception e) {
			// TODO: handle exception
			errorMessage = e.getMessage();
		}
		return photo;
	}

	@Override
	protected void onPostExecute(byte[] result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result == null || result.length < 1) {
			if (result != null) {
				mCallbackListener.onError(errorMessage);
			}
		} else {
			if (result != null) {
				mCallbackListener.onFinish(result);
			}
		}
	}
}