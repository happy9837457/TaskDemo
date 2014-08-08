package com.palm.task.util;

public interface Const {
	/******************** 连接地址 ********************/
	/**
	 * 是否调试模式(调试模式开启日志)
	 */
	public static final boolean IS_DEBUG = true;
	/**
	 * 协议地址
	 * 
	 */
	public static final String HOST = "http://110.249.176.140:7373/o2o_phone_interface/servlet/main.cl";

	/******************** 协议参数名 ********************/
	/**
	 * 接口代码
	 */
	public final static String TRANSCODE = "transCode";
	/**
	 * json
	 */
	public final static String JSON = "json";
	/**
	 * content
	 */
	public final static String CONTENT = "content";
	
	/******************** bean包名 ********************/
	/**
	 * 
	 */
	public static final String PROTOCOL_PACKAGE = "com.palm.task.bean.resp";
}
