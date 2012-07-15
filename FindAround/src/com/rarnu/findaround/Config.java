package com.rarnu.findaround;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Config {

	public static List<String> getKeywordsList(Context context) {
		
		if (getFirstLoad(context)) {
			setKeywords(context, context.getString(R.string.init_keywords));
			setFirstLoad(context, false);
		}
		
		String path = "/data/data/" + context.getPackageName() + "/keywords";
		try {
			return FileUtils.readFile(new File(path));
		} catch (IOException e) {
			return null;
		}
	}

	public static String getKeywordsText(Context context) {
		List<String> list = getKeywordsList(context);
		String ret = "";
		if (list != null && list.size() != 0) {
			for (String s : list) {
				ret += s + "\n";
			}
		}
		return ret;
	}

	public static void setKeywords(Context context, String text) {
		String path = "/data/data/" + context.getPackageName() + "/keywords";

		try {
			FileUtils.rewriteFile(new File(path), text);
		} catch (IOException e) {
		}
	}

	public static int getDist(Context context) {
		return getSharedPreferences(context).getInt(KEY_DIST, 1000);
	}

	public static int getMethod(Context context) {
		return getSharedPreferences(context).getInt(KEY_METHOD, 2);
	}

	public static void setDist(Context context, int value) {
		getSharedPreferences(context).edit().putInt(KEY_DIST, value).commit();
	}

	public static void setMethod(Context context, int value) {
		getSharedPreferences(context).edit().putInt(KEY_METHOD, value).commit();
	}

	private static boolean getFirstLoad(Context context) {
		return getSharedPreferences(context).getBoolean(KEY_FIRST_LOAD, true);
	}
	private static void setFirstLoad(Context context, boolean value) {
		getSharedPreferences(context).edit().putBoolean(KEY_FIRST_LOAD, value).commit();
	}
	
	private final static String KEY_DIST = "key_dist";
	private final static String KEY_METHOD = "key_method";
	private final static String KEY_FIRST_LOAD = "key_first_load";

	private static SharedPreferences sp = null;

	private static SharedPreferences getSharedPreferences(Context context) {
		if (sp == null) {
			sp = PreferenceManager.getDefaultSharedPreferences(context);
		}
		return sp;
	}
}
