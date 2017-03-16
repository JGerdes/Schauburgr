package com.jonasgerdes.schauburgr.util;

/**
 * Created by jonas on 07.03.2017.
 */

public class StringUtil {

    public static String concat(Iterable<String> parts, String glue) {
        String concated = "";
        for (String part : parts) {
            if (!concated.isEmpty()) {
                concated += glue;
            }
            concated += part;
        }
        return concated;
    }

    public static String remove(String full, int start, int length) {
        return full.substring(0, start) + full.substring(start + length, full.length());
    }
}
