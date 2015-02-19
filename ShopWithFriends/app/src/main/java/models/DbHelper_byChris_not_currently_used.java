package models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by filoleg on 2/19/15.
 */
public class DbHelper_byChris_not_currently_used extends SQLiteOpenHelper {
    // All Static variables
    private static final DbHelper_byChris_not_currently_used instance = new DbHelper_byChris_not_currently_used(MyApplication.getAppContext());
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ShopWithFriends.db";

    public static DbHelper_byChris_not_currently_used getInstance() {
        return instance;
    }

    private static class USERS_TABLE {
        // Contacts table name
        public static final String NAME = "users";

        // Contacts Table Columns names
        public static final String KEY_ID = "id";
        public static final String KEY_USERNAME = "username";
        public static final String KEY_PASSWORD = "password";
        public static final String KEY_EMAIL = "email";

        // Creating Tables
        public static final String CREATE = "CREATE TABLE " + NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERNAME + " TEXT," + KEY_PASSWORD + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")";
    }

    private static class FRIENDS_TABLE {
        // Contacts table name
        public static final String NAME = "friends";

        // Contacts Table Columns names
        public static final String KEY_ID = "id";
        public static final String KEY_FRIEND_ID = "friend_id";

        // Creating Tables
        public static final String CREATE = "CREATE TABLE " + NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_FRIEND_ID + " TEXT" + ")";
    }


    public DbHelper_byChris_not_currently_used(Context context) {
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