package cn.it.weatherforecast.util;

/*
 * 该接口为在子线程中下载图片后的回调函数
 */
public interface AsyncCallbackListenerForBitmap {

	public void onFinish(byte[] photo);

	public void onError(String errorMessage);
}
