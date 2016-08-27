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
	 * 运用的是HttpURLConnection来下载网上数据 待解决的问题： 获取组件宽高，然后去压缩图片，从而减少内存的使用，达到优化目的
	 */
	static String mHttpResponse;

	// 下载文本文件
	protected static String downTextByHttpUrlConnection(String urlText) {
		// TODO Auto-generated method stub
		URL url;
		try {
			// URi 跟URL的生成方式不同 Uri.paser("http://www.baidu.com")
			url = new URL(urlText);
			// 不使用这个
			// URLConnection connection= url.openConnection();
			// 把URLConnection强制转换为
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			// 设置基本配置信息
			// 设置连接超时,网络连接相对耗时些，
			connection.setConnectTimeout(20 * 1000);
			// 设置读写超时，已经建立到了通道，时间稍短些
			connection.setReadTimeout(3 * 1000);

			// 设置扩展的基本信息
			// 是否使用本地缓存中的缓存数据
			connection.setUseCaches(false);
			// 是否启用网址重定向
			HttpURLConnection.setFollowRedirects(true);
			// 支持中文
			connection.setRequestProperty("Accept-Language", "zh-CN");
			// 报文中字符采用编码格式
			connection.setRequestProperty("Charset", "UTF-8");
			// 设置网络的连接属性：
			// 长连接(本次使用完毕后，放入连接池，跟服务器端继续保持连接，
			// 下次使用时,直接从连接池中拿出重复使用，节省了连接时间)，
			// 短连接(本次使用完毕后，释放连接资源,下一次使用时重新连接)
			connection.setRequestProperty("Connection", "Keep-Alive");

			// 设置网络请求方式:GET POST
			connection.setRequestMethod("GET");

			// 省略跟服务器的通讯过程，由内部自动执行

			// 获取返回码(返回代码和代码描述)
			int code = connection.getResponseCode();
			String msg = connection.getResponseMessage();
			// 如果本次返回数据是有效的，则返回码为200
			if (code == HttpURLConnection.HTTP_OK) {
				// 不完善的解析方式：跳过回应头的描述，直接解析返回内容;
				// 获取正文部分的长度, 注意：部分不规范的回应中可能取不到值
				int leng = connection.getContentLength();
				String enCode = connection.getContentEncoding();
				// 直接获取正文部分对应的输入流
				InputStream is = connection.getInputStream();
				// 我们知道百度返回正文部分是文本,利用对方返回的字符编码生成字符流
				InputStreamReader isr;
				if (enCode == null) {
					isr = new InputStreamReader(is);
				} else {
					isr = new InputStreamReader(is, enCode);
				}
				BufferedReader bR = new BufferedReader(isr);
				StringBuffer sBuffer = new StringBuffer();

				// 读返回的正文内容
				String line;
				while ((line = bR.readLine()) != null) {
					sBuffer.append(line + "\n");
					LogUtil.d("HttpURLConnection1", "" + sBuffer);
				}
				mHttpResponse = sBuffer.toString();
				// 关闭流
				bR.close();
				// 如果是短连接则断开网络，释放网络资源
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

	// 下载图片
	protected static byte[] downTextByHttpUrlConnectx(String urlText) {
		// TODO Auto-generated method stub
		Bitmap bitmap = null;
		URL url;
		try {
			// URi 跟URL的生成方式不同 Uri.paser("http://www.baidu.com")
			url = new URL(urlText);
			// 不使用这个
			// URLConnection connection= url.openConnection();
			// 把URLConnection强制转换为
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			// 设置基本配置信息
			// 设置连接超时,网络连接相对耗时些，
			connection.setConnectTimeout(20 * 1000);
			// 设置读写超时，已经建立到了通道，时间稍短些
			connection.setReadTimeout(3 * 1000);

			// 设置扩展的基本信息
			// 是否使用本地缓存中的缓存数据
			connection.setUseCaches(false);
			// 是否启用网址重定向
			HttpURLConnection.setFollowRedirects(true);
			// 支持中文
			connection.setRequestProperty("Accept-Language", "zh-CN");
			// 报文中字符采用编码格式
			connection.setRequestProperty("Charset", "UTF-8");
			// 设置网络的连接属性：
			// 长连接(本次使用完毕后，放入连接池，跟服务器端继续保持连接，
			// 下次使用时,直接从连接池中拿出重复使用，节省了连接时间)，
			// 短连接(本次使用完毕后，释放连接资源,下一次使用时重新连接)
			connection.setRequestProperty("Connection", "Keep-Alive");

			// 设置网络请求方式:GET POST
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
		// 8888的格式时，一个像素点占用4个字节，最费内存的一种方式
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