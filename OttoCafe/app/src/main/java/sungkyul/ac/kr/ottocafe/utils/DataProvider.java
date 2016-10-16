package sungkyul.ac.kr.ottocafe.utils;


import java.util.HashMap;

/**
 * Created by HunJin on 2016-09-11.
 */
public class DataProvider {

    // StaticURL 파싱을 하기 위해서는 이 부분을 사용하세요.
    HashMap<String, String> file_maps;

    // 이미지로부터 가져오기 위해서는 이 부분을 사용하세요.
    HashMap<String, Integer> file_maps_src;

    public DataProvider() {
        file_maps = new HashMap<>();
        file_maps_src = new HashMap<>();
    }

    public HashMap<String, String> getFileSrcHorizontal() {
        return file_maps;
    }

    public HashMap<String, Integer> getFileSrc() {
        return file_maps_src;
    }

    public void setHashFile(String fileNmae, String url) {
        file_maps.put(fileNmae, url);
    }

    public void setHashFile(String fileNmae, int url) {

        file_maps_src.put(fileNmae, url);
    }
}
