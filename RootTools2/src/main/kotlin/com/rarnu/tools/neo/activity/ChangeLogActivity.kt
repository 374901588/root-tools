package com.rarnu.tools.neo.activity

import android.app.Fragment
import com.rarnu.tools.neo.R
import com.rarnu.tools.neo.base.BaseActivity
import com.rarnu.tools.neo.fragment.ChangeLogFragment

/**
 * Created by rarnu on 12/7/16.
 */
class ChangeLogActivity : BaseActivity() {

    override fun getIcon(): Int = R.drawable.ic_launcher

    override fun replaceFragment(): Fragment = ChangeLogFragment()

    override fun customTheme(): Int = 0

    override fun getActionBarCanBack(): Boolean = true

}