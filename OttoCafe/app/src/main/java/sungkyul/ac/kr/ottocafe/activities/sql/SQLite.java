package sungkyul.ac.kr.ottocafe.activities.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HunJin on 2016-10-15.
 * 장바구니 저장을 위해 사용될 안드로이드 내부 디비
 */

public class SQLite extends SQLiteOpenHelper{

    public SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table `cart` (key INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name VARCHAR(30)," +
                " number VARCHAR(3)," +
                " cost VARCHAR(10)," +
                " img VARCHAR(100));");
    }

    /**
     * 값을 검색하는 메서드
     * 사용법 참고 : https://github.com/KimHunJin/Gravity/blob/master/GravityMemberManagementApplication/app/src/main/java/com/dxmnd/gravitymembermanagementapplication/activity/MemberListActivity.java
     * @return cursor
     */
    public Cursor select() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * from `cart`";
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    /**
     * 장바구니에 들어갈 항목을 저장하는 메서드
     * @param name
     * @param number
     * @param cost
     * @param img
     */
    public void insert(String name, int number, int cost, String img) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "INSERT into `cart` values(null, '" + name + "', '" + number + "', '" + cost + "' , '" + img + "');";
        db.execSQL(query);
        db.close();
    }

    /**
     * 장바구니에서 항목을 삭제하는 메서드
     * @param name
     */
    public void delete(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE * from `cart` where `name`= '" + name + "';";
        db.execSQL(query);
        db.close();
    }

    /**
     * 장바구니의 모든 항목을 삭제하는 메서드
     */
    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE * from `cart`:";
        db.execSQL(query);
        db.close();
    }

    /**
     * SQLite가 버전업될 때 사용되는 메서드 (딱히 사용할 일 없음)
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS device";
        db.execSQL(sql);
        onCreate(db);
    }
}
