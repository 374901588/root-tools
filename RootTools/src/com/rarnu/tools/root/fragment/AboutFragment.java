package com.rarnu.tools.root.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.rarnu.tools.root.EggActivity;
import com.rarnu.tools.root.GlobalInstance;
import com.rarnu.tools.root.R;
import com.rarnu.tools.root.adapter.AboutAdapter;
import com.rarnu.tools.root.adapter.PartnerAdapter;
import com.rarnu.tools.root.api.LogApi;
import com.rarnu.tools.root.base.BaseFragment;
import com.rarnu.tools.root.common.AboutInfo;
import com.rarnu.tools.root.comp.BlockListView;
import com.rarnu.tools.root.fragmentactivity.HelpActivity;
import com.rarnu.tools.root.utils.DeviceUtils;
import com.rarnu.tools.root.utils.UIUtils;
import com.rarnu.tools.root.utils.UpdateUtils;

public class AboutFragment extends BaseFragment implements OnItemClickListener {

	TextView tvAppVersion, tvDebug;
	BlockListView lstAbout, lstEoe;

	AboutAdapter adapter = null;
	List<AboutInfo> list = null;

	PartnerAdapter adapterEoe = null;
	List<Object> listEoe = null;

	int fitable = 5;
	int fitableClick = 0;

	private void showDebugStatus() {
		tvDebug.setVisibility(GlobalInstance.DEBUG ? View.VISIBLE : View.GONE);
	}

	private void showAppVersion() {
		tvAppVersion.setText(DeviceUtils.getAppVersionName(getActivity()));
	}

	private int getSystemFitable() {

		fitable = DeviceUtils.getFitable(GlobalInstance.metric);
		if (fitable < 1) {
			fitable = 1;
		}
		if (fitable > 9) {
			fitable = 9;
		}
		return fitable;
	}

	@Override
	protected int getBarTitle() {
		return R.string.about;
	}

	@Override
	protected int getBarTitleWithPath() {
		return R.string.about_with_path;
	}

	@Override
	protected void initComponents() {

		tvAppVersion = (TextView) innerView.findViewById(R.id.tvAppVersion);
		tvDebug = (TextView) innerView.findViewById(R.id.tvDebug);
		lstAbout = (BlockListView) innerView.findViewById(R.id.lstAbout);
		lstEoe = (BlockListView) innerView.findViewById(R.id.lstEoe);
		lstAbout.setItemHeight(UIUtils.dipToPx(56));
		lstEoe.setItemHeight(UIUtils.dipToPx(64));
		lstAbout.setOnItemClickListener(this);
		lstEoe.setOnItemClickListener(this);

		list = new ArrayList<AboutInfo>();
		adapter = new AboutAdapter(getActivity(), list);
		lstAbout.setAdapter(adapter);

		listEoe = new ArrayList<Object>();
		listEoe.add(new Object());
		adapterEoe = new PartnerAdapter(getActivity(), listEoe);
		lstEoe.setAdapter(adapterEoe);
	}

	@Override
	protected int getFragmentLayoutResId() {
		return R.layout.layout_about;
	}

	@Override
	protected void initMenu(Menu menu) {

	}

	@Override
	protected void initLogic() {
		showAppVersion();
		showDebugStatus();
		LogApi.logEnterAbout();
		fitableClick = 0;

		list.clear();
		AboutInfo infoUpdate = new AboutInfo();
		infoUpdate.title = getString(R.string.check_update);
		infoUpdate.fitable = -1;
		list.add(infoUpdate);

		AboutInfo infoHelp = new AboutInfo();
		infoHelp.title = getString(R.string.how_to_use);
		infoHelp.fitable = -1;
		list.add(infoHelp);

		AboutInfo infoFitable = new AboutInfo();
		infoFitable.title = getString(R.string.system_fitable);
		infoFitable.fitable = getSystemFitable();
		list.add(infoFitable);

		adapter.setNewList(list);
		lstAbout.resize();
		lstEoe.resize();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent.getAdapter() instanceof PartnerAdapter) {
			Intent inEoe = new Intent(Intent.ACTION_VIEW);
			inEoe.setData(Uri.parse("http://eoemarket.com/"));
			startActivity(inEoe);
		} else {
			switch (position) {
			case 0:
				UpdateUtils.showUpdateInfo(getActivity());
				break;
			case 1:
				GlobalFragment.showContent(getActivity(), new Intent(
						getActivity(), HelpActivity.class),
						GlobalFragment.fIntro);
				break;
			case 2:
				if (!GlobalInstance.DEBUG) {
					fitableClick++;
					if (fitableClick == 10) {
						fitableClick = 0;
						Intent inEgg = new Intent(getActivity(),
								EggActivity.class);
						startActivity(inEgg);
					}
				}
				break;
			}
		}
	}
}
