package com.rarnu.tools.root;

import java.io.File;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ShareActionProvider;

import com.rarnu.tools.root.api.LogApi;
import com.rarnu.tools.root.api.MobileApi;
import com.rarnu.tools.root.base.MenuItemIds;
import com.rarnu.tools.root.common.RTConfig;
import com.rarnu.tools.root.fragment.GlobalFragment;
import com.rarnu.tools.root.utils.DeviceUtils;
import com.rarnu.tools.root.utils.DirHelper;
import com.rarnu.tools.root.utils.ImageUtils;
import com.rarnu.tools.root.utils.MemorySpecialList;
import com.rarnu.tools.root.utils.NetworkUtils;
import com.rarnu.tools.root.utils.PingUtils;
import com.rarnu.tools.root.utils.root.RootUtils;

public class MainActivity extends Activity {

	private static boolean oneTimeRun = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		if (!oneTimeRun) {
			oneTimeRun = true;
			initOneTime();
		}
		loadUI();
	}

	@Override
	protected void onDestroy() {
		LogApi.logAppStop();
		super.onDestroy();
	}
	
	private void initOneTime() {
		RootUtils.mountRW();
		loadExcludeListT();
		initConfig();
		if (GlobalInstance.isFirstStart) {
			LogApi.logAppFirstStart();
			GlobalInstance.isFirstStart = false;
			RTConfig.setFirstStart(this, GlobalInstance.isFirstStart);
		}

		loadNetworkStatus();
		getUpdateInfo();
	}

	private void loadUI() {
		setContentView(R.layout.layout_main);
		replaceMainFragment();
		View vDetail = findViewById(R.id.fragmentDetail);
		GlobalInstance.dualPane = vDetail != null
				&& vDetail.getVisibility() == View.VISIBLE;
		getActionBar().setTitle(R.string.app_name);

	}

	private void setDualPane() {
		if (GlobalInstance.dualPane) {
			switch (GlobalInstance.currentFragment) {
			case 1:
				replaceDetailFragment(GlobalFragment.fSysapp);
				break;
			case 4:
				replaceDetailFragment(GlobalFragment.fBusybox);
				break;
			case 10:
				replaceDetailFragment(GlobalFragment.fFeedback);
				break;
			case 12:
				replaceDetailFragment(GlobalFragment.fAbout);
				break;
			default:
				replaceDetailFragment(GlobalFragment.fIntro);
				break;
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setDualPane();
	}

	private void replaceMainFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.fragmentMain, GlobalFragment.fMain);
		fragmentTransaction
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}

	private void replaceDetailFragment(Fragment f) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		fragmentTransaction.replace(R.id.fragmentDetail, f);
		fragmentTransaction
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		MenuItem actionItem = menu.add(0, MenuItemIds.MENU_ID_SHARE, 0,
				R.string.short_share);
		actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		actionItem.setIcon(android.R.drawable.ic_menu_share);
		ShareActionProvider actionProvider = new ShareActionProvider(this);
		actionItem.setActionProvider(actionProvider);
		actionProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		actionProvider.setShareIntent(createShareIntent());

		return true;
	}

	private Intent createShareIntent() {

		String bmpName = DirHelper.ROOT_DIR + "icon.png";
		File fIcon = new File(bmpName);
		if (!fIcon.exists()) {
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon);
			ImageUtils.saveBitmapToFile(bmp, bmpName);
		}

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/*");
		Uri uri = Uri.fromFile(fIcon);
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		return shareIntent;
	}

	private void initConfig() {
		GlobalInstance.isFirstStart = RTConfig.getFirstStart(this);
		GlobalInstance.allowDeleteLevel0 = RTConfig.getAllowDeleteLevel0(this);
		GlobalInstance.alsoDeleteData = RTConfig.getAlsoDeleteData(this);
		GlobalInstance.backupBeforeDelete = RTConfig
				.getBackupBeforeDelete(this);
		GlobalInstance.overrideBackuped = RTConfig.getOverrideBackuped(this);
		GlobalInstance.reinstallApk = RTConfig.getReinstallApk(this);
		GlobalInstance.killProcessBeforeClean = RTConfig
				.getKillProcessBeforeClean(this);
		GlobalInstance.nameServer = RTConfig.getNameServer(this);
	}

	private void loadExcludeListT() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MemorySpecialList.loadExcludeList();
			}
		}).start();
	}

	private void loadNetworkStatus() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				GlobalInstance.loadingNetwork = true;
				GlobalInstance.networkInfo = NetworkUtils
						.getNetworkInfo(MainActivity.this);
				GlobalInstance.networkSpeed = PingUtils
						.testNetworkSpeed(MainActivity.this);
				GlobalInstance.loadingNetwork = false;
			}
		}).start();
	}

	private void getUpdateInfo() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				int verCode = DeviceUtils.getAppVersionCode(MainActivity.this);
				GlobalInstance.updateInfo = MobileApi.checkUpdate(verCode);

			}
		}).start();
	}

}
