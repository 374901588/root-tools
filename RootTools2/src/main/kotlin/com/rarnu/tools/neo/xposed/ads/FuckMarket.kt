package com.rarnu.tools.neo.xposed.ads

import android.content.Loader
import com.rarnu.tools.neo.xposed.XpUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by rarnu on 12/6/16.
 */
object FuckMarket {

    fun fuckMarket(loadPackageParam: XC_LoadPackage.LoadPackageParam) {

        // 3.x
//        XpUtils.findAndHookMethod("com.xiaomi.market.ui.UpdateHistoryFragmentPhone", loadPackageParam.classLoader, "lK", XC_MethodReplacement.returnConstant(null))
//        XpUtils.findAndHookMethod("com.xiaomi.market.ui.UpdateHistoryFragmentPhone", loadPackageParam.classLoader, "lH", XC_MethodReplacement.returnConstant(null))
//        XpUtils.findAndHookMethod("com.xiaomi.market.ui.UpdateAppsFragmentPhone", loadPackageParam.classLoader, "lH", XC_MethodReplacement.returnConstant(null))

        val clsCg = XpUtils.findClass(loadPackageParam.classLoader, "com.xiaomi.market.data.cg")
        if (clsCg != null) {
            XpUtils.findAndHookMethod("com.xiaomi.market.ui.UpdateAppsFragmentPhone", loadPackageParam.classLoader, "a", Loader::class.java, clsCg, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val loader = param.args[0] as Loader<*>
                    if (loader.id == 1) {
                        val list = param.args[1]
                        val fPL = list.javaClass.getDeclaredField("pL")
                        val l = fPL.get(list) as MutableList<*>?
                        l?.clear()
                    }
                }
            })
            XpUtils.findAndHookMethod("com.xiaomi.market.ui.UpdateHistoryFragmentPhone", loadPackageParam.classLoader, "a", Loader::class.java, clsCg, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val loader = param.args[0] as Loader<*>
                    val list = param.args[1]
                    when (loader.id) {
                        1 -> {
                            val fPT = list.javaClass.getDeclaredField("pT")
                            val l = fPT.get(list) as MutableList<*>?
                            l?.clear()
                        }
                        2 -> {
                            val fPL = list.javaClass.getDeclaredField("pL")
                            val l = fPL.get(list) as MutableList<*>?
                            l?.clear()
                        }
                    }
                }
            })
        }

        // 6.x
        val clsCy = XpUtils.findClass(loadPackageParam.classLoader, "com.xiaomi.market.data.cy")
        if (clsCy != null) {
            XpUtils.findAndHookMethod("com.xiaomi.market.ui.UpdateHistoryFragmentPhone", loadPackageParam.classLoader, "a", Loader::class.java, clsCy, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val loader = param.args[0] as Loader<*>
                    val list = param.args[1]
                    when (loader.id) {
                        1 -> {
                            val fASN = list.javaClass.getDeclaredField("asN")
                            val l = fASN.get(list) as MutableList<*>?
                            l?.clear()
                        }
                        2 -> {
                            val fASE = list.javaClass.getDeclaredField("asE")
                            val l = fASE.get(list) as MutableList<*>?
                            l?.clear()
                        }
                    }
                }
            })
            XpUtils.findAndHookMethod("com.xiaomi.market.ui.UpdateAppsFragmentPhone", loadPackageParam.classLoader, "a", Loader::class.java, clsCy, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val loader = param.args[0] as Loader<*>
                    if (loader.id == 1) {
                        val list = param.args[1]
                        val fASE = list.javaClass.getDeclaredField("asE")
                        val l = fASE.get(list) as MutableList<*>?
                        l?.clear()
                    }
                }
            })
        }

    }
}