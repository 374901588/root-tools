package com.rarnu.tools.neo.xposed

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by rarnu on 11/17/16.
 */
class MIUIMinusScreen : IXposedHookLoadPackage {

    @Throws(Throwable::class)
    override fun handleLoadPackage(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        val prefs = XSharedPreferences(XpStatus.PKGNAME, XpStatus.PREF)
        prefs.makeWorldReadable()
        prefs.reload()
        if (loadPackageParam.packageName == "com.miui.home") {
            if (prefs.getBoolean(XpStatus.KEY_MINUS_SCREEN, false)) {
                XpUtils.findAndHookMethod("com.miui.home.launcher.X", loadPackageParam.classLoader, "cl", XC_MethodReplacement.returnConstant(true))
                XpUtils.findAndHookMethod("com.miui.home.launcher.DeviceConfig", loadPackageParam.classLoader, "needHideMinusScreen", XC_MethodReplacement.returnConstant(true))
            }
        }
    }
}

