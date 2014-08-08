package com.palm.task.util;

/**
 * 自定义异常
 * 
 * @author weixiang.qin
 * 
 */
public class HttpStatusException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5298166535390223998L;

	public HttpStatusException(String msg) {
		super(msg);
	}

}
