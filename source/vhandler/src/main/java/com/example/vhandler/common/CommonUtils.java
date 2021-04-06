package com.example.vhandler.common;

import org.apache.commons.collections4.MapUtils;

import java.util.Map;

public class CommonUtils {

    public static String buildParamUrl(String apiUrl, String endPoint, Map<String, Object> requestParam) {
        StringBuilder buffer = new StringBuilder(apiUrl).append(endPoint).append("?");
        if (MapUtils.isNotEmpty(requestParam)) {
            for(Map.Entry<String, Object> entry : requestParam.entrySet()) {
                buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        String url = buffer.toString();
        return url.substring(0, url.length() - 1);
    }
}
