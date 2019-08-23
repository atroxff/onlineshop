package cn.ff.onlineshop.utils;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class SysUtil {
    /** 获取UUID */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        // 去掉“-”符号
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }

    /** 将String转换成Integer */
    public static Integer strToInteger(String str) {
        if (StringUtils.isNotBlank(str)) {
            return Integer.valueOf(str);
        }
        return null;
    }

    /* String 类型的数字++ */
    public static String strNumAdd(String str) {

        if (StringUtils.isNotBlank(str)) {
            int i = Integer.parseInt(str);
           return Integer.toString( ++i);
        }
        return null;
    }
}
