package com.rarnu.tools.neo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.rarnu.tools.neo.data.AppInfo;
import com.rarnu.tools.neo.data.BanStartInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rarnu on 9/2/16.
 */
public class AppUtils {

    public static List<AppInfo> getSystemApps(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        List<PackageInfo> pkgs = pm.getInstalledPackages(0);

        List<AppInfo> list = new ArrayList<>();
        List<AppInfo> listDisabled = new ArrayList<>();
        if (pkgs != null) {
            for (PackageInfo pkg : pkgs) {
                if ((pkg.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                    if (pkg.applicationInfo.enabled) {
                        list.add(new AppInfo(
                                pkg.applicationInfo.loadLabel(pm).toString(),
                                pkg.applicationInfo.loadIcon(pm),
                                pkg.packageName,
                                false,
                                pkg.versionName
                        ));
                    } else {
                        listDisabled.add(new AppInfo(
                                pkg.applicationInfo.loadLabel(pm).toString(),
                                pkg.applicationInfo.loadIcon(pm),
                                pkg.packageName,
                                true,
                                pkg.versionName
                        ));
                    }
                }
            }
        }
        list.addAll(listDisabled);
        return list;
    }

    public static List<AppInfo> getInstalledApps(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        List<PackageInfo> pkgs = pm.getInstalledPackages(0);
        List<AppInfo> list = new ArrayList<>();
        if (pkgs != null) {
            for (PackageInfo pkg : pkgs) {
                if (pkg.applicationInfo.enabled) {
                    list.add(new AppInfo(
                            pkg.applicationInfo.loadLabel(pm).toString(),
                            pkg.applicationInfo.loadIcon(pm),
                            pkg.packageName,
                            true,
                            pkg.versionName
                    ));
                }
            }
        }
        return list;
    }

    public static List<BanStartInfo> getAppsWithBannedStatue(Context ctx, SharedPreferences pref) {
        PackageManager pm = ctx.getPackageManager();
        List<PackageInfo> pkgs = pm.getInstalledPackages(0);
        List<BanStartInfo> list = new ArrayList<>();
        List<BanStartInfo> listSystem = new ArrayList<>();
        if (pkgs != null) {
            for (PackageInfo pkg : pkgs) {
                if (pkg.applicationInfo.enabled) {
                    if ((pkg.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                        listSystem.add(new BanStartInfo(
                                pkg.applicationInfo.loadLabel(pm).toString(),
                                pkg.applicationInfo.loadIcon(pm),
                                pkg.packageName,
                                pkg.versionName,
                                true,
                                pref.getBoolean("banned_" + pkg.packageName, false)
                        ));
                    } else {
                        list.add(new BanStartInfo(
                                pkg.applicationInfo.loadLabel(pm).toString(),
                                pkg.applicationInfo.loadIcon(pm),
                                pkg.packageName,
                                pkg.versionName,
                                false,
                                pref.getBoolean("banned_" + pkg.packageName, false)
                        ));
                    }

                }
            }
        }
        list.addAll(listSystem);
        return list;
    }

}
