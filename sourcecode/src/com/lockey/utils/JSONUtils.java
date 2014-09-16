package com.lockey.utils;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by liuxj on 14-9-15.
 */
public class JSONUtils {

    public static JSONObject getJSON(HashSet set) {
        Iterator iter = set.iterator();
        JSONObject data = new JSONObject();

            JSONArray jsonArray = new JSONArray(set);
        return data;
    }
}
