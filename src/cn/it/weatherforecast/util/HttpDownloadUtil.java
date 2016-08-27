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
		try {
			// URi ��URL�����ɷ�ʽ��ͬ Uri.paser("http://www.baidu.com")
			url = new URL(urlText);
			// ��ʹ�����
			// URLConnection connection= url.openConnection();
			// ��URLConnectionǿ��ת��Ϊ
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
			// ����������������ԣ�
			// ������(����ʹ����Ϻ󣬷������ӳأ����������˼����������ӣ�
			// �´�ʹ��ʱ,ֱ�Ӵ����ӳ����ó��ظ�ʹ�ã���ʡ������ʱ��)��
			// ������(����ʹ����Ϻ��ͷ�������Դ,��һ��ʹ��ʱ��������)
			connection.setRequestProperty("Connection", "Keep-Alive");

			// ������������ʽ:GET POST
			connection.setRequestMethod("GET");

			// ʡ�Ը���������ͨѶ���̣����ڲ��Զ�ִ��

			// ��ȡ������(���ش���ʹ�������)
			int code = connection.getResponseCode();
			String msg = connection.getResponseMessage();
			// ������η�����������Ч�ģ��򷵻���Ϊ200
			if (code == HttpURLConnection.HTTP_OK) {
				// �����ƵĽ�����ʽ��������Ӧͷ��������ֱ�ӽ�����������;
				// ��ȡ���Ĳ��ֵĳ���, ע�⣺���ֲ��淶�Ļ�Ӧ�п���ȡ����ֵ
				int leng = connection.getContentLength();
				String enCode = connection.getContentEncoding();
				// ֱ�ӻ�ȡ���Ĳ��ֶ�Ӧ��������
				InputStream is = connection.getInputStream();
				// ����֪���ٶȷ������Ĳ������ı�,���öԷ����ص��ַ����������ַ���
				InputStreamReader isr;
				if (enCode == null) {
					isr = new InputStreamReader(is);
				} else {
					isr = new InputStreamReader(is, enCode);
				}
				BufferedReader bR = new BufferedReader(isr);
				StringBuffer sBuffer = new StringBuffer();

				// �����ص���������
				String line;
				while ((line = bR.readLine()) != null) {
					sBuffer.append(line + "\n");
					LogUtil.d("HttpURLConnection1", "" + sBuffer);
				}
				mHttpResponse = sBuffer.toString();
				// �ر���
				bR.close();
				// ����Ƕ�������Ͽ����磬�ͷ�������Դ
				connection.disconnect();
			} 
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mHttpResponse;
	}

	// ����ͼƬ
	protected static byte[] downTextByHttpUrlConnectx(String urlText) {
		// TODO Auto-generated method stub
		Bitmap bitmap = null;
		URL url;
		try {
			// URi ��URL�����ɷ�ʽ��ͬ Uri.paser("http://www.baidu.com")
			url = new URL(urlText);
			// ��ʹ�����
			// URLConnection connection= url.openConnection();
			// ��URLConnectionǿ��ת��Ϊ
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
			// ����������������ԣ�
			// ������(����ʹ����Ϻ󣬷������ӳأ����������˼����������ӣ�
			// �´�ʹ��ʱ,ֱ�Ӵ����ӳ����ó��ظ�ʹ�ã���ʡ������ʱ��)��
			// ������(����ʹ����Ϻ��ͷ�������Դ,��һ��ʹ��ʱ��������)
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