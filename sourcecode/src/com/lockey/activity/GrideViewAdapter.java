package com.lockey.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import com.google.gson.Gson;
import com.lockey.R;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by whatsupstar on 2014/9/17.
 */
public class GrideViewAdapter extends SimpleAdapter{

    private LayoutInflater inflater;

    private Set<String> choicedApps = new HashSet<String>();

    SharedPreferences preferences;

    public GrideViewAdapter(SharedPreferences preferences,Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.preferences = preferences;
        inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View superView = super.getView(position, convertView, parent);

        Map itemData = (Map)getItem(position);

        ResolveInfo resolveInfo = (ResolveInfo) itemData.get("app_name");

        String packageName = resolveInfo.activityInfo.packageName;

        if (choicedApps.contains(packageName)) {
            superView.setBackgroundColor(Color.WHITE);
        } else{
            superView.setBackgroundColor(Color.BLUE);
        }

        return superView;
    }


    public void setChoicedApps(Set<String> choicedApps){
        this.choicedApps = choicedApps;
    }

    public Set<String> getChoicedApps(){
        return choicedApps;
    }

    public void addChoicedApps(String packageName){
        choicedApps.add(packageName);
    }

    public int getChoicedAppSize(){
        return choicedApps.size();
    }

    public boolean removeChoicedApp(String packageName){
        return choicedApps.remove(packageName);
    }

    public void save() {
        Gson gson = new Gson();
        String json = gson.toJson(choicedApps);
        preferences.edit().putString("selectedApps",json).commit();
    }


    public void reflashSettingButtonText(Button settingButton){
        //改变setting 按钮文字
        String btnText = inflater.getContext().getResources().getString(R.string.setting_btn1);
        String btnNewText  = MessageFormat.format(btnText, new Object[]{getChoicedAppSize()});
        settingButton.setText(btnNewText);
    }
}
