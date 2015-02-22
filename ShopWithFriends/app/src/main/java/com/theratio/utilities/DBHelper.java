package com.theratio.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by filoleg on 2/19/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    //region Static Members & Methods

    private static final String DATABASE_NAME = "com.theratio.ShopWithFriends.db";
    private static DBHelper instance;
    private static final int DATABASE_VERSION = 2;

    public static void initInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
    }

    public static DBHelper getInstance() {
        return instance;
    }


    //endregion
    

    //region Database Tables

    public static class USERS_TABLE {
        // Contacts table name
        public static final String NAME = "users";

        // Contacts Table Columns names
        public static final String KEY_ID = "id";
        public static final String KEY_USERNAME = "username";
        public static final String KEY_PASSWORD = "password";
        public static final String KEY_EMAIL = "email";

        // Creating Tables
        private static final String CREATE = "CREATE TABLE " + NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERNAME + " TEXT," + KEY_PASSWORD + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")";
    }

    public static class FRIENDS_TABLE {
        // Contacts table name
        public static final String NAME = "friends";

        // Contacts Table Columns names
        public static final String KEY_ID = "id";
        public static final String KEY_FRIEND_ID = "friend_id";

        // Creating Tables
        private static final String CREATE = "CREATE TABLE " + NAME + "("
                + KEY_ID + " LONG," +
                KEY_FRIEND_ID + " TEXT" + ")";
    }

    //endregion


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USERS_TABLE.CREATE);
        db.execSQL(FRIENDS_TABLE.CREATE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FRIENDS_TABLE.NAME);


        // Create tables again
        onCreate(db);
    }
}