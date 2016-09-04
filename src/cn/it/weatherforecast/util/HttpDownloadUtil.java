package cn.it.weatherforecast.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpDownloadUtil {

	/*
	 * ���õ���HttpURLConnection�������������� ����������⣺ ��ȡ�����ߣ�Ȼ��ȥѹ��ͼƬ���Ӷ������ڴ��ʹ�ã��ﵽ�Ż�Ŀ��
	 */
	static String mHttpResponse;

	// �����ı��ļ�
	protected static String downTextByHttpUrlConnection(String urlText) {
		// TODO Auto-generated method stub
		URL url;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlText);
			connection = (HttpURLConnection) url.openConnection();

			// ������������ʽ:GET POST
			connection.setRequestMethod("GET");

			// �����B�ӌ���
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestProperty("Accept-Language", "zh-CN");
			
			// ������η�����������Ч�ģ��򷵻���Ϊ200
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// ֱ�ӻ�ȡ���Ĳ��ֶ�Ӧ��������
				InputStream is = connection.getInputStream();
				BufferedReader bR = new BufferedReader(
						new InputStreamReader(is));

				StringBuilder response = new StringBuilder();

				// �����ص���������
				String line;
				while ((line = bR.readLine()) != null) {
					response.append(line);
				}
				mHttpResponse = response.toString();
				// �ر���
				bR.close();

			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				// ����Ƕ�������Ͽ����磬�ͷ�������Դ
				connection.disconnect();
			}
		}
		return mHttpResponse;
	}

	// ����ͼƬ
	protected static byte[] downTextByHttpUrlConnectx(String urlText) {
		// TODO Auto-generated method stub
		Bitmap bitmap = null;
		URL url;
		try {
			url = new URL(urlText);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			// ���û���������Ϣ
			// �������ӳ�ʱ,����������Ժ�ʱЩ��
			connection.setConnectTimeout(20 * 1000);
			// ���ö�д��ʱ���Ѿ���������ͨ����ʱ���Զ�Щ
			connection.setReadTimeout(3 * 1000);

			// ������չ�Ļ�����Ϣ
			// �Ƿ�ʹ�ñ��ػ����еĻ�������
			connection.setUseCaches(false);
			// �Ƿ�������ַ�ض���
			HttpURLConnection.setFollowRedirects(true);
			// ֧������
			connection.setRequestProperty("Accept-Language", "zh-CN");
			// �������ַ����ñ����ʽ
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Connection", "Keep-Alive");

			// ������������ʽ:GET POST
			connection.setRequestMethod("GET");
			int code = connection.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {

				InputStream is = connection.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);

			}

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flattenBitmap(bitmap);

	}

	static byte[] flattenBitmap(Bitmap bitmap) {
		// Try go guesstimate how much space the icon will take when serialized
		// to avoid unnecessary allocations/copies during the write.
		// 8888�ĸ�ʽʱ��һ�����ص�ռ��4���ֽڣ�����ڴ��һ�ַ�ʽ
		int size = bitmap.getWidth() * bitmap.getHeight() * 4;
		ByteArrayOutputStream out = new ByteArrayOutputStream(size);
		try {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			return out.toByteArray();
		} catch (IOException e) {
			LogUtil.w("Favorite", "Could not write icon");
			return null;
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}