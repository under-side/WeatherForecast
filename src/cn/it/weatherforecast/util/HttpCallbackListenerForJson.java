package cn.it.weatherforecast.util;

/*
 * 开启子线程中从网上获取信息时的回调函数
 * 由于在子线程中无法运用return返回获取的数据，则运用回调函数可以解
 * 决从子线程中将数据回调用到调用方法的方法体中，解决了子线程中数据返回的难题
 */
public interface HttpCallbackListenerForJson {

	public void onFinish(String response);

	public void onError(Exception e);
}
