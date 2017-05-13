/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluetech.core;

import java.math.BigDecimal;

/**
 *
 * @author yasin
 */
public class Strings {
    public static boolean isFilled(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof String) {
            return ((String) value).trim().length() != 0;
        }
        return false;
    }

    public static boolean isEmpty(String value) {
        if (value == null || value.trim().length() == 0) return true;
        return false;
    }

    public static String toString(Object value) {

        if (value instanceof String) {
            return (String) value;
        }

        if (value instanceof Integer) {
            return String.valueOf(value);
        }
        if (value instanceof Long) {
            return String.valueOf(value);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toPlainString();
        }
        if (value instanceof Double) {
            return String.valueOf(value);
        }
        if (value.getClass().isEnum()) {
            return ((Enum<?>) value).name();
        }
        if (value instanceof Float) {
            return String.valueOf(value);
        }

        return value.toString();

    }
}
