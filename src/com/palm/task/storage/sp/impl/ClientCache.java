package com.palm.task.storage.sp.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.palm.task.storage.sp.BaseSpStorage;

/**
 * 
 * @author weixiang.qin
 * 
 */
public class ClientCache extends BaseSpStorage {
	public boolean isFirstInstall;

	public ClientCache(Context context){
		super(context);
	}
	
	@Override
	public void ser(SharedPreferences sp) {
		Editor editor = sp.edit();
		editor.putBoolean("isFirstInstall", isFirstInstall);
		editor.commit();
	}

	@Override
	public void unSer(SharedPreferences sp) {
		isFirstInstall = sp.getBoolean("isFirstInstall", true);
	}

	@Override
	public void del(SharedPreferences sp) {
		sp.edit().remove("isFirstInstall").commit();
	}

	@Override
	public String getIdentifer() {
		return ClientCache.class.getName();
	}

}
