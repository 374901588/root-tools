package com.rarnu.tools.root.loader;

import java.util.List;

import android.content.Context;

import com.rarnu.tools.root.base.BaseLoader;
import com.rarnu.tools.root.common.DataappInfo;
import com.rarnu.tools.root.utils.ApkUtils;

public class BackupLoader extends BaseLoader<DataappInfo> {

	public BackupLoader(Context context) {
		super(context);
	}

	@Override
	public List<DataappInfo> loadInBackground() {
		return ApkUtils.getInstalledApps(getContext(), false);
	}
}
