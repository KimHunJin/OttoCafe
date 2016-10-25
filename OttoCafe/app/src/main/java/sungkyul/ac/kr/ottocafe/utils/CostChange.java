package sungkyul.ac.kr.ottocafe.utils;

/**
 * Created by HunJin on 2016-10-19.
 */

public class CostChange {
    public static String changCost(int cost) {
        String number = cost + "";
        String dump = "";
        String cNumber = "";
        for (int i = number.length() - 1; i >= 0; i--) {
            dump += number.charAt(i);
        }
        for (int i = 0; i < dump.length(); i += 3) {
            if (i + 3 < dump.length()) {
                cNumber += dump.substring(i, i + 3) + ",";
            } else {
                cNumber += dump.substring(i, dump.length());
            }
        }
        number = "";
        for (int i = cNumber.length() - 1; i >= 0; i--) {
            number += cNumber.charAt(i);
        }
        return number;
    }

    public static String changCost(String cost) {
        String number = cost + "";
        String dump = "";
        String cNumber = "";
        for (int i = number.length() - 1; i >= 0; i--) {
            dump += number.charAt(i);
        }
        for (int i = 0; i < dump.length(); i += 3) {
            if (i + 3 < dump.length()) {
                cNumber += dump.substring(i, i + 3) + ",";
            } else {
                cNumber += dump.substring(i, dump.length());
            }
        }
        number = "";
        for (int i = cNumber.length() - 1; i >= 0; i--) {
            number += cNumber.charAt(i);
        }
        return number;
    }
}
