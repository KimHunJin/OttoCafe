package sungkyul.ac.kr.ottocafe.items;

/**
 * Created by HunJin on 2016-09-23.
 */
public class NavItem {
    private int number;
    private String content;
    private int image;

    public int getImage() {
        return image;
    }

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

    public NavItem(int number, String content, int image) {
        this.number = number;
        this.content = content;
        this.image = image;
    }
}
