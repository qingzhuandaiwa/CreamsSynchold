package com.yinkun.workgo.test.kit;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpHelper {
	private static final String TAG = "HttpUtils";
	private static final int mReadTimeOut = 1000 * 10; // 10秒
	private static final int mConnectTimeOut = 1000 * 5; // 5秒
	private static final String CHAR_SET = "UTF-8";
	private static final int mRetry = 2; // 默认尝试访问次数

	public static String get(String url) throws Exception {
		return get(url, null);
	}

	public static String get(String url, Map<String, ? extends Object> params) throws Exception {
		return get(url, params, null);
	}

	public static String get(String url, Map<String, ? extends Object> params, Map<String, String> headers)
			throws Exception {
		if (url == null || url.trim().length() == 0) {
			throw new Exception(TAG + ": url is null or empty!");
		}

		if (params != null && params.size() > 0) {
			if (!url.contains("?")) {
				url += "?";
			}

			if (url.charAt(url.length() - 1) != '?') {
				url += "&";
			}

			url += buildParams(params);
		}

		return tryToGet(url, headers);
	}

	public static String buildParams(Map<String, ? extends Object> params) throws UnsupportedEncodingException {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, ? extends Object> entry : params.entrySet()) {
			if (entry.getKey() != null && entry.getValue() != null)
				builder.append(entry.getKey().trim()).append("=")
						.append(URLEncoder.encode(entry.getValue().toString(), CHAR_SET)).append("&");
		}

		if (builder.charAt(builder.length() - 1) == '&') {
			builder.deleteCharAt(builder.length() - 1);
		}

		return builder.toString();
	}

	private static String tryToGet(String url, Map<String, String> headers) throws Exception {
		int tryTime = 0;
		Exception ex = null;
		while (tryTime < mRetry) {
			try {
				return doGet(url, headers);
			} catch (Exception e) {
				if (e != null)
					ex = e;
				tryTime++;
			}
		}
		if (ex != null)
			throw ex;
		else
			throw new Exception("未知网络错误 ");
	}

	private static String doGet(String strUrl, Map<String, String> headers) throws Exception {
		HttpURLConnection connection = null;
		InputStream stream = null;
		try {

			connection = getConnection(strUrl);
			configConnection(connection);
			if (headers != null && headers.size() > 0) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			connection.setInstanceFollowRedirects(true);
			connection.connect();

			stream = connection.getInputStream();
			ByteArrayOutputStream obs = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			for (int len = 0; (len = stream.read(buffer)) > 0;) {
				obs.write(buffer, 0, len);
			}
			obs.flush();
			obs.close();
			stream.close();

			return new String(obs.toByteArray());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (stream != null) {
				stream.close();
			}
		}
	}

	public static String post(String url) throws Exception {
		return post(url, null);
	}

	public static String post(String url, Map<String, ? extends Object> params) throws Exception {
		return post(url, params, null);
	}

	public static String post(String url, Map<String, ? extends Object> params, Map<String, String> headers)
			throws Exception {
		if (url == null || url.trim().length() == 0) {
			throw new Exception(TAG + ":url is null or empty!");
		}

		if (params != null && params.size() > 0) {
			return tryToPost(url, buildParams(params), headers);
		} else {
			return tryToPost(url, null, headers);
		}
	}

	public static String post(String url, String content, Map<String, String> headers) throws Exception {
		return tryToPost(url, content, headers);
	}

	private static String tryToPost(String url, String postContent, Map<String, String> headers) throws Exception {
		int tryTime = 0;
		Exception ex = null;
		while (tryTime < mRetry) {
			try {
				return doPost(url, postContent, headers);
			} catch (Exception e) {
				if (e != null)
					ex = e;
				tryTime++;
			}
		}
		if (ex != null)
			throw ex;
		else
			throw new Exception("未知网络错误 ");
	}

	private static String doPost(String strUrl, String postContent, Map<String, String> headers) throws Exception {
		HttpURLConnection connection = null;
		InputStream stream = null;
		try {
			connection = getConnection(strUrl);
			configConnection(connection);
			if (headers != null && headers.size() > 0) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			if (null != postContent && !"".equals(postContent)) {
				DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
				dos.write(postContent.getBytes(CHAR_SET));
				dos.flush();
				dos.close();
			}
			stream = connection.getInputStream();
			ByteArrayOutputStream obs = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			for (int len = 0; (len = stream.read(buffer)) > 0;) {
				obs.write(buffer, 0, len);
			}
			obs.flush();
			obs.close();

			return new String(obs.toByteArray());

		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (stream != null) {
				stream.close();
			}
		}

	}

	private static void configConnection(HttpURLConnection connection) {
		if (connection == null)
			return;
		connection.setReadTimeout(mReadTimeOut);
		connection.setConnectTimeout(mConnectTimeOut);

		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
	}

	private static HttpURLConnection getConnection(String strUrl) throws Exception {
		if (strUrl == null) {
			return null;
		}
		if (strUrl.toLowerCase().startsWith("https")) {
			return getHttpsConnection(strUrl);
		} else {
			return getHttpConnection(strUrl);
		}
	}

	private static HttpURLConnection getHttpConnection(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		return conn;
	}

	private static HttpsURLConnection getHttpsConnection(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setHostnameVerifier(hnv);
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		if (sslContext != null) {
			TrustManager[] tm = { xtm };
			sslContext.init(null, tm, null);
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			conn.setSSLSocketFactory(ssf);
		}

		return conn;
	}

	private static X509TrustManager xtm = new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};

	private static HostnameVerifier hnv = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * 发送HttpPost请求 
	 * @author taoliangfei
	 * 2017年6月9日 下午4:19:52
	 */
    public static String doPost(String strURL, String params) {  
        System.out.println(strURL);  
        System.out.println(params);  
        try {  
            URL url = new URL(strURL);// 创建连接  
            HttpURLConnection connection = (HttpURLConnection) url  
                    .openConnection();  
            connection.setDoOutput(true);  
            connection.setDoInput(true);  
            connection.setUseCaches(false);  
            connection.setInstanceFollowRedirects(true);  
            connection.setRequestMethod("POST"); // 设置请求方式  
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式  
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式  
            connection.connect();  
            OutputStreamWriter out = new OutputStreamWriter(  
                    connection.getOutputStream(), "UTF-8"); // utf-8编码  
            out.append(params);  
            out.flush();  
            out.close();  
            // 读取响应  
            int length = (int) connection.getContentLength();// 获取长度  
            InputStream is = connection.getInputStream();  
            if (length != -1) {  
                byte[] data = new byte[length];  
                byte[] temp = new byte[512];  
                int readLen = 0;  
                int destPos = 0;  
                while ((readLen = is.read(temp)) > 0) {  
                    System.arraycopy(temp, 0, data, destPos, readLen);  
                    destPos += readLen;  
                }  
                String result = new String(data, "UTF-8"); // utf-8编码  
                System.out.println(result);  
                return result;  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return "error";
    }  
    
    /**
     * 上传微信图片素材
     * @author taoliangfei
     * 2017年7月3日 上午10:06:11
     */
    public static  String connectHttpsByPost(String path, File file) throws Exception {
        URL urlObj = new URL(path);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        String result = null;
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type",
                "multipart/form-data; boundary="
                        + BOUNDARY);
        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" + file.length() + "\";filename=\""

                + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);
        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return result;
    }
    
    public static String getSHA256StrJava(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

}
