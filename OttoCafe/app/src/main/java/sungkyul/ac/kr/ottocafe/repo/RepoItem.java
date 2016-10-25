package sungkyul.ac.kr.ottocafe.repo;

import java.util.List;

/**
 * Created by HunJin on 2016-09-23.
 * 모든 아이템 항목은 이 하나의 클래스로 통합할 수 있을거라 생각.
 */
public class RepoItem {
    String err;
    String USER_ID;
    String USER_NAME;
    String THUMBNAIL_IMAGE_PATH;
    List<DrinkListItems> result;

    public String getErr() {
        return err;
    }

    public List<DrinkListItems> getResult() {
        return result;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public String getTHUMBNAIL_IMAGE_PATH() {
        return THUMBNAIL_IMAGE_PATH;
    }
}
