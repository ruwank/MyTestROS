package com.relianceit.relianceorder.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class AppUtils {

	public static String getDataDirectory(Context context) {
		File sdRoot = Environment.getExternalStorageDirectory();
		String path = sdRoot.getPath();
		String packageName = context.getPackageName();
		path = path + File.separator + "Android" + File.separator + "data"
				+ File.separator + packageName + File.separator + "files"
				+ File.separator;
		return path;
	}


}

//test pull
