package com.relianceit.relianceorder.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;

import java.io.File;

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

    public static void broadcastAction(Context context, String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);
    }

    public static void showAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
