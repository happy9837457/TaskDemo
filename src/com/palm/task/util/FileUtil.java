package com.palm.task.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.os.Environment;

/**
 * 文件工具类
 * 
 * @author weixiang.qin
 * 
 */
public class FileUtil {
	protected static final String PATH = "palm" + File.separator + "http";

	/**
	 * 外置存储卡是否可写
	 * 
	 * @return
	 */
	public static boolean isExternalAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 外置存储卡路径(/mnt/sdcard/)
	 * 
	 * @return
	 */
	public static String getExternalDirectory() {
		return Environment.getExternalStorageDirectory().getAbsoluteFile()
				+ File.separator;
	}

	/**
	 * Pictures路径
	 * 
	 * @return
	 */
	public static String getExternalStoragePublicDirectory() {
		return Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES).getAbsolutePath()
				+ File.separator;
	}

	/**
	 * 创建目录
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean createDir(String dir) {
		File file = new File(dir);
		return file.mkdirs();
	}

	/**
	 * stream转成file
	 * 
	 * @param ins
	 * @param file
	 * @throws Exception
	 */
	public static void streamToFile(InputStream ins, File file)
			throws Exception {
		FileOutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
	}

}
