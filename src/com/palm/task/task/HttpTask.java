package com.palm.task.task;

import java.net.SocketTimeoutException;
import java.util.HashMap;

import org.apache.http.conn.ConnectTimeoutException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.palm.task.bean.req.BaseReq;
import com.palm.task.bean.resp.BaseResp;
import com.palm.task.util.ClassUtil;
import com.palm.task.util.Const;
import com.palm.task.util.HttpClientUtil;
import com.palm.task.util.JsonUtil;
import com.palm.task.util.LogUtil;
import com.palm.task.util.ThreadPoolUtil;
import com.palm.task.util.UrlUtil;

/**
 * 协议HttpTask
 * 
 * @author weixiang.qin
 * 
 */
@SuppressLint("HandlerLeak")
public class HttpTask extends BaseTask {
	protected Context ctx;
	protected String url = Const.HOST;
	protected BaseReq request;
	protected HttpHandler httpHandler = new HttpHandler();
	protected volatile boolean interrupted = false;

	public final HttpTask execute(Context ctx, BaseReq request) {
		this.ctx = ctx;
		this.request = request;
		ThreadPoolUtil.execute(this);
		return this;
	}

	@Override
	public void run() {
		if (interrupted) {
			return;
		}
		httpHandler.sendEmptyMessage(ON_PREEXECUTE);
		try {
			HashMap<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put(TRANSCODE, request.transcode);
			paramsMap.put("sessionId", "");
			String req = JsonUtil.formatObject(request);
			paramsMap.put(CONTENT, req);
			LogUtil.info("request is " + req);
			String result = HttpClientUtil.getInstance().sendPost(url,
					paramsMap);
			LogUtil.info("response is " + result);
			String json = parseResp(result);
			Class<?> cls = ClassUtil.forName(PROTOCOL_PACKAGE + ".P"
					+ request.transcode + "Resp");
			Object resp = null;
			if (JsonUtil.isJsonArray(json)) {
				resp = JsonUtil.convertToArray(json, cls);
			} else {
				resp = JsonUtil.convertToObject(json, cls);
			}
			sendSuccess(httpHandler, resp);
		} catch (Exception e) {
			int result = ON_ERROR;
			if (e instanceof ConnectTimeoutException) {
				result = ON_CONNECT_TIMEOUT;
			}
			if (e instanceof SocketTimeoutException) {
				result = ON_SO_TIMEOUT;
			}
			LogUtil.printEx(e);
			httpHandler.sendEmptyMessage(result);
		}
		httpHandler.sendEmptyMessage(ON_POSTEXECUTE);
	}

	/**
	 * 处理返回
	 * 
	 * @param result
	 */
	private String parseResp(String result) {
		return UrlUtil.getUrlParams(result).get(CONTENT);
	}

	protected void onPreExecute() {

	}

	protected void onProgress(Integer value) {

	}

	protected void onSuccess(BaseResp response) {

	}

	protected void onError(int errorId) {

	}

	protected void onPostExecute() {

	}

	protected void interrupted() {
		interrupted = true;
		if (this != null) {
			this.interrupted();
		}
	}
	
	public class HttpHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (interrupted) {
				return;
			}
			switch (msg.what) {
			case ON_PREEXECUTE:
				onPreExecute();
				break;
			case ON_PROGRESSUPDATE:
				onProgress(msg.arg1);
				break;
			case ON_SUCCESS:
				onSuccess((BaseResp) msg.obj);
				break;
			case ON_CONNECT_TIMEOUT:
				onError(msg.arg1);
				break;
			case ON_SO_TIMEOUT:
				onError(msg.arg1);
				break;
			case ON_ERROR:
				onError(msg.arg1);
				break;
			case ON_POSTEXECUTE:
				onPostExecute();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

}
