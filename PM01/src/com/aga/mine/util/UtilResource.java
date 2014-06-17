package com.aga.mine.util;


import com.aga.mine.pumpkin.MainApplication;

import android.os.Environment;


public class UtilResource {
	private static final String OBB = "/Android/obb/";
	public static final String RESOURCE_PATH = Environment.getExternalStorageDirectory() + OBB + MainApplication.getInstance().getPackageName() + "/com.aga.mine.pumpkin.resources/";
}
