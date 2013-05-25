package com.zoe.calendar;

import android.app.Fragment;

import com.rarnu.devlib.base.BaseActivity;
import com.zoe.calendar.fragment.SettingsFragment;

public class SettingsActivity extends BaseActivity {

	@Override
	public int getIcon() {
		return R.drawable.ic_launcher;
	}

	@Override
	public Fragment replaceFragment() {
		return new SettingsFragment(getString(R.tag.fragment_settings));
	}

}
