package com.rarnu.zoe.loving;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rarnu.zoe.loving.base.BaseActivity;
import com.rarnu.zoe.loving.comp.ScrollLayout;
import com.rarnu.zoe.loving.comp.ScrollLayout.OnScreenChangeListener;
import com.rarnu.zoe.loving.page.PageHistory;
import com.rarnu.zoe.loving.page.PageImage;
import com.rarnu.zoe.loving.page.PageLetter;
import com.rarnu.zoe.loving.page.PageSettings;
import com.rarnu.zoe.loving.page.PageToday;
import com.rarnu.zoe.loving.utils.UIUtils;

public class MainActivity extends BaseActivity implements OnClickListener,
		OnScreenChangeListener {

	// [region] field define
	RelativeLayout btnFunc1, btnFunc2, btnFunc3, btnFunc4, btnFunc5;
	PageImage pImage;
	PageLetter pLetter;
	PageToday pToday;
	PageHistory pHistory;
	PageSettings pSettings;
	ScrollLayout layMain;

	// [/region]

	// [region] life
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		UIUtils.initDisplayMetrics(getWindowManager());
		super.onCreate(savedInstanceState);

		onClick(btnFunc3);
		layMain.setToScreen(2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			if (requestCode == 0) {
				finish();
			}
			return;
		}

		switch (requestCode) {
		case 0:
			if (Config.getAccount(this).equals("")) {
				finish();
			}
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	// [/region]

	// [region] override
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void init() {
		super.init();

		adjustButtonWidth();
		
	}

	@Override
	protected void initComponents() {
		super.initComponents();
		layMain = (ScrollLayout) findViewById(R.id.layMain);

		pImage = (PageImage) findViewById(R.id.pageImage);
		pLetter = (PageLetter) findViewById(R.id.pageLetter);
		pToday = (PageToday) findViewById(R.id.pageToday);
		pHistory = (PageHistory) findViewById(R.id.pageHistory);
		pSettings = (PageSettings) findViewById(R.id.pageSettings);

		pImage.setRootInflater(getLayoutInflater());
		pLetter.setRootInflater(getLayoutInflater());
		pToday.setRootInflater(getLayoutInflater());
		pHistory.setRootInflater(getLayoutInflater());
		pSettings.setRootInflater(getLayoutInflater());

		btnFunc1 = (RelativeLayout) findViewById(R.id.btnFunc1);
		btnFunc2 = (RelativeLayout) findViewById(R.id.btnFunc2);
		btnFunc3 = (RelativeLayout) findViewById(R.id.btnFunc3);
		btnFunc4 = (RelativeLayout) findViewById(R.id.btnFunc4);
		btnFunc5 = (RelativeLayout) findViewById(R.id.btnFunc5);

		btnFunc1.setTag(1);
		btnFunc2.setTag(2);
		btnFunc3.setTag(3);
		btnFunc4.setTag(4);
		btnFunc5.setTag(5);

		setIconText(btnFunc1, R.string.tab_image);
		setIconText(btnFunc2, R.string.tab_letter);
		setIconText(btnFunc3, R.string.tab_today);
		setIconText(btnFunc4, R.string.tab_history);
		setIconText(btnFunc5, R.string.tab_settings);

	}

	@Override
	protected void initEvents() {
		super.initEvents();
		layMain.setOnScreenChangeListener(this);
	}

	// [/region]

	// [region] UI operating
	private void adjustButtonWidth() {

		// 64dip = (x)px
		int wid = (UIUtils.getWidth() - UIUtils.dipToPx(32)) / 5;
		setButtonWidth(btnFunc1, wid);
		setButtonWidth(btnFunc2, wid);
		setButtonWidth(btnFunc3, wid);
		setButtonWidth(btnFunc4, wid);
		setButtonWidth(btnFunc5, wid);

	}

	private void setButtonWidth(RelativeLayout btn, int width) {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) btn
				.getLayoutParams();
		lp.width = width;
		btn.setLayoutParams(lp);
	}

	private void setIconText(RelativeLayout btn, int text) {

		((TextView) btn.findViewById(R.id.tvItemName)).setText(text);
		btn.setOnClickListener(this);
	}

	private void setSelectedItem(RelativeLayout btn) {
		((TextView) btnFunc1.findViewById(R.id.tvItemName)).setTextSize(16);
		((TextView) btnFunc2.findViewById(R.id.tvItemName)).setTextSize(16);
		((TextView) btnFunc3.findViewById(R.id.tvItemName)).setTextSize(16);
		((TextView) btnFunc4.findViewById(R.id.tvItemName)).setTextSize(16);
		((TextView) btnFunc5.findViewById(R.id.tvItemName)).setTextSize(16);

		((TextView) btnFunc1.findViewById(R.id.tvItemName))
				.setTextColor(0xff999999);
		((TextView) btnFunc2.findViewById(R.id.tvItemName))
				.setTextColor(0xff999999);
		((TextView) btnFunc3.findViewById(R.id.tvItemName))
				.setTextColor(0xff999999);
		((TextView) btnFunc4.findViewById(R.id.tvItemName))
				.setTextColor(0xff999999);
		((TextView) btnFunc5.findViewById(R.id.tvItemName))
				.setTextColor(0xff999999);

		((TextView) btn.findViewById(R.id.tvItemName)).setTextSize(22);
		((TextView) btn.findViewById(R.id.tvItemName))
				.setTextColor(Color.WHITE);

	}

	// [/region]

	// [region] events
	@Override
	public void onClick(View v) {

		if (v instanceof RelativeLayout) {
			setSelectedItem((RelativeLayout) v);
			switch (v.getId()) {

			case R.id.btnFunc1:
				layMain.snapToScreen(0);
				pImage.load();
				break;
			case R.id.btnFunc2:
				layMain.snapToScreen(1);
				pLetter.load();
				break;
			case R.id.btnFunc3:
				Log.e("snapToScreen", "snapToScreen 2");
				layMain.snapToScreen(2);
				pToday.load();
				break;
			case R.id.btnFunc4:
				layMain.snapToScreen(3);
				pHistory.load();
				break;
			case R.id.btnFunc5:
				layMain.snapToScreen(4);
				pSettings.load();
				break;
			}
		}
	}

	@Override
	public void onScreenChange(int screen) {
		switch (screen) {
		case 0:
			setSelectedItem(btnFunc1);

			pImage.load();
			break;
		case 1:
			setSelectedItem(btnFunc2);

			pLetter.load();
			break;
		case 2:
			setSelectedItem(btnFunc3);

			pToday.load();
			break;
		case 3:
			setSelectedItem(btnFunc4);
			pHistory.load();
			break;
		case 4:
			setSelectedItem(btnFunc5);
			pSettings.load();
			break;
		}
	}

	// [/region]

}
