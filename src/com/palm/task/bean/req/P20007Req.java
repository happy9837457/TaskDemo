package com.palm.task.bean.req;

public class P20007Req extends BaseReq {
	public static final String TRANSCODE = "20007";
	public String appType;// 客户端类型
	public String clientType = "0";// 应用类型:0站主平台、1购彩平台互联网版、2购彩平台站主版

	public P20007Req() {
		this.transcode = TRANSCODE;
	}
}
