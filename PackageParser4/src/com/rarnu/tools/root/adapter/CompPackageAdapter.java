package com.rarnu.tools.root.adapter;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rarnu.root.pp4.R;
import com.rarnu.tools.root.GlobalInstance;
import com.rarnu.tools.root.holder.CompPackageAdapterHolder;
import com.rarnu.tools.root.utils.ColorUtils;

public class CompPackageAdapter extends InnerAdapter<PackageInfo> {

	public CompPackageAdapter(Context context, List<PackageInfo> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		PackageInfo item = list.get(position);

		View v;
		if (convertView == null) {
			v = inflater.inflate(R.layout.comp_package_item, parent, false);
		} else {
			v = convertView;
		}
		CompPackageAdapterHolder holder = (CompPackageAdapterHolder) v.getTag();
		if (holder == null) {
			holder = new CompPackageAdapterHolder();
			holder.itemIcon = (ImageView) v.findViewById(R.id.itemIcon);
			holder.itemName = (TextView) v.findViewById(R.id.itemName);
			holder.tvReceiverCountValue = (TextView) v
					.findViewById(R.id.tvReceiverCountValue);
			v.setTag(holder);
		}

		if (item != null) {

			holder.itemIcon.setBackgroundDrawable(GlobalInstance.pm
					.getApplicationIcon(item.applicationInfo));
			holder.itemName.setText(GlobalInstance.pm
					.getApplicationLabel(item.applicationInfo));

			holder.itemName.setTextColor(ColorUtils.getSystemAttrColor(context,
					android.R.attr.textColorPrimary));
			if (item.applicationInfo.sourceDir.contains("/system/app/")) {
				holder.itemName.setTextColor(Color.RED);
			}

			holder.tvReceiverCountValue.setText(item.applicationInfo.sourceDir);

		}

		return v;
	}

	@Override
	public String getValueText(PackageInfo item) {
		return GlobalInstance.pm.getApplicationLabel(item.applicationInfo)
				+ item.packageName;
	}
}
