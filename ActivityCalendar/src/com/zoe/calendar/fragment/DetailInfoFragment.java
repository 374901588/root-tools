package com.zoe.calendar.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.rarnu.devlib.base.BaseFragment;
import com.zoe.calendar.R;
import com.zoe.calendar.classes.ActivityItem;

public class DetailInfoFragment extends BaseFragment {

	TextView tvTitle;
	TextView tvLocation;
	TextView tvContent;
	ActivityItem actItem;

	public DetailInfoFragment(String tag, String title) {
		super(tag, title);
	}

	@Override
	public int getBarTitle() {
		return R.string.detail_name;
	}

	@Override
	public int getBarTitleWithPath() {
		return R.string.detail_name;
	}

	@Override
	public String getCustomTitle() {
		return null;
	}

	@Override
	public void initComponents() {
		tvTitle = (TextView) innerView.findViewById(R.id.tvTitle);
		tvLocation = (TextView) innerView.findViewById(R.id.tvLocation);
		tvContent = (TextView) innerView.findViewById(R.id.tvContent);
	}

	@Override
	public void initEvents() {

	}

	@Override
	public void initLogic() {
		actItem = (ActivityItem) getActivity().getIntent().getSerializableExtra(
				"item");
		tvTitle.setText(actItem.title);
		tvLocation.setText(actItem.location);
		tvContent.setText(actItem.content);
	}

	@Override
	public int getFragmentLayoutResId() {
		return R.layout.fragment_detail_info;
	}

	@Override
	public String getMainActivityName() {
		return "";
	}

	@Override
	public void initMenu(Menu menu) {

	}

	@Override
	public void onGetNewArguments(Bundle bn) {

	}

	@Override
	public Bundle getFragmentState() {
		return null;
	}

}
