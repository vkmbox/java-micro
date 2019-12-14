package vkmbox.micro.lib.utils;

import org.apache.commons.lang.StringUtils;

public class Lang {

    public static String notEmpty(String ... values) {
        for (String value: values) {
            if (StringUtils.isNotEmpty(value)) {
                return value;
            }
        }
        return "";
    }
    
    public static <T> T nvl(T ... values) {
        for (T value: values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }
}
