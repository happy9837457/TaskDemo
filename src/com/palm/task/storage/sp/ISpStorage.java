package com.palm.task.storage.sp;

import android.content.SharedPreferences;

/**
 * 首选项存储接口
 * 
 * @author weixiang.qin
 * 
 */
public interface ISpStorage {
	/**
	 * 
	 * @param sp
	 */
	public void ser(SharedPreferences sp);

	/**
	 * 
	 * @param sp
	 */
	public void unSer(SharedPreferences sp);

	/**
	 * 
	 * @param sp
	 */
	public void del(SharedPreferences sp);

	/**
	 * 
	 * @param sp
	 */
	public String getIdentifer();
}
