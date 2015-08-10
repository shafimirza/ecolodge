package com.example.root.myapplication_eco;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 3/16/15.
 */
public class Utility {
    private static Pattern pattern;
    private static Matcher matcher;
    //Email Pattern
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    /**
     * Validate Email with regular expression
     *
     * @param uid
     * @return true for Valid Email and false for Invalid Email
     */
    public static boolean validate(String uid) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(uid);
        return matcher.matches();
    }
    /**
     * Checks for Null String object
     *
     * @param txt
     * @return true for not null and false for null String object
     */
    public static boolean isNotNull(String txt) {
        return txt != null && txt.trim().length() > 0 ? true : false;
    }
}

