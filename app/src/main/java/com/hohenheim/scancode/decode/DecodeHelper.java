package com.hohenheim.scancode.decode;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by hohenheim on 2017/12/24.
 */

public class DecodeHelper {

    /**
     * 进行中文乱码处理
     *
     * @param str
     * @return
     */
    public static String recode(String str) {
        String format;
        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder().canEncode(str);
            if (ISO) {
                format = new String(str.getBytes("ISO-8859-1"), "GB2312");
            } else {
                format = str;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        return format;
    }
}
