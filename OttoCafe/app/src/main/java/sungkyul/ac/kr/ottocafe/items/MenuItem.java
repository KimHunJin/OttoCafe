package sungkyul.ac.kr.ottocafe.items;

/**
 * Created by HunJin on 2016-09-15.
 *
 * this class is item for menu list
 */
public class MenuItem {

    private int type;
    private int mNumber;
    private String mName;
    private String mCost;
    private String mExplain;
    private String mImageUrl;

    public int getType() {
        return type;
    }

    public int getmNumber() {
        return mNumber;
    }

    public String getmName() {
        return mName;
    }

    public String getmCost() {
        return mCost;
    }

    public String getmExplain() {
        return mExplain;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public MenuItem(int type, int number, String name, String cost, String explain, String url) {
        this.type = type;
        this.mNumber = number;
        this.mName = name;
        this.mCost = cost;
        this.mExplain = explain;
        this.mImageUrl = url;
    }
}
