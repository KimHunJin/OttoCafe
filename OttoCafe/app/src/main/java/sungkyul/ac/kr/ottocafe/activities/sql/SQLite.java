package sungkyul.ac.kr.ottocafe.activities.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HunJin on 2016-10-15.
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

    public Cursor select() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * from `cart`";
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public void insert(String name, int number, int cost, String img) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "INSERT into `cart` values(null, '" + name + "', '" + number + "', '" + cost + "' , '" + img + "');";
        db.execSQL(query);
        db.close();
    }

    public void delete(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE * from `cart` where `name`= '" + name + "';";
        db.execSQL(query);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS device";
        db.execSQL(sql);
        onCreate(db);
    }
}
