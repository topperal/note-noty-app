package com.example.shortmess;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицы с полями
        // связь id в folderShape и idTitle messengerList позволяет связать две таблицы
        db.execSQL("create table folderShape ("
                + "id integer primary key autoincrement,"
                + "colour integer,"
                + "divider text,"
                + "title text" + ");");

        db.execSQL("create table messengerList ("
                + "id integer primary key autoincrement,"
                + "idTitle integer,"
                + "body text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
