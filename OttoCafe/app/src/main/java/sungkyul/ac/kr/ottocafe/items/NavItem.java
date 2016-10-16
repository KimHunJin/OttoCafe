package sungkyul.ac.kr.ottocafe.items;

/**
 * Created by HunJin on 2016-09-23.
 */
public class NavItem {
    private int number;
    private String content;

    public int getNumber() {
        return number;
    }

    public String getContent() {
        return content;
    }

    public NavItem(int number, String content) {
        this.number = number;
        this.content = content;
    }
}
