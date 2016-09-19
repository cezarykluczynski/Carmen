package com.cezarykluczynski.carmen.util;

import com.google.common.collect.Maps;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class HashMapJsonConverter {

    public static String hashMapToJsonString(HashMap<String, Object> hashMap) {
        return new JSONObject(hashMap).toString();
    }

    public static HashMap<String, Object> jsonStringToHashMap(String jsonString) {
        HashMap<String, Object> hashMap = Maps.newHashMap();
        JSONObject json = new JSONObject(jsonString);
        Iterator keys = json.keys();
        String key;

        while (keys.hasNext()) {
            key = (String) keys.next();
            hashMap.put(key, json.get(key));
        }

        return hashMap;
    }

}
