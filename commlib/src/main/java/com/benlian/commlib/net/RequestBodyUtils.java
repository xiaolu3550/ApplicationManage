package com.benlian.commlib.net;


import com.benlian.commlib.utils.GsonUtils;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RequestBodyUtils {
    public static RequestBody setRequestBody_map_JSON(Map<String, Object> stringObjectMap) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                GsonUtils.toJson(stringObjectMap));
    }

    public static RequestBody setRequestBody_JSON(String json) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    }

    public static RequestBody setRequestBody(String requestBody) {
        return RequestBody.create(MediaType.parse("Accept-Encoding identity"), requestBody);
    }
}
