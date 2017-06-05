package com.rarnu.tools.neo.activity

import android.app.Fragment
import com.rarnu.base.app.BaseActivity
import com.rarnu.tools.neo.R
import com.rarnu.tools.neo.fragment.ComponentDetailFragment

class ComponentDetailActivity : BaseActivity() {
    override fun getIcon(): Int = R.drawable.ic_launcher

    override fun replaceFragment(): Fragment = ComponentDetailFragment()

    override fun customTheme(): Int = 0

    override fun getActionBarCanBack(): Boolean = true
}
