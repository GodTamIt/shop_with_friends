package com.theratio.utilities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.theratio.ShopWithFriends;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores data about User.
 */
public class User implements Serializable {

    //region Declarations
    private static final long serialVersionUID = -7060210544600464481L;

    private long id;
    private String userName;
    private String email;
    private Boolean isAdmin;
    private long rating;
    private long salesReportsNum;
    Drawable profilePicture;
    //endregion


    //region Constructors

    public User(long id, String userName, String email, Boolean isAdmin) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.rating = 0L; // default
        this.salesReportsNum = 0L; // default
        this.profilePicture = null; // drawing activity should handle default
    }

    public User(long id, String userName, String email, Boolean isAdmin, long rating, long salesReportsNum, Drawable profilePicture) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.rating = rating;
        this.salesReportsNum = salesReportsNum;
        this.profilePicture = profilePicture;
    }

    //endregion


    //region Encapsulation Methods

    public long getID() { return id; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public Drawable getProfilePicture() { return profilePicture; }

    //endregion


    //region Instance Methods

    public static enum AddFriendResult {
        SUCCESS, ALREADY_FRIENDS, NO_SUCH_USER, UNKNOWN
    }

    public AddFriendResult addFriend(String friendEmail, String friendUsername) {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();

        String query = String.format("SELECT %s FROM %s WHERE %s=? AND %s=?",
                DBHelper.USERS_TABLE.KEY_ID,
                DBHelper.USERS_TABLE.NAME,
                DBHelper.USERS_TABLE.KEY_EMAIL,
                DBHelper.USERS_TABLE.KEY_USERNAME);

        Cursor cursor = db.rawQuery(query, new String[] {friendEmail, friendUsername});


        if (cursor.moveToFirst()) {
            // User exists - get Friend's ID from
            cursor.moveToFirst();
            long friendID = cursor.getLong(cursor.getColumnIndex(DBHelper.USERS_TABLE.KEY_ID));

            // Close cursor
            cursor.close();

            db = DBHelper.getInstance().getWritableDatabase();


            // Create SQL entry
            ContentValues values = new ContentValues();
            values.put(DBHelper.FRIENDS_TABLE.KEY_ID, this.getID());
            values.put(DBHelper.FRIENDS_TABLE.KEY_FRIEND_ID, friendID);

            try
            {
                db.insert(DBHelper.FRIENDS_TABLE.NAME, null, values);
                return AddFriendResult.SUCCESS;
            }
            catch(Exception e)
            {
                Log.e("AddFriend", "Error occurred when adding friend", e);
                e.printStackTrace();
            }
        }

        return AddFriendResult.NO_SUCH_USER;
    }

    public void getFriends(List<User> friends, RecyclerView.Adapter adapterToNotify) {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();

        // Get all friends of current user
        String query = String.format("SELECT %s FROM %s WHERE %s=?",
                DBHelper.FRIENDS_TABLE.KEY_FRIEND_ID,
                DBHelper.FRIENDS_TABLE.NAME,
                DBHelper.FRIENDS_TABLE.KEY_ID);

        String userID = Long.toString(getID());

        Log.d("tskLoadFriends", "Loading friends for " + userID);

        Cursor cursor = db.rawQuery(query,
                new String[]{ userID });

        if (cursor.getCount() < 1)
            return;

        Log.d("tskLoadFriends", String.format("Loaded %d friend(s) for %s", cursor.getCount(), userID));

        // Create temporary ArrayList of friendIDs
        ArrayList<Long> friendIDs = new ArrayList<Long>(cursor.getCount());

        while (cursor.moveToNext()) {
            friendIDs.add(cursor.getLong(cursor.getColumnIndex(DBHelper.FRIENDS_TABLE.KEY_FRIEND_ID)));
        }

        // Close cursor
        cursor.close();

        // Build selection query from friendIDs
        StringBuilder buildSelection = new StringBuilder((DBHelper.USERS_TABLE.KEY_ID.length() + 8) * friendIDs.size());
        for (Long id : friendIDs) {
            buildSelection.append(String.format(" OR %s=%d", DBHelper.USERS_TABLE.KEY_ID, id));
        }
        // Removing leading " OR ", (inclusive, exclusive)
        buildSelection.delete(0, 4);

        // Run database query (TableName, Columns to Select, Selection)
        cursor = db.query(DBHelper.USERS_TABLE.NAME,
                null,
                buildSelection.toString(), null, null, null, null);

        // Iterate through results
        User friend;
        while ((friend = User.fromCursor(cursor)) != null) {
            Log.d("tskLoadFriends", String.format("Adding %s to Friend List", friend.getUsername()));

            if (friends.add(friend)) {
                // NOTE: Must call this synchronously
                adapterToNotify.notifyItemInserted(friends.size() - 1);
            }
        }

        // Close cursor
        cursor.close();
    }

    //endregion


    //region Static Methods

    public static User fromCursor(Cursor cursor) {
        // Handle cursor moving
        if (cursor.moveToNext()) {
            String uname = (cursor.getString(cursor.getColumnIndex(DBHelper.USERS_TABLE.KEY_USERNAME)));
            String email = (cursor.getString(cursor.getColumnIndex(DBHelper.USERS_TABLE.KEY_EMAIL)));
            Long id = (cursor.getLong(cursor.getColumnIndex(DBHelper.USERS_TABLE.KEY_ID)));
            return new User(id, uname, email, false);
        }
        return null;
    }


    public static class LoginResult {

        //region Declaration
        private User user;
        private Result result;

        public enum Result {
            SUCCESS, INVALID_INPUT, WRONG, UNKNOWN
        }
        //endregion

        public LoginResult(Result result) {
            this.user = null;
            this.result = result;
        }

        public LoginResult(Result result, User user) {
            this.user = user;
            this.result = result;
        }

        public Result getResult() {
            return this.result;
        }

        public User getLoggedInUser() {
            return this.user;
        }

    }

    public static LoginResult login(String userEmail, String password) {
        String loginType;
        User loginUser;

        if (userEmail.contains("@")) {
            if (!Utility.validateEmail(userEmail))
                return new LoginResult(LoginResult.Result.INVALID_INPUT);

            loginType = DBHelper.USERS_TABLE.KEY_EMAIL;
        } else {
            if (!Utility.validateUsername(userEmail))
                return new LoginResult(LoginResult.Result.INVALID_INPUT);

            loginType = DBHelper.USERS_TABLE.KEY_USERNAME;
        }

        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();

        String query = String.format("SELECT * FROM %s WHERE %s=? AND %s=?",
                DBHelper.USERS_TABLE.NAME,
                loginType,
                DBHelper.USERS_TABLE.KEY_PASSWORD);

        // Remember to clean SQL
        Cursor cursor = db.rawQuery(query,
                new String[]{userEmail, password});

        if (cursor.getCount() < 1)
            return new LoginResult(LoginResult.Result.WRONG);

        loginUser = User.fromCursor(cursor);

        // Close cursor
        cursor.close();
        return new LoginResult(
                loginUser == null ? LoginResult.Result.INVALID_INPUT : LoginResult.Result.SUCCESS,
                loginUser);
    }


    public static enum RegisterResult {
        SUCCESS, EMAIL_EXISTS, USERNAME_EXISTS, UNKNOWN
    }

    public static RegisterResult register(String username, String email, String password) {
        // Create SQL entries
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);

        // Retrieve database
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        try {
            db.insert(DBHelper.USERS_TABLE.NAME, null, values);
        }
        catch (Exception e) {
            Log.e("Registration", "Error occurred with database.", e);
            return RegisterResult.UNKNOWN;
        }

        return RegisterResult.SUCCESS;
    }

    //endregion

}
