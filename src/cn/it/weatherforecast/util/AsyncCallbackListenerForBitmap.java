package cn.it.weatherforecast.util;


public interface AsyncCallbackListenerForBitmap {

	public void onFinish(byte[] photo);

	public void onError(String errorMessage);
}
