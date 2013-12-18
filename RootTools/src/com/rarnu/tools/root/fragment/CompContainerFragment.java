package com.rarnu.tools.root.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import com.rarnu.devlib.base.BaseTabFragment;
import com.rarnu.tools.root.MainActivity;
import com.rarnu.tools.root.R;

import java.util.List;

public class CompContainerFragment extends BaseTabFragment {

    @Override
    public void initFragmentList(List<Fragment> listFragment) {
        listFragment.add(new AutoBootFragment());
        listFragment.add(new CompFragment());
    }

    @Override
    public int getBarTitle() {
        return R.string.func4_title;
    }

    @Override
    public int getBarTitleWithPath() {
        return R.string.func4_title_with_path;
    }

    @Override
    public String getCustomTitle() {
        return null;
    }

    @Override
    public String getMainActivityName() {
        return MainActivity.class.getName();
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
