package com.rarnu.tools.root.fragment;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rarnu.tools.root.GlobalInstance;
import com.rarnu.tools.root.R;
import com.rarnu.tools.root.adapter.CompAdapter;
import com.rarnu.tools.root.api.LogApi;
import com.rarnu.tools.root.base.BasePopupFragment;
import com.rarnu.tools.root.common.CompInfo;
import com.rarnu.tools.root.utils.ComponentUtils;

public class CompPackageInfoFragment extends BasePopupFragment implements OnItemLongClickListener {

	ImageView ivAppIcon;
	TextView tvAppName, tvAppPackage;
	ListView lvReceiver;
	
	CompAdapter adapter = null;
	List<CompInfo> lstComponentInfo = null;
	
	@Override
	protected int getBarTitle() {
		return R.string.component_list;
	}

	@Override
	protected int getBarTitleWithPath() {
		return R.string.component_list;
	}

	@Override
	protected void initComponents() {
		ivAppIcon = (ImageView) innerView.findViewById(R.id.ivAppIcon);
		tvAppName = (TextView) innerView.findViewById(R.id.tvAppName);
		tvAppPackage = (TextView) innerView.findViewById(R.id.tvAppPackage);
		lvReceiver = (ListView) innerView.findViewById(R.id.lvReceiver);
		lvReceiver.setOnItemLongClickListener(this);

	}

	@Override
	protected void initLogic() {
		fillComponentList();

	}
	
	private void fillComponentList() {
		ivAppIcon
				.setBackgroundDrawable(GlobalInstance.pm
						.getApplicationIcon(GlobalInstance.currentComp.applicationInfo));
		tvAppName
				.setText(GlobalInstance.pm
						.getApplicationLabel(GlobalInstance.currentComp.applicationInfo));
		tvAppPackage.setText(GlobalInstance.currentComp.packageName);

		tvAppName
				.setTextColor(GlobalInstance.currentComp.applicationInfo.sourceDir
						.contains("/system/app/") ? Color.RED : Color.WHITE);

		Object /* PackageParser.Package */pkg = ComponentUtils
				.parsePackageInfo(GlobalInstance.currentComp);
		if (pkg == null) {
			Toast.makeText(getActivity(), R.string.no_package_info_found,
					Toast.LENGTH_LONG).show();
			getActivity().finish();
			return;
		}
		// lvReceiver
		lstComponentInfo = ComponentUtils.getPackageRSList(pkg);
		adapter = new CompAdapter(getActivity().getLayoutInflater(), lstComponentInfo);
		lvReceiver.setAdapter(adapter);

	}

	@Override
	protected int getFragmentLayoutResId() {
		return R.layout.layout_comp_packageinfo;
	}

	@Override
	protected void initMenu(Menu menu) {

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		CompInfo item = (CompInfo) lvReceiver.getItemAtPosition(position);
		boolean bRet = false;
		if (item.enabled) {
			// item.component.getComponentName()
			LogApi.logDisableComponent(ComponentUtils.getPackageComponentName(
					item.component).toString());
			bRet = ComponentUtils.doDisableComponent(ComponentUtils
					.getPackageComponentName(item.component));
			if (bRet) {
				item.enabled = false;
				((TextView) view.findViewById(R.id.itemReceiverStatus))
						.setText(R.string.comp_disabled);
				((TextView) view.findViewById(R.id.itemReceiverStatus))
						.setTextColor(Color.RED);
			} else {
				Toast.makeText(getActivity(), R.string.operation_failed,
						Toast.LENGTH_LONG).show();
			}
		} else if (!item.enabled) {
			LogApi.logEnableComponent(ComponentUtils.getPackageComponentName(
					item.component).toString());
			bRet = ComponentUtils.doEnabledComponent(ComponentUtils
					.getPackageComponentName(item.component));
			if (bRet) {
				item.enabled = true;
				((TextView) view.findViewById(R.id.itemReceiverStatus))
						.setText(R.string.comp_enabled);
				((TextView) view.findViewById(R.id.itemReceiverStatus))
						.setTextColor(0xFF008000);
			} else {
				Toast.makeText(getActivity(), R.string.operation_failed,
						Toast.LENGTH_LONG).show();
			}
		}
		getActivity().setResult(Activity.RESULT_OK);
		return false;
	}

}
