package cn.it.weatherforecast.util;

/*
 * �������߳��д����ϻ�ȡ��Ϣʱ�Ļص�����
 * ���������߳����޷�����return���ػ�ȡ�����ݣ������ûص��������Խ�
 * �������߳��н����ݻص��õ����÷����ķ������У���������߳������ݷ��ص�����
 */
public interface HttpCallbackListenerForJson {

	public void onFinish(String response);

	public void onError(Exception e);
}
