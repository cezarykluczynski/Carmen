package com.cezarykluczynski.carmen.util

import org.testng.annotations.Test
import org.testng.Assert

import java.util.Collection

import java.util.HashMap
import java.util.Iterator

import org.json.JSONObject

class HashMapJsonConverterTest {

    String jsonString = '{"key1":"value1","key2":2,"key4":false}'

    @Test
    void hashMapToJsonString() {
        // setup
        HashMap<String, Object> hashMap = new HashMap<String, Object>()
        hashMap.put("key1", "value1");
        hashMap.put("key2", 2);
        hashMap.put("key3", null);
        hashMap.put("key4", false);

        // exercise
        String stringifiedHashMap = HashMapJsonConverter.hashMapToJsonString hashMap

        // assertion
        Assert.assertEquals stringifiedHashMap, jsonString
    }

    @Test
    void jsonStringToHashMap() {
        // exercise
        HashMap<String, Object> unstringifiedHashMap = HashMapJsonConverter.jsonStringToHashMap jsonString

        // assertion
        Assert.assertEquals unstringifiedHashMap.size(), 3
        Assert.assertEquals unstringifiedHashMap.get("key1"), "value1"
        Assert.assertEquals unstringifiedHashMap.get("key2"), 2
        Assert.assertEquals unstringifiedHashMap.get("key3"), null
        Assert.assertEquals unstringifiedHashMap.get("key4"), false
    }

}
