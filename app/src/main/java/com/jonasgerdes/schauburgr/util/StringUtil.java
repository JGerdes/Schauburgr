package com.jonasgerdes.schauburgr.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Static collection of various useful string operations
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 07.03.2017
 */

public class StringUtil {

    /**
     * Concatenates an iterable of strings to one and putting given glue strings between parts
     *
     * @param parts List of string to concatenate
     * @param glue  Glue string to put between elements. Isn't insert at begin and end of string
     * @return Concatenated string
     */
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

    /**
     * Removes part of a string
     * @param full String to remove from
     * @param start Start index to start removal
     * @param length Length of part to be removed
     * @return new string without removed part
     */
    public static String remove(String full, int start, int length) {
        return full.substring(0, start) + full.substring(start + length, full.length());
    }

    /**
     * Splits a string by a given separator, similar to {@link String#split(String)}
     * Returns a list of splitted elements instead of an array and removes all entries
     * which are empty (like "")
     *
     * @param toSplit   String to split
     * @param separator Sparator to split at
     * @return List of string containing parts of toSplit
     */
    public static List<String> splitWithoutEmpty(String toSplit, String separator) {
        List<String> splitted = new ArrayList<>();
        for (String splitPart : toSplit.split(separator)) {
            if (!splitPart.isEmpty()) {
                splitted.add(splitPart);
            }
        }
        return splitted;
    }
}
