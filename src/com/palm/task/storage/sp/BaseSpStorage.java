package com.palm.task.storage.sp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author weixiang.qin
 * 
 */
public abstract class BaseSpStorage implements ISpStorage {
	protected Context mContext;

	public BaseSpStorage(Context context){
		this.mContext = context;
		load();
	}
	
	/**
	 * 加载
	 */
	public void load() {
		if (this != null && this.getIdentifer() != null) {
			SharedPreferences sp = mContext.getSharedPreferences(
					this.getIdentifer(), Context.MODE_PRIVATE);
			this.unSer(sp);
		}
	}

	/**
	 * 保存
	 */
	public void save() {
		if (this != null && this.getIdentifer() != null) {
			SharedPreferences sp = mContext.getSharedPreferences(
					this.getIdentifer(), Context.MODE_PRIVATE);
			this.ser(sp);
		}
	}

	/**
	 * 删除
	 */
	public void del() {
		if (this != null && this.getIdentifer() != null) {
			SharedPreferences sp = mContext.getSharedPreferences(
					this.getIdentifer(), Context.MODE_PRIVATE);
			this.del(sp);
		}
	}
}
