package sungkyul.ac.kr.ottocafe.utils;

/**
 * Created by HunJin on 2016-09-15.
 * 말 줄임표
 */
public class EndString {
    /**
     * 문자열 contents를 n번째 이후는 모두 ...으로 대체
     * @param contents
     * @param n
     * @return
     */
    public static String endString(String contents, int n) {
        if(contents.length()>n) {
            return contents.substring(0,n) + "...";
        } else {
            return contents;
        }
    }
}
