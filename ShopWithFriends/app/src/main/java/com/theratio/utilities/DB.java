package com.theratio.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by filoleg on 2/19/15.
 */
public class DB extends SQLiteOpenHelper {

    //region Static Members & Methods

    private static final String DATABASE_NAME = "com.theratio.ShopWithFriends.db";
    private static DB instance;
    private static final int DATABASE_VERSION = 10;

    /**
     * This method should only be called by the Application class.
     * @param context - the application's context to create the database.
     */
    public static void initInstance(Context context) {
        if (instance == null) {
            instance = new DB(context);
        }
    }

    /**
     * Returns the application's instance of the database.
     * @return the application's single instance of <code>DB</code>.
     */
    public static DB getInstance() {
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

        private static final String CREATE = "CREATE TABLE " + NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_USERNAME + " VARCHAR(20)," +
                KEY_PASSWORD + " VARCHAR(100)," +
                KEY_EMAIL + " VARCHAR(100)" + ")";
    }

    public static class FRIENDS_TABLE {
        public static final String NAME = "friends";

        public static final String KEY_ID = "id";
        public static final String KEY_FRIEND_ID = "friend_id";

        private static final String CREATE = "CREATE TABLE " + NAME + "(" +
                KEY_ID + " LONG," +
                KEY_FRIEND_ID + " LONG" + ")";
    }

    public static class POSTS_TABLE {
        public static final String NAME = "posts";

        public static final String KEY_POST_ID = "post_id";
        public static final String KEY_USER_ID = "user_id";
        public static final String KEY_POST_TYPE = "post_type";
        public static final String KEY_ITEM_NAME = "item_name";
        public static final String KEY_WORST_PRICE = "worst_price";
        public static final String KEY_AUTO_PRICE = "auto_price";
        public static final String KEY_DESCRIPTION = "description";

        private static final String CREATE = "CREATE TABLE " + NAME + "(" +
                KEY_POST_ID + " INTEGER PRIMARY KEY," +
                KEY_POST_TYPE + " INTEGER," +
                KEY_USER_ID + " LONG," +
                KEY_ITEM_NAME + " VARCHAR(50)," +
                KEY_WORST_PRICE + " FLOAT," +
                KEY_AUTO_PRICE + " FLOAT," +
                KEY_DESCRIPTION + " TEXT" + ")";
    }

    //endregion

    //region Overridden Methods

    public DB(Context context) {
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
        db.execSQL("DROP TABLE IF EXISTS " + POSTS_TABLE.NAME);


        // Create tables again
        onCreate(db);
    }

    //endregion

}

