package sungkyul.ac.kr.ottocafe.utils;

/**
 * Created by HunJin on 2016-09-15.
 */
public class EndString {
    public static String endString(String contents, int n) {
        if(contents.length()>n) {
            return contents.substring(0,n) + "...";
        } else {
            return contents;
        }
    }
}
