package com.rarnu.tools.root.fragmentactivity;

import android.app.Fragment;

import com.rarnu.tools.root.base.BasePopupActivity;
import com.rarnu.tools.root.fragment.GlobalFragment;

public class HostAddActivity extends BasePopupActivity {

	@Override
	public Fragment replaceFragment() {
		return GlobalFragment.fHostAdd;
	}

}
