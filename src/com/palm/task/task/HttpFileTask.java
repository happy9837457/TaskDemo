package com.palm.task.task;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.palm.task.util.HttpClientUtil;
import com.palm.task.util.LogUtil;
import com.palm.task.util.ThreadPoolUtil;

/**
 * 文件下载Task
 * 
 * @author weixiang.qin
 * 
 */
@SuppressLint("HandlerLeak")
public class HttpFileTask extends BaseTask {
	protected Context ctx;
	protected String url;
	protected String fileName;
	protected HttpHandler httpHandler = new HttpHandler();

	public final HttpFileTask execute(Context ctx, String url, String fileName) {
		this.ctx = ctx;
		this.url = url;
		this.fileName = fileName;
		ThreadPoolUtil.execute(this);
		return this;
	}

	@Override
	public void run() {
		try {
			File file = new File(fileName);
			HttpClientUtil.getInstance().downloadFile(url, file);
			sendSuccess(httpHandler, file);
		} catch (Exception e) {
			LogUtil.printEx(e);
			httpHandler.sendEmptyMessage(ON_ERROR);
		}
	}

	protected void onSuccess(File file) {

	}

	protected void onError() {

	}

	public class HttpHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ON_SUCCESS:
				onSuccess((File) msg.obj);
				break;
			case ON_ERROR:
				onError();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

}
