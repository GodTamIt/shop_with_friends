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
    private static final int DATABASE_VERSION = 3;

    /**
     * This method should only be called by the Application class.
     * @param context - the application's context to create the database.
     */
    public static void initInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
    }

    /**
     * Returns the application's instance of the database.
     * @return the application's single instance of DBHelper.
     */
    public static DBHelper getInstance() {
        return instance;
    }

    //endregion

    //region Database Tables

    public static class USERS_TABLE {
        public static final String NAME = "users";

        public static final String KEY_ID = "id";
        public static final String KEY_USERNAME = "username";
        public static final String KEY_PASSWORD = "password";
        public static final String KEY_EMAIL = "email";

        private static final String CREATE = "CREATE TABLE " + NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERNAME + " TEXT," + KEY_PASSWORD + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")";
    }

    public static class FRIENDS_TABLE {
        public static final String NAME = "friends";

        public static final String KEY_ID = "id";
        public static final String KEY_FRIEND_ID = "friend_id";

        private static final String CREATE = "CREATE TABLE " + NAME + "("
                + KEY_ID + " LONG," +
                KEY_FRIEND_ID + " TEXT" + ")";
    }

    public static class POSTS_TABLE {
        public static final String NAME = "posts";

        public static final String KEY_ID = "id";
        public static final String KEY_ITEM_NAME = "item_name";
        public static final String KEY_MAX_PRICE = "max_price";

        private static final String CREATE = "CREATE TABLE " + NAME + "("
                + KEY_ID + " LONG," +
                KEY_ITEM_NAME + " TEXT," +
                KEY_MAX_PRICE + " FLOAT" + ")";
    }

    //endregion

    //region Overridden Methods

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USERS_TABLE.CREATE);
        db.execSQL(FRIENDS_TABLE.CREATE);
        db.execSQL(POSTS_TABLE.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FRIENDS_TABLE.NAME);


        // Create tables again
        onCreate(db);
    }

    //endregion

}

