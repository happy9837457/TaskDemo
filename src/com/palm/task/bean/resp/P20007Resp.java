package com.palm.task.bean.resp;

public class P20007Resp extends BaseResp {
	public int versionCode;// 版本号 int类型
	public String versionName;// 版本名称 string类型
	public String downloadUrl;// 客户端更新地址
	public String forcedUpdate;// 是否强制更新 0 不强制 1 强制更新
	public String createTime;// 更新时间
	public String appSize;// 客户端大小 单位：字节
	public String updateProfile;// 更新简介 更新简介
}
