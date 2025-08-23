package org.example.util;

import java.util.List;

public class Validate {
    private static final String NOT_NULL = " must not be null";
    private static final String NOT_EMPTY = " must not be empty";
    private static final String MUST_BE_POSITIVE_OR_ZERO = " must be equal or higher than 0";


    private Validate (){}

    public static String requireText(String text, String field) {
        if (text == null) throw new IllegalArgumentException(field +NOT_NULL);
        String t = text.trim();
        if (t.isEmpty()) throw new IllegalArgumentException(field + NOT_EMPTY);
        return t;
    }

    public static int requirePositiveOrZero(int n, String field) {
        if (n < 0) throw new IllegalArgumentException(field + MUST_BE_POSITIVE_OR_ZERO);
        return n;
    }

    public static <T> List<T> requireSizeBetween(List<T> list, int min, int max, String field) {
        if (list == null) throw new IllegalArgumentException(field + NOT_NULL);
        int size = list.size();
        if (size < min || size > max) {
            throw new IllegalArgumentException(field + " size must be between " + min + " and " + max);
        }
        return list;
    }
}
