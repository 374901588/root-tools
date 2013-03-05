package com.rarnu.tools.root;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.rarnu.command.RootUtils;
import com.rarnu.devlib.utils.UIUtils;
import com.rarnu.tools.root.utils.DirHelper;

public class SplashActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		UIUtils.initDisplayMetrics(this, getWindowManager());
		RootUtils.init(this);
		GlobalInstance.init(this);

		if (!DirHelper.isSDCardExists()) {

			new AlertDialog.Builder(this)
					.setTitle(R.string.hint)
					.setMessage(R.string.no_sdcard_found)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();

								}
							}).show();

			return;
		}

		setContentView(R.layout.layout_splash);
		DirHelper.makeDir();

		final Timer tmrClose = new Timer();
		tmrClose.schedule(new TimerTask() {

			@Override
			public void run() {
				tmrClose.cancel();
				finish();
				startMainActivity();
			}
		}, 2000);

	}

	private void startMainActivity() {
		Intent inMain = new Intent(this, MainActivity.class);
		inMain.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		startActivity(inMain);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

}
