package com.sbbs.me.android.loader;

import org.eclipse.egit.github.core.Blob;

import android.content.Context;

import com.rarnu.devlib.base.BaseClassLoader;
import com.sbbs.me.android.api.SbbsMeAPI;

public class SbbsCodeViewLoader extends BaseClassLoader<Blob> {

	int repoType;
	String sha;

	public SbbsCodeViewLoader(Context context, int repoType, String sha) {
		super(context);
		this.repoType = repoType;
		this.sha = sha;
	}

	@Override
	public Blob loadInBackground() {
		String userName = "zhuangbiaowei";
		String repoName = "sbbsme";
		if (repoType == 0) {

		} else if (repoType == 1) {
			userName = "rarnu";
			repoName = "root-tools";
		}
		Blob blob = null;
		try {
			blob = SbbsMeAPI.getCodeView(userName, repoName, sha, getContext());
		} catch (Exception e) {

		}
		return blob;
	}

}
