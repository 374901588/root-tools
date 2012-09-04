package com.anjuke.android.dailybuild;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.anjuke.android.dailybuild.api.MobileAPI;
import com.anjuke.android.dailybuild.api.UpdateInfo;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity implements OnItemClickListener,
		OnClickListener {

	ListView lvDB;
	TextView tvLoading;
	List<String> list = null;
	ArrayAdapter<String> adapter = null;
	ImageView imgNoRecord;

	UpdateInfo updateInfo = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		lvDB = (ListView) findViewById(R.id.lvDB);
		tvLoading = (TextView) findViewById(R.id.tvLoading);
		imgNoRecord = (ImageView) findViewById(R.id.imgNoRecord);

		lvDB.setOnItemClickListener(this);
		tvLoading.setOnClickListener(this);
		checkUpdateT();
		loadProjectListT();

	}

	private void loadProjectListT() {
		imgNoRecord.setVisibility(View.GONE);
		tvLoading.setText(R.string.loading);
		tvLoading.setEnabled(false);
		final Handler h = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 1) {
					tvLoading.setText(R.string.reload);
					tvLoading.setEnabled(true);
					if (list == null) {
						adapter = null;
						imgNoRecord.setVisibility(View.VISIBLE);
					} else {
						adapter = new ArrayAdapter<String>(MainActivity.this,
								R.layout.list_item, R.id.tvListItem, list);
						imgNoRecord.setVisibility(View.GONE);
					}
					lvDB.setAdapter(adapter);
				}
				super.handleMessage(msg);
			};

		};

		new Thread(new Runnable() {

			@Override
			public void run() {
				list = MobileAPI.getProjectList();
				h.sendEmptyMessage(1);

			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position,
			long id) {
		String folder = (String) lvDB.getItemAtPosition(position);

		Intent inBuild = new Intent(this, BuildActivity.class);
		inBuild.putExtra("folder", folder);
		startActivity(inBuild);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvLoading:
			loadProjectListT();
			break;
		}
	}

	private void checkUpdateT() {
		final Handler h = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					if (updateInfo != null) {
						if (updateInfo.result != 0) {
							selfUpdate();
						}
					}
				}
				super.handleMessage(msg);
			}

		};

		new Thread(new Runnable() {

			@Override
			public void run() {
				int verCode = MobileAPI.getAppVersionCode(MainActivity.this);
				updateInfo = MobileAPI.checkUpdate(verCode);
				h.sendEmptyMessage(1);

			}
		}).start();
	}

	private void selfUpdate() {
		new AlertDialog.Builder(this)
				.setCancelable(false)
				.setTitle(R.string.update)
				.setMessage(
						String.format(getString(R.string.update_found_info),
								updateInfo.versionName, updateInfo.size))
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MobileAPI.downloadFile(MainActivity.this,
										"download", updateInfo.file);

							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

}
