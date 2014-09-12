package com.lockey.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import com.lockey.R;

import java.util.*;

/**
 *参数设置活动页面
 */
public class SettingActivity extends Activity{

    SimpleAdapter adapter;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        gridView = (GridView) findViewById(R.id.gridView);
        String from[] = new String[]{"item_pic","item_name"};
        int to[] = new int[]{R.id.item_pic,R.id.item_name};
        adapter = new SimpleAdapter(this,getDate(),R.layout.item,from,to);
        gridView.setAdapter(adapter);
    }

    private List<Map<String,Object>> getDate() {
        List installedApps = new ArrayList<Map<String, Object>>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(mainIntent, PackageManager.GET_ACTIVITIES);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(getPackageManager()));
        if (installedApps != null) {
            installedApps.clear();
            for (ResolveInfo reInfo : resolveInfos) {
                String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
                String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
                String appLabel = (String) reInfo.loadLabel(getPackageManager()); // 获得应用程序的Label
                Drawable icon = reInfo.loadIcon(getPackageManager()); // 获得应用程序图标
                Map appInfo = new HashMap();
                appInfo.put("item_name",appLabel);
                appInfo.put("item_pic",icon.getCurrent());
                installedApps.add(appInfo);
            }
        }
//        List<PackageInfo> installedPackages = getPackageManager().getInstalledPackages(PackageManager.SIGNATURE_MATCH);
//        for (int i = 0; i < installedPackages.size(); i++) {
//            PackageInfo installedPackage = installedPackages.get(i);
//            Map appInfo = new HashMap<String, Object>();
//            appInfo.put("item_name", installedPackage.);
//            appInfo.put("item_pic", installedPackage.applicationInfo.loadIcon(getPackageManager()));
//            installedApps.add(appInfo);
//        }

        return installedApps;
    }
}
