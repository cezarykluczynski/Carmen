package com.cezarykluczynski.carmen.util

import com.google.common.collect.Maps
import spock.lang.Specification

class HashMapJsonConverterTest extends Specification {

    private static final String JSON_STRING = '{"key1":"value1","key2":2,"key4":false}'

    def "converts hash map to json string"() {
        given:
        HashMap<String, Object> hashMap = Maps.newHashMap()
        hashMap.put("key1", "value1")
        hashMap.put("key2", 2)
        hashMap.put("key3", null)
        hashMap.put("key4", false)

        when:
        String hashMapAsString = HashMapJsonConverter.hashMapToJsonString hashMap

        then:
        hashMapAsString == JSON_STRING
    }

    void "covnerts json string to hash map"() {
        when:
        HashMap<String, Object> hashMap = HashMapJsonConverter.jsonStringToHashMap JSON_STRING

        then:
        hashMap.size() == 3
        hashMap.get("key1") == "value1"
        hashMap.get("key2") == 2
        hashMap.get("key3") == null
        hashMap.get("key4") == false
    }

}
