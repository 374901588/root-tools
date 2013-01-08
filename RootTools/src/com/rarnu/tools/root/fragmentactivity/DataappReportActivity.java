package com.rarnu.tools.root.fragmentactivity;

import android.app.Fragment;

import com.rarnu.tools.root.base.BasePopupActivity;
import com.rarnu.tools.root.fragment.GlobalFragment;

public class DataappReportActivity extends BasePopupActivity {

	@Override
	public boolean getCloseCondition() {
		return false;
	}
	
	@Override
	public Fragment replaceFragment() {
		return GlobalFragment.fDataappReport;
	}
}
