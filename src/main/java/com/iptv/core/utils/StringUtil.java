package com.iptv.core.utils;

import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;


 public class StringUtil
{

    public static boolean isInteger(String str)
 {

        if ((str == null) || ("".equals(str))) {

            return false;

        }

        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");

        return pattern.matcher(str).matches();
    }

    public static boolean isDouble(String str)
{

        if ((str == null) || ("".equals(str))) {

            return false;

        }

        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");

        return pattern.matcher(str).matches();

    }

    public static boolean isNumeric(String str)
 {

        Pattern pattern = Pattern.compile("[0-9]*");

        Matcher isNum = pattern.matcher(str);

        if (!isNum.matches()) {

            return false;

        }

        return true;

    }

}