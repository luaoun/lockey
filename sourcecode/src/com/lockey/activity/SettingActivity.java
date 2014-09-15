package com.lockey.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.lockey.R;

import java.util.*;

/**
 *参数设置活动页面
 */
public class SettingActivity extends Activity implements AdapterView.OnItemClickListener{

    SimpleAdapter adapter;

    GridView gridView;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        if(preferences == null)
            preferences = getSharedPreferences("setting", Context.MODE_PRIVATE);

        gridView = (GridView) findViewById(R.id.gridView);
        String from[] = new String[]{"app_icon","app_name"};
        int to[] = new int[]{R.id.app_icon,R.id.app_name};
        adapter = new SimpleAdapter(this,getDate(),R.layout.item,from,to);
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, final String textRepresentation) {
                if(view instanceof ImageView && data instanceof Drawable){
                    ImageView iconView = (ImageView)view;
                    iconView.setImageDrawable((Drawable) data);
                    return true;
                }
                return false;
            }
        });
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    private List<Map<String,Object>> getDate() {
        List installedApps = new ArrayList<Map<String, Object>>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(mainIntent, PackageManager.SIGNATURE_MATCH);
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
                appInfo.put("app_name",appLabel);
                appInfo.put("app_icon",icon);
                appInfo.put("pkg_name",pkgName);
                appInfo.put("act_name",activityName);
                installedApps.add(appInfo);
            }
        }

        return installedApps;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("main","viewName="+view.getClass().getName()+";viewId="+view.getId());
        Log.i("main","parentView="+parent.getClass().getName());
        Log.i("main","position="+position);
        Log.i("main","id="+id);
        Map itemObject = (Map)parent.getAdapter().getItem(position);
        Log.i("main","item="+itemObject.getClass().getName());
        Log.i("main","item_pkgName="+itemObject.get("pkg_name"));
        Log.i("main","item_actName="+itemObject.get("act_name"));
        TextView textView = (TextView)view.findViewById(R.id.app_name);
        textView.setTextColor(android.graphics.Color.RED);

        SharedPreferences.Editor editor = preferences.edit();
    }
}
