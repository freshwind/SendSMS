package com.example.szhangcs.sendsms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by szhangcs on 12/31/15.
 * Currently don't use this class as I found the contact data is not huge.
 * We use SharedPreference to store the data.
 */

public class MyDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SendSms.db";
    public static final String TABLE_NAME = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
    public static final String COLUMN_IS_REPLYED = "IsReplyed";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    COLUMN_PHONE_NUMBER + " INTEGER" + COMMA_SEP +
                    COLUMN_IS_REPLYED + " INTEGER" + COMMA_SEP +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;



    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // This function is actually deleting the database. It should not be used.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // This function is actually deleting the database. It should not be used.
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
