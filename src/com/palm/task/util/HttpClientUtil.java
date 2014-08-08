package com.palm.task.util;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

/**
 * httpclient使用(android内置apache)
 * 
 * @author weixiang.qin
 * 
 */
public class HttpClientUtil {
	public static final int FETECH_CONNECTION_TIME_OUT = 10000;// 从连接池中取连接的超时时间(单位毫秒)
	public static final int CONNECTION_TIME_OUT = 10000;// 设置连接超时时间(单位毫秒)
	public static final int SO_TIME_OUT = 20000;// 设置读数据超时时间(单位毫秒)
	public final static int MAX_TOTAL_CONNECTIONS = 800;// 最大连接数
	public final static int MAX_ROUTE_CONNECTIONS = 400;// 每个路由最大连接数
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String STATUSCODE_ERROR = "返回错误码异常";
	private static final String HTTP_GET = "get";
	private static final String HTTP_POST = "post";
	private static HttpClientUtil httpClientUtils;
	private HttpClient httpClient;

	private HttpClientUtil() {

	}

	public static synchronized HttpClientUtil getInstance() {
		if (httpClientUtils == null) {
			httpClientUtils = new HttpClientUtil();
		}
		return httpClientUtils;
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws Exception
	 * @throws ClientProtocolException
	 * @throws HttpStatusException
	 */
	public String sendGet(String url) throws Exception {
		return sendHttp(url, HTTP_GET, null, null);
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @param paramsMap
	 * @return
	 * @throws Exception
	 */
	public String sendGet(String url, HashMap<String, String> paramsMap)
			throws Exception {
		return sendHttp(url, HTTP_GET, paramsMap, null);
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param paramsMap
	 * @return
	 * @throws Exception
	 */
	public String sendPost(String url, HashMap<String, String> paramsMap)
			throws Exception {
		return sendHttp(url, HTTP_POST, paramsMap, null);
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public String sendPost(String url, String entity) throws Exception {
		return sendHttp(url, HTTP_POST, null, entity);
	}

	/**
	 * http
	 * 
	 * @param url
	 * @param httpType
	 * @param paramsMap
	 * @param entity
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws HttpStatusException
	 */
	public String sendHttp(String url, String httpType,
			HashMap<String, String> paramsMap, String entity) throws Exception {
		HttpRequestBase httpRequest = null;
		try {
			HttpClient httpClient = getHttpClient();
			LinkedList<BasicNameValuePair> params = getParams(paramsMap);
			if (HTTP_GET.equals(httpType)) {
				if (params != null) {
					String param = URLEncodedUtils.format(params, CHARSET_UTF8);
					url = url + "?" + param;
				}
				httpRequest = new HttpGet(url);
			} else if (HTTP_POST.equals(httpType)) {
				httpRequest = new HttpPost(url);
				if (params != null) {
					((HttpPost) httpRequest)
							.setEntity(new UrlEncodedFormEntity(params,
									CHARSET_UTF8));
				} else if (entity != null) {
					((HttpPost) httpRequest).setEntity(new StringEntity(entity,
							CHARSET_UTF8));
				}
			}
			HttpResponse response = httpClient.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new HttpStatusException(STATUSCODE_ERROR);
			}
			return EntityUtils.toString(response.getEntity(), CHARSET_UTF8);
		} finally {
			if (httpRequest != null) {
				httpRequest.abort();
			}
		}
	}

	/**
	 * 获取参数
	 * 
	 * @param paramsMap
	 * @return
	 */
	private LinkedList<BasicNameValuePair> getParams(
			HashMap<String, String> paramsMap) {
		if (paramsMap == null) {
			return null;
		}
		LinkedList<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		Set<String> keys = paramsMap.keySet();
		for (String key : keys) {
			params.add(new BasicNameValuePair(key, paramsMap.get(key)));
		}
		return params;
	}

	/**
	 * 下载文件
	 * 
	 * @param url
	 * @param file
	 * @return
	 */
	public void downloadFile(String url, File file) throws Exception {
		HttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		HttpResponse response = httpClient.execute(httpPost);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new HttpStatusException(STATUSCODE_ERROR);
		}
		FileUtil.streamToFile(response.getEntity().getContent(), file);
	}

	/**
	 * 获取httpclient
	 * 
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws UnrecoverableKeyException
	 * @throws KeyManagementException
	 * @throws IOException
	 * @throws CertificateException
	 */
	private synchronized HttpClient getHttpClient() throws Exception {
		if (httpClient == null) {
			HttpParams params = new BasicHttpParams();
			// 最大连接数
			ConnManagerParams.setMaxTotalConnections(params,
					MAX_TOTAL_CONNECTIONS);
			// 设置每个路由最大连接数
			ConnManagerParams.setMaxConnectionsPerRoute(params,
					new ConnPerRouteBean(MAX_ROUTE_CONNECTIONS));
			// 从连接池中取连接的超时时间
			ConnManagerParams.setTimeout(params, FETECH_CONNECTION_TIME_OUT);
			// 连接超时
			HttpConnectionParams.setConnectionTimeout(params,
					CONNECTION_TIME_OUT);
			// 请求超时
			HttpConnectionParams.setSoTimeout(params, SO_TIME_OUT);
			// 设置参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET_UTF8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", getSSLSocketFactory(), 443));
			// 使用线程安全的连接管理来创建HttpClient
			ClientConnectionManager manager = new ThreadSafeClientConnManager(
					params, schReg);
			httpClient = new DefaultHttpClient(manager, params);
		}
		return httpClient;
	}

	/**
	 * getSSLSocketFactory
	 * 
	 * @return
	 */
	private SSLSocketFactory getSSLSocketFactory() throws Exception {
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustStore.load(null, null);
		SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
		sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return sf;
	}

	/**
	 * 重写SSLSocketFactory
	 * 
	 * @author weixiang.qin
	 * 
	 */
	class SSLSocketFactoryEx extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {

				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {

				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
