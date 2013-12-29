package com.smilehacker.dongxi.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by kleist on 13-9-10.
 */
public class UrlUtils {
    public static HashMap<String, String> UrlParse(String url) {

        String[] urlParams = url.split("[?]");
        String[] params = urlParams[1].split("&");
        HashMap<String, String> map = new HashMap<String, String>(params.length + 1);
        map.put("url", urlParams[0]);

        for (String param : params) {
            String[] vk = param.split("=");
            map.put(vk[0], vk[1]);
        }

        return map;
    }

    public static String UrlBuilder(String baseUrl, HashMap<String, Object> paramas) {
        StringBuffer url = new StringBuffer();
        url.append(baseUrl);
        if (paramas != null) {
            url.append("?");
        }

        Iterator iter = paramas.entrySet().iterator();
        Boolean hasNext = iter.hasNext();
        while(hasNext) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();

            if (key == null || val == null) {
                continue;
            }
            url.append(key);
            url.append("=");
            url.append(val);

            hasNext = iter.hasNext();
            if (hasNext) {
                url.append("&");
            }
        }

        return url.toString();
    }

}

