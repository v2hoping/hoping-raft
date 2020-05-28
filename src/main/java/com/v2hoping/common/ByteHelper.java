package com.v2hoping.common;


import com.alibaba.fastjson.JSON;

import java.nio.charset.Charset;

/**
 * Created by houping wang on 2020/5/14
 *
 * @author houping wang
 */
public class ByteHelper {

    public static final Charset CHARSET = Charset.forName("UTF-8");

    public static byte[] serialize(Object obj) {
        return JSON.toJSONString(obj).getBytes(CHARSET);
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(new String(bytes, CHARSET), clazz);
    }
}
