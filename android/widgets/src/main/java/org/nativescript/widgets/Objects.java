package org.nativescript.widgets;

/**
 * Created by hristov on 6/23/16.
 */
public class Objects {
    /**
     * Null-safe equivalent of {@code a.equals(b)}.
     */
    public static boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    public static boolean isNullOrEmpty(String s){
        return s == null || s.isEmpty();
    }
}
