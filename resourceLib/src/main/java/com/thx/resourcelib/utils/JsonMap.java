package com.thx.resourcelib.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonMap {
    /**
     * 将json 数组转换为Map 对象
     *
     * @param jsonString
     */

    public static Map<String, String> getMap(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            Iterator<String> keyIterator = jsonObject.keys();
            String key;
            String value;
            Map<String, String> valueMap = new HashMap<>();
            while (keyIterator.hasNext()) {
                key = keyIterator.next();
                value = jsonObject.getString(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 把json 转换为ArrayList 形式
     */
    public static List<Map<String, String>> getList(String jsonString) {
        List<Map<String, String>> list = null;
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject;
            list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                list.add(getMap(jsonObject.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}