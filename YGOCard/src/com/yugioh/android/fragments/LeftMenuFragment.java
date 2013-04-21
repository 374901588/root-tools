package com.yugioh.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rarnu.devlib.base.BaseFragment;
import com.yugioh.android.MainActivity;
import com.yugioh.android.R;

public class LeftMenuFragment extends BaseFragment implements OnItemClickListener {

	ListView lvCard, lvExit;
	ArrayAdapter<String> adapterCard, adapterExit;
	List<String> listCard, listExit;

	@Override
	protected int getBarTitle() {
		return R.string.app_name;
	}

	@Override
	protected int getBarTitleWithPath() {
		return R.string.app_name;
	}

	@Override
	protected void initComponents() {
		lvCard = (ListView) innerView.findViewById(R.id.lvCard);
		lvExit = (ListView) innerView.findViewById(R.id.lvExit);
		
		listCard = new ArrayList<String>();
		listCard.add(getString(R.string.lm_search));
		listCard.add(getString(R.string.lm_banned));
		listCard.add(getString(R.string.lm_newcard));
		listCard.add(getString(R.string.lm_tool));
		listExit = new ArrayList<String>();
		listExit.add(getString(R.string.lm_exit));
		adapterCard = new ArrayAdapter<String>(getActivity(), R.layout.item_menu, listCard);
		adapterExit = new ArrayAdapter<String>(getActivity(), R.layout.item_menu, listExit);
		lvCard.setAdapter(adapterCard);
		lvExit.setAdapter(adapterExit);
		
	}

	@Override
	protected void initEvents() {
		lvCard.setOnItemClickListener(this);
		lvExit.setOnItemClickListener(this);
	}

	@Override
	protected void initLogic() {
		
	}

	@Override
	protected int getFragmentLayoutResId() {
		return R.layout.menu_left;
	}

	@Override
	protected String getMainActivityName() {
		return MainActivity.class.getName();
	}

	@Override
	protected void initMenu(Menu menu) {

	}

	@Override
	protected void onGetNewArguments(Bundle bn) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.lvCard:
			// TODO: switch page
			break;
		case R.id.lvExit:
			getActivity().finish();
			break;
		}
		
	}

	@Override
	protected String getCustomTitle() {
		return null;
	}

}
