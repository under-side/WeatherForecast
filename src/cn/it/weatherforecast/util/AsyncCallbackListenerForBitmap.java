package cn.it.weatherforecast.util;

/*
 * �ýӿ�Ϊ�����߳�������ͼƬ��Ļص�����
 */
public interface AsyncCallbackListenerForBitmap {

	public void onFinish(byte[] photo);

	public void onError(String errorMessage);
}
