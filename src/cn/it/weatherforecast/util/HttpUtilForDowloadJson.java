package cn.it.weatherforecast.util;


/*
 * �����������HttpURLConnection����ȡ��������
 */
public class HttpUtilForDowloadJson {

	public static void sendHttpRequest(final String address,
			final HttpCallbackListenerForJson listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				String response = HttpDownloadUtil
						.downTextByHttpUrlConnection(address);
				if (response != null) {
					listener.onFinish(response);
				}
			}
		}).start();
	}
}