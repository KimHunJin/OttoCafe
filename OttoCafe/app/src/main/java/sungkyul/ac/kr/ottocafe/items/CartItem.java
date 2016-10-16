package sungkyul.ac.kr.ottocafe.items;

/**
 * Created by HunJin on 2016-09-13.
 */
public class CartItem {

    private int cNumber;
    private String cName;
    private String cTotalCost;
    private String cCount;
    private String cUrl;

    public int getcNumber() {
        return cNumber;
    }

    public String getcName() {
        return cName;
    }

    public String getcTotalCost() {
        return cTotalCost;
    }

    public String getcCount() {
        return cCount;
    }

    public String getcUrl() {
        return cUrl;
    }

    public CartItem(int number, String name, String cost, String count, String url) {
        this.cNumber = number;
        this.cName = name;
        this.cCount = count;
        this.cTotalCost = cost;
        this.cUrl = url;
    }
}
