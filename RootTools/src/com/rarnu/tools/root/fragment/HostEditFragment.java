package com.rarnu.tools.root.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.rarnu.tools.root.R;
import com.rarnu.tools.root.api.LogApi;
import com.rarnu.tools.root.base.BasePopupFragment;
import com.rarnu.tools.root.common.MenuItemIds;
import com.rarnu.tools.root.utils.DirHelper;
import com.rarnu.tools.root.utils.FileUtils;
import com.rarnu.tools.root.utils.HostsUtils;

public class HostEditFragment extends BasePopupFragment {

	EditText etEditHosts;

	private static final String PATH_HOSTS = "/system/etc/hosts";
	private static final String LOCAL_HOSTS = DirHelper.HOSTS_DIR + "hosts";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogApi.logEnterManualEditHosts();
	}

	@Override
	protected int getBarTitle() {
		return R.string.manual_edit_hosts;
	}

	@Override
	protected int getBarTitleWithPath() {
		return R.string.manual_edit_hosts;
	}

	@Override
	protected void initComponents() {
		etEditHosts = (EditText) innerView.findViewById(R.id.etEditHosts);
	}

	@Override
	protected void initLogic() {
		loadHosts();

	}

	@Override
	protected int getFragmentLayoutResId() {
		return R.layout.layout_host_edit;
	}

	@Override
	protected void initMenu(Menu menu) {
		MenuItem itemSave = menu.add(0, MenuItemIds.MENU_SAVE, 99,
				R.string.save);
		itemSave.setIcon(android.R.drawable.ic_menu_save);
		itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MenuItemIds.MENU_SAVE:
			if (!saveHosts()) {
				Toast.makeText(getActivity(), R.string.save_hosts_error,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getActivity(), R.string.save_hosts_succ,
						Toast.LENGTH_LONG).show();
				getActivity().finish();
			}
			break;
		}
		return true;
	}

	private void loadHosts() {
		List<String> hosts = null;
		try {
			hosts = FileUtils.readFile(PATH_HOSTS);
			String hostsStr = "";
			if (hosts != null && hosts.size() != 0) {
				for (String s : hosts) {
					hostsStr += s + "\n";
				}
			}
			etEditHosts.setText(hostsStr);
		} catch (Exception e) {
			etEditHosts.setText("");
		}
	}

	private boolean saveHosts() {
		LogApi.logManualEditHosts();
		String hosts = etEditHosts.getText().toString();
		return HostsUtils.copyHosts(hosts, LOCAL_HOSTS);

	}

}
