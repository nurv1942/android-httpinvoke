package com.example.android_httpinvoke;

import java.net.SocketTimeoutException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * 
 * @author wsh HTTP «Î«Û
 */
public class HttpInvoke {

	private static final String TAG = "HttpInvoke";
	private static boolean DEBUG_MODE = true;

	private static OnPreExecuteListener mPreExecuteListener = null;
	private static OnPostExecuteListener mPostExecuteListener = null;
	public static Context mContext = null;
	private static String mException = null;

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public interface OnJsonObjectResultListener {
		void onOK(JSONObject result);

		void onNG(String reason);

		void onCancel();
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public interface OnJsonArrayResultListener {
		void onOK(JSONArray result);

		void onNG(String reason);

		void onCancel();
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public interface OnStringResultListener {
		void onOK(String result);

		void onNG(String reason);

		void onCancel();
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public interface OnPreExecuteListener {
		void onPreExecute();
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public interface OnPostExecuteListener {
		void onPostExecute();
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokeGetJsonObject(String url, Context context, OnJsonObjectResultListener jsonObjectResultListener) {
		asyncInvokeGetJsonObject(url, NetWorkConfigure.getInstance().getTIMEOUT_DEFAULT(), context, jsonObjectResultListener);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokeGetJsonObject(String url, int timeout, Context context, OnJsonObjectResultListener jsonObjectResultListener) {
		asyncInvokeGetJsonObject(url, timeout, context, jsonObjectResultListener, null, null);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokeGetJsonObject(String url, int timeout, Context context, OnJsonObjectResultListener jsonObjectResultListener, OnPreExecuteListener preExecuteListener, OnPostExecuteListener postExecuteListener) {
		asyncInvokeDoJsonObject(url, null, false, timeout, context, jsonObjectResultListener, preExecuteListener, postExecuteListener);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokePostJsonObject(JSONObject param, Context context, OnJsonObjectResultListener jsonObjectResultListener) {
		asyncInvokePostJsonObject(param, NetWorkConfigure.getInstance().getTIMEOUT_DEFAULT(), context, jsonObjectResultListener);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokePostJsonObject(JSONObject param, int timeout, Context context, OnJsonObjectResultListener jsonObjectResultListener) {
		asyncInvokePostJsonObject(param, timeout, context, jsonObjectResultListener, null, null);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokePostJsonObject(JSONObject param, int timeout, Context context, final OnJsonObjectResultListener jsonObjectResultListener, OnPreExecuteListener preExecuteListener, OnPostExecuteListener postExecuteListener) {
		asyncInvokeDoJsonObject(null, param, true, timeout, context, jsonObjectResultListener, preExecuteListener, postExecuteListener);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static JSONObject invokeGetJsonObject(String url) {
		return invokeGetJsonObject(url, NetWorkConfigure.getInstance().getTIMEOUT_DEFAULT());
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static JSONObject invokeGetJsonObject(String url, int timeout) {
		return invokeDoJsonObject(url, null, false, timeout, false);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static JSONObject invokePostJsonObject(JSONObject param) {
		return invokePostJsonObject(param, NetWorkConfigure.getInstance().getTIMEOUT_DEFAULT());
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static JSONObject invokePostJsonObject(JSONObject param, int timeout) {
		return invokeDoJsonObject(null, param, true, timeout, false);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	private static void asyncInvokeDoJsonObject(String url, JSONObject param, boolean postFlag, int timeout, Context context, final OnJsonObjectResultListener jsonObjectResultListener, OnPreExecuteListener preExecuteListener, OnPostExecuteListener postExecuteListener) {
		mContext = context;
		mPreExecuteListener = preExecuteListener;
		mPostExecuteListener = postExecuteListener;
		// CheckNetwork checkNetwork = new CheckNetwork(mContext);
		// if(checkNetwork.getNetworkState(true)) {
		new AsyncTask<Object, Integer, JSONObject>() {

			@Override
			protected void onPreExecute() {
				if (mPreExecuteListener != null) {
					mPreExecuteListener.onPreExecute();
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			protected JSONObject doInBackground(Object... params) {
				return invokeDoJsonObject((String) params[0], (JSONObject) params[1], (Boolean) params[2], (Integer) params[3], true);
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				if (result == null) {
					if (jsonObjectResultListener != null) {
						jsonObjectResultListener.onNG(mException);
					}
				} else {
					jsonObjectResultListener.onOK(result);
				}
				if (mPostExecuteListener != null) {
					mPostExecuteListener.onPostExecute();
				}
			}
		}.execute(new Object[] { url, param, postFlag, Integer.valueOf(timeout) });
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	@SuppressWarnings("unused")
	private static JSONObject invokeDoJsonObject(String url, JSONObject param, boolean postFlag, int timeout, boolean asyncFlag) {
		if (postFlag && param == null) {
			mException = "Use POST method, param cannot be null.";
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		}
		if (!postFlag && url == null) {
			mException = "Use GET method, url cannot be null.";
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		}
		// StrictMode.ThreadPolicy policy = new
		// StrictMode.ThreadPolicy.Builder().permitAll().build();
		// StrictMode.setThreadPolicy(policy);
		try {
			if (postFlag) {
				if (DEBUG_MODE)
					Log.i(TAG, "request: " + NetWorkConstant.mServerUrl + param.toString());
				HttpPost request = null;
				request = new HttpPost(NetWorkConstant.mServerUrl);
				if (!NetWorkConstant.Cookie.equals("")) {
					request.addHeader("cookie", NetWorkConstant.Cookie);

				}
				StringEntity entity = new StringEntity(param.toString(), NetWorkConfigure.getInstance().getENCODING());
				request.setEntity(entity);

				HttpResponse httpResponse = new DefaultHttpClient().execute(request);
				if (httpResponse == null) {
					throw new Exception("HttpResponse is null.");
				}
				/*
				 * if(httpResponse.getStatusLine().getStatusCode() != 200) {
				 * throw new
				 * Exception(httpResponse.getStatusLine().getReasonPhrase()); }
				 */
				String retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				String cookie = (String) httpResponse.getLastHeader("Set-Cookie").toString();
				// Log.d(TAG, cookie);
				JSONObject result = new JSONObject(retSrc);
				// Log.i(TAG,result.toString());
				if (DEBUG_MODE) {
					cookie = cookie.substring(12);
					// Log.i(TAG, "response: " + cookie);
					NetWorkConstant.Cookie = cookie;
				}
				if (!asyncFlag) {
					initConfigure();
				}
				return result;
			} else {
				HttpGet request = null;
				if (url.contains("http://")) {
					if (DEBUG_MODE)
						// Log.i(TAG, "request:" + url);
						request = new HttpGet(url);
				} else {
					if (DEBUG_MODE)
						// Log.i(TAG, "request:" + Config.mServerUrl + "?" +
						// url);
						request = new HttpGet(NetWorkConstant.mServerUrl + "?" + url);
				}
				HttpPost requests = null;
				requests = new HttpPost(NetWorkConstant.mServerUrl);
				StringEntity entity = new StringEntity(param.toString(), "GBK");
				requests.setEntity(entity);
				HttpResponse httpResponse = new DefaultHttpClient().execute(request);
				if (httpResponse == null) {
					throw new Exception("HttpResponse is null.");
				}
				if (httpResponse.getStatusLine().getStatusCode() != 200) {
					throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
				}
				String retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				JSONObject result = new JSONObject(retSrc);
				if (DEBUG_MODE)
					// Log.i(TAG, "response: " + result.toString());
					if (!asyncFlag) {
						initConfigure();
					}
				return result;
			}
		} catch (ConnectTimeoutException e) {
			if (e != null) {
				mException = e.toString();
			} else {
				mException = "Connect timeout.";
			}
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		} catch (SocketTimeoutException e) {
			if (e != null) {
				mException = e.toString();
			} else {
				mException = "Socket timeout.";
			}
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		} catch (Exception e) {
			if (e != null) {
				mException = e.toString();
			} else {
				mException = "Null exception.";
			}
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		}
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokeGetJsonArray(String url, Context context, OnJsonArrayResultListener jsonArrayResultListener) {
		asyncInvokeGetJsonArray(url, NetWorkConfigure.getInstance().getTIMEOUT_DEFAULT(), context, jsonArrayResultListener);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokeGetJsonArray(String url, int timeout, Context context, OnJsonArrayResultListener jsonArrayResultListener) {
		asyncInvokeGetJsonArray(url, timeout, context, jsonArrayResultListener, null, null);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokeGetJsonArray(String url, int timeout, Context context, OnJsonArrayResultListener jsonArrayResultListener, OnPreExecuteListener preExecuteListener, OnPostExecuteListener postExecuteListener) {
		asyncInvokeDoJsonArray(url, null, false, timeout, context, jsonArrayResultListener, preExecuteListener, postExecuteListener);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokePostJsonArray(JSONObject param, Context context, OnJsonArrayResultListener jsonArrayResultListener) {
		asyncInvokePostJsonArray(param, NetWorkConfigure.getInstance().getTIMEOUT_DEFAULT(), context, jsonArrayResultListener);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokePostJsonArray(JSONObject param, int timeout, Context context, OnJsonArrayResultListener jsonArrayResultListener) {
		asyncInvokePostJsonArray(param, timeout, context, jsonArrayResultListener, null, null);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokePostJsonArray(JSONObject param, int timeout, Context context, final OnJsonArrayResultListener jsonArrayResultListener, OnPreExecuteListener preExecuteListener, OnPostExecuteListener postExecuteListener) {
		asyncInvokeDoJsonArray(null, param, true, timeout, context, jsonArrayResultListener, preExecuteListener, postExecuteListener);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static JSONArray invokeGetJsonArray(String url) {
		return invokeGetJsonArray(url, NetWorkConfigure.getInstance().getTIMEOUT_DEFAULT());
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static JSONArray invokeGetJsonArray(String url, int timeout) {
		return invokeDoJsonArray(url, null, false, timeout, false);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static JSONArray invokePostJsonArray(JSONObject param) {
		return invokePostJsonArray(param, NetWorkConfigure.getInstance().getTIMEOUT_DEFAULT());
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static JSONArray invokePostJsonArray(JSONObject param, int timeout) {
		return invokeDoJsonArray(null, param, true, timeout, false);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	private static void asyncInvokeDoJsonArray(String url, JSONObject param, boolean postFlag, int timeout, Context context, final OnJsonArrayResultListener jsonArrayResultListener, OnPreExecuteListener preExecuteListener, OnPostExecuteListener postExecuteListener) {
		mContext = context;
		mPreExecuteListener = preExecuteListener;
		mPostExecuteListener = postExecuteListener;
		// CheckNetwork checkNetwork = new CheckNetwork(mContext);
		// if(checkNetwork.getNetworkState(true)) {
		new AsyncTask<Object, Integer, JSONArray>() {

			@Override
			protected void onPreExecute() {
				if (mPreExecuteListener != null) {
					mPreExecuteListener.onPreExecute();
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			protected JSONArray doInBackground(Object... params) {
				return invokeDoJsonArray((String) params[0], (JSONObject) params[1], (Boolean) params[2], (Integer) params[3], true);
			}

			@Override
			protected void onPostExecute(JSONArray result) {
				if (result == null) {
					if (jsonArrayResultListener != null) {
						jsonArrayResultListener.onNG(mException);
					}
				} else {
					jsonArrayResultListener.onOK(result);
				}
				if (mPostExecuteListener != null) {
					mPostExecuteListener.onPostExecute();
				}
			}
		}.execute(new Object[] { url, param, postFlag, Integer.valueOf(timeout) });
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	@SuppressWarnings("unused")
	private static JSONArray invokeDoJsonArray(String url, JSONObject param, boolean postFlag, int timeout, boolean asyncFlag) {
		if (postFlag && param == null) {
			mException = "Use POST method, param cannot be null.";
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		}
		if (!postFlag && url == null) {
			mException = "Use GET method, url cannot be null.";
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		}
		/*
		 * StrictMode.ThreadPolicy policy = new
		 * StrictMode.ThreadPolicy.Builder().permitAll().build();
		 * StrictMode.setThreadPolicy(policy);
		 */
		try {
			if (postFlag) {
				if (DEBUG_MODE)
					Log.i(TAG, "request: " + NetWorkConstant.mServerUrl + param.toString());
				HttpPost request = null;
				request = new HttpPost(NetWorkConstant.mServerUrl);
				// HttpEntity entity = new UrlEncodedFormEntity(param, "UTF-8");
				StringEntity entity = new StringEntity(param.toString(), "GBK");
				request.setEntity(entity);

				HttpResponse httpResponse = new DefaultHttpClient().execute(request);
				if (httpResponse == null) {
					throw new Exception("HttpResponse is null.");
				}
				/*
				 * if(httpResponse.getStatusLine().getStatusCode() != 200) {
				 * throw new
				 * Exception(httpResponse.getStatusLine().getReasonPhrase()); }
				 */
				String retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// Log.i(TAG, "response: " + retSrc);
				JSONArray result = new JSONArray(retSrc);
				if (DEBUG_MODE)
					Log.i(TAG, "response: " + result.toString());
				if (!asyncFlag) {
					initConfigure();
				}
				return result;
			} else {
				HttpGet request = null;
				if (url.contains("http://")) {
					if (DEBUG_MODE)
						Log.i(TAG, "request:" + url);
					request = new HttpGet(url);
				} else {
					if (DEBUG_MODE)
						Log.i(TAG, "request:" + NetWorkConstant.mServerUrl + "?" + url);
					request = new HttpGet(NetWorkConstant.mServerUrl + "?" + url);
				}
				BasicHttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
				HttpConnectionParams.setSoTimeout(httpParams, timeout);
				HttpResponse httpResponse = new DefaultHttpClient(httpParams).execute(request);
				if (httpResponse == null) {
					throw new Exception("HttpResponse is null.");
				}
				if (httpResponse.getStatusLine().getStatusCode() != 200) {
					throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
				}
				String retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				JSONArray result = new JSONArray(retSrc);
				if (DEBUG_MODE)
					Log.i(TAG, "response: " + result.toString());
				if (!asyncFlag) {
					initConfigure();
				}
				return result;
			}
		} catch (ConnectTimeoutException e) {
			if (e != null) {
				mException = e.toString();
			} else {
				mException = "Connect timeout.";
			}
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		} catch (SocketTimeoutException e) {
			if (e != null) {
				mException = e.toString();
			} else {
				mException = "Socket timeout.";
			}
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		} catch (Exception e) {
			if (e != null) {
				mException = e.toString();
			} else {
				mException = "Null exception.";
			}
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		}
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokeGetString(String url, Context context, OnStringResultListener jsonArrayResultListener) {
		asyncInvokeGetString(url, NetWorkConfigure.getInstance().getTIMEOUT_DEFAULT(), context, jsonArrayResultListener);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokeGetString(String url, int timeout, Context context, OnStringResultListener jsonArrayResultListener) {
		asyncInvokeGetString(url, timeout, context, jsonArrayResultListener, null, null);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokeGetString(String url, int timeout, Context context, OnStringResultListener jsonArrayResultListener, OnPreExecuteListener preExecuteListener, OnPostExecuteListener postExecuteListener) {
		asyncInvokeDoString(url, null, false, timeout, context, jsonArrayResultListener, preExecuteListener, postExecuteListener);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokePostString(List<NameValuePair> param, Context context, OnStringResultListener jsonArrayResultListener) {
		asyncInvokePostString(param, NetWorkConfigure.getInstance().getTIMEOUT_DEFAULT(), context, jsonArrayResultListener);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokePostString(List<NameValuePair> param, int timeout, Context context, OnStringResultListener jsonArrayResultListener) {
		asyncInvokePostString(param, timeout, context, jsonArrayResultListener, null, null);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static void asyncInvokePostString(List<NameValuePair> param, int timeout, Context context, final OnStringResultListener jsonArrayResultListener, OnPreExecuteListener preExecuteListener, OnPostExecuteListener postExecuteListener) {
		asyncInvokeDoString(null, param, true, timeout, context, jsonArrayResultListener, preExecuteListener, postExecuteListener);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static String invokeGetString(String url) {
		return invokeGetString(url, NetWorkConfigure.getInstance().getTIMEOUT_DEFAULT());
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static String invokeGetString(String url, int timeout) {
		return invokeDoString(url, null, false, timeout, false);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static String invokePostString(List<NameValuePair> param) {
		return invokePostString(param, NetWorkConfigure.getInstance().getTIMEOUT_DEFAULT());
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	public static String invokePostString(List<NameValuePair> param, int timeout) {
		return invokeDoString(null, param, true, timeout, false);
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	private static void asyncInvokeDoString(String url, List<NameValuePair> param, boolean postFlag, int timeout, Context context, final OnStringResultListener stringResultListener, OnPreExecuteListener preExecuteListener, OnPostExecuteListener postExecuteListener) {
		mContext = context;
		mPreExecuteListener = preExecuteListener;
		mPostExecuteListener = postExecuteListener;
		// CheckNetwork checkNetwork = new CheckNetwork(mContext);
		// if(checkNetwork.getNetworkState(true)) {
		new AsyncTask<Object, Integer, String>() {

			@Override
			protected void onPreExecute() {
				if (mPreExecuteListener != null) {
					mPreExecuteListener.onPreExecute();
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			protected String doInBackground(Object... params) {
				return invokeDoString((String) params[0], (List<NameValuePair>) params[1], (Boolean) params[2], (Integer) params[3], true);
			}

			@Override
			protected void onPostExecute(String result) {
				if (result == null) {
					if (stringResultListener != null) {
						stringResultListener.onNG(mException);
					}
				} else {
					stringResultListener.onOK(result);
				}
				if (mPostExecuteListener != null) {
					mPostExecuteListener.onPostExecute();
				}
			}
		}.execute(new Object[] { url, param, postFlag, Integer.valueOf(timeout) });
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	@SuppressWarnings("unused")
	private static String invokeDoString(String url, List<NameValuePair> param, boolean postFlag, int timeout, boolean asyncFlag) {
		if (postFlag && param == null) {
			mException = "Use POST method, param cannot be null.";
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		}
		if (!postFlag && url == null) {
			mException = "Use GET method, url cannot be null.";
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		}
		/*
		 * StrictMode.ThreadPolicy policy = new
		 * StrictMode.ThreadPolicy.Builder().permitAll().build();
		 * StrictMode.setThreadPolicy(policy);
		 */
		try {
			if (postFlag) {
				if (DEBUG_MODE)
					Log.i(TAG, "request: " + NetWorkConstant.mServerUrl + param.toString());
				HttpPost request = null;
				request = new HttpPost(NetWorkConstant.mServerUrl);
				HttpEntity entity = new UrlEncodedFormEntity(param, NetWorkConfigure.getInstance().getENCODING());
				request.setEntity(entity);
				BasicHttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
				HttpConnectionParams.setSoTimeout(httpParams, timeout);
				HttpResponse httpResponse = new DefaultHttpClient(httpParams).execute(request);
				if (httpResponse == null) {
					throw new Exception("HttpResponse is null.");
				}
				if (httpResponse.getStatusLine().getStatusCode() != 200) {
					throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
				}
				String retSrc = EntityUtils.toString(httpResponse.getEntity(), NetWorkConfigure.getInstance().getENCODING());
				String result = retSrc;
				if (DEBUG_MODE)
					Log.i(TAG, "response: " + result.toString());
				if (!asyncFlag) {
					initConfigure();
				}
				return result;
			} else {
				HttpGet request = null;
				if (url.contains("http://")) {
					if (DEBUG_MODE)
						Log.i(TAG, "request:" + url);
					request = new HttpGet(url);
				} else {
					if (DEBUG_MODE)
						Log.i(TAG, "request:" + NetWorkConstant.mServerUrl + "?" + url);
					request = new HttpGet(NetWorkConstant.mServerUrl + "?" + url);
				}
				BasicHttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
				HttpConnectionParams.setSoTimeout(httpParams, timeout);
				HttpResponse httpResponse = new DefaultHttpClient(httpParams).execute(request);
				if (httpResponse == null) {
					throw new Exception("HttpResponse is null.");
				}
				if (httpResponse.getStatusLine().getStatusCode() != 200) {
					throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
				}
				String retSrc = EntityUtils.toString(httpResponse.getEntity(), NetWorkConfigure.getInstance().getENCODING());
				String result = retSrc;
				if (DEBUG_MODE)
					Log.i(TAG, "response: " + result.toString());
				if (!asyncFlag) {
					initConfigure();
				}
				return result;
			}
		} catch (ConnectTimeoutException e) {
			if (e != null) {
				mException = e.toString();
			} else {
				mException = "Connect timeout.";
			}
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		} catch (SocketTimeoutException e) {
			if (e != null) {
				mException = e.toString();
			} else {
				mException = "Socket timeout.";
			}
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		} catch (Exception e) {
			if (e != null) {
				mException = e.toString();
			} else {
				mException = "Null exception.";
			}
			if (!asyncFlag) {
				initConfigure();
			}
			return null;
		}
	}

	/**
	 * 
	 * @author wsh
	 * 
	 */
	private static void initConfigure() {
		mPreExecuteListener = null;
		mPostExecuteListener = null;
		mContext = null;
		mException = null;
	}

}
