package com.lockey.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.lockey.R;
import org.json.JSONException;

import java.text.MessageFormat;
import java.util.*;

/**
 *参数设置活动页面
 */
public class SettingActivity extends Activity implements AdapterView.OnItemClickListener{

    GrideViewAdapter adapter;

    GridView gridView;

    Button settingButton;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        if(preferences == null)
            preferences = PreferenceManager.getDefaultSharedPreferences(this);

        settingButton = (Button)findViewById(R.id.btn1);

        gridView = (GridView) findViewById(R.id.gridView);

        String from[] = new String[]{"app_name"};
        int to[] = new int[]{R.id.app_name};

        adapter = new GrideViewAdapter(preferences,this,getDate(),R.layout.item,from,to);

        String selectedAppsJsonStr = preferences.getString("selectedApps", "");
        Log.i("main",selectedAppsJsonStr);
        Gson gson = new Gson();

        HashSet selectedApps = gson.fromJson(selectedAppsJsonStr, HashSet.class);

        adapter.setChoicedApps(selectedApps);

        //渲染每个Item时的处理
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, final String textRepresentation) {
                if(view instanceof ImageView && data instanceof Drawable){
                    ImageView iconView = (ImageView)view;
                    iconView.setImageDrawable((Drawable) data);
                    return true;
                }else if(view instanceof TextView ){
                    TextView textView = (TextView)view;
                    ResolveInfo resolveInfo = (ResolveInfo)data;
                    Drawable icon = resolveInfo.loadIcon(getPackageManager());
                    int width = icon.getIntrinsicWidth();
                    int height = icon.getIntrinsicHeight();
                    float density = getApplicationContext().getResources().getDisplayMetrics().density;
                    int actWidth = (int)(width * density);
                    int actHeight = (int)(height * density);
                    Log.i("main","At "+density +"DIP ,icon size is :" +actWidth+" x "+actHeight);
                    textView.setCompoundDrawablesWithIntrinsicBounds(null,icon,null,null);
                    textView.setText(resolveInfo.loadLabel(getPackageManager()));
                    return true;
                }
                return false;
            }
        });
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
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
                appInfo.put("app_name",reInfo);
                installedApps.add(appInfo);
            }
        }

        return installedApps;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        adapter.notifyDataSetChanged();

        adapter.reflashSettingButtonText(settingButton);

        adapter.save();
    }

    public static void main(String[] args) throws JSONException {

        String arrays[] = new String[]{"a","b","c"};
        HashSet<String> set = new HashSet<String>();
        set.add("a");
        set.add("b");
        set.add("c");
        Gson gson = new Gson();
        String setJson = gson.toJson(set);
        HashSet obj = gson.fromJson(setJson, HashSet.class);
        Iterator iterator = obj.iterator();
        while(iterator.hasNext()){
            String next = (String)iterator.next();
            System.out.println(next);
        }

        obj.addAll(set);

        System.out.printf(""+obj.size());
    }
}
