package com.palm.task.task;

import android.os.Handler;
import android.os.Message;

import com.palm.task.util.Const;

/**
 * 
 * @author weixiang.qin
 * 
 */
public abstract class BaseTask implements Runnable, Const {
	protected static final int ON_PREEXECUTE = 0;
	protected static final int ON_PROGRESSUPDATE = 1;
	protected static final int ON_SUCCESS = 2;
	protected static final int ON_ERROR = 3;
	protected static final int ON_POSTEXECUTE = 4;
	protected static final int ON_CONNECT_TIMEOUT = 11;// 连接超时
	protected static final int ON_SO_TIMEOUT = 12;// 读取超时
	
	/**
	 * 成功返回
	 * 
	 * @param obj
	 */
	protected void sendSuccess(Handler handler, Object obj) {
		Message msg = new Message();
		msg.what = ON_SUCCESS;
		msg.obj = obj;
		handler.sendMessage(msg);
	}
}
