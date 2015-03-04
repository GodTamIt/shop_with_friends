package com.theratio.utilities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.theratio.ShopWithFriends;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores data about User.
 */
public class User implements Parcelable {

    //region Declarations


    // Friends should eventually be stored in DiskCache
    private List<User> friends;
    private long id;
    private String userName;
    private String email;
    private Boolean isAdmin;
    private long rating;
    private long salesReportsNum;
    private BitmapDrawable profilePicture;
    //endregion


    //region Constructors

    /**
     * Builds a new instance of a user from a given Parcel.
     * @param in the Parcel in which the User class's data is stored.
     */
    private User(Parcel in) {
        friends = new ArrayList<User>();
        in.readList(friends, List.class.getClassLoader());

        this.id = in.readLong();
        this.userName = in.readString();
        this.email = in.readString();
        this.isAdmin = in.readInt() == 1;
        this.rating = in.readLong();
        this.salesReportsNum = in.readLong();

        Object tmp = in.readValue(Bitmap.class.getClassLoader());
        if (tmp != null && tmp instanceof Bitmap) {
            this.profilePicture = new BitmapDrawable(ShopWithFriends.getAppContext().getResources(),
                    (Bitmap) tmp);
        }

    }

    /**
     * Initializes a new user from the given parameters.  All other fields are defaulted to 0 or null.
     * @param id the ID of the user.
     * @param userName the display username of the user.
     * @param email the email of the user.
     * @param isAdmin specifies whether the user has administrator privileges.
     */
    public User(long id, String userName, String email, Boolean isAdmin) {
        this(id, userName, email, isAdmin, 0L, 0L, null);
    }

    /**
     *
     * @param id the ID of the user.
     * @param userName the display username of the user.
     * @param email the email of the user.
     * @param isAdmin specifies whether the user has administrator privileges.
     * @param rating the rating of the user.
     * @param salesReportsNum the reported number of sales of the user.
     * @param profilePicture a <code>BitmapDrawable</code> profile picture of the user.  If null, the default picture will be used.
     */
    public User(long id, String userName, String email, Boolean isAdmin, long rating, long salesReportsNum, BitmapDrawable profilePicture) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.rating = rating;
        this.salesReportsNum = salesReportsNum;
        this.setProfilePicture(profilePicture);

        // Initialize friends
        friends = new ArrayList<>();
    }

    //endregion


    //region Encapsulation Methods

    /**
     * Retrieves the ID of the current user.
     * @return a <code>long</code> value representing the user's ID.
     */
    public long getID() { return id; }

    /**
     * Gets the registered email address of the current user.
     * @return a <code>String</code> representing the user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the registered email address of the current user.
     * @param email the <code>String</code> representing the user's new email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the display username of the current user.
     * @return a <code>String</code> representing the user's username.
     */
    public String getUsername() {
        return this.userName;
    }

    /**
     * Sets the display username of the current user.
     * @param userName the <code>String</code> representing the user's new username.
     */
    public void setUsername(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the current user's profile picture.
     * @return a <code>BitmapDrawable</code> representation of the user's profile picture, or
     * <code>null</code> if the user does not have a profile picture set.
     */
    public BitmapDrawable getProfilePicture() { return this.profilePicture; }

    /**
     * Sets the current user's profile picture.
     * @param picture a <code>BitmapDrawable</code> representation of the user's new profile picture, or
     *                <code>null</code> to delete current picture.
     */
    public void setProfilePicture(BitmapDrawable picture) {
        this.profilePicture = picture;
    }

    /**
     * Gets the current user's rating.
     * @return a <code>long</code> value representing the user's rating.
     */
    public long getRating() {
        return this.rating;
    }

    /**
     * Sets the current user's rating.
     * @param rating a <code>long</code> value representing the user's new rating.
     */
    public void setRating(long rating) {
        this.rating = rating;
    }

    /**
     * Gets the number of sales reports on the current user.
     * @return the <code>long</code> value representing the user's number of sales reports.
     */
    public long getSalesReportsNum() {
        return this.salesReportsNum;
    }

    /**
     * Sets the current user's number of sales reports.
     * @param salesReportsNum the <code>long</code> value representing the user's new number of sales reports.
     */
    public void setSalesReportsNum(long salesReportsNum) {
        this.salesReportsNum = salesReportsNum;
    }

    //endregion


    //region Overridden Methods

    @Override
    public int hashCode() {
        return (int) (id % Integer.MAX_VALUE);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            // Eventually check for the update status of the user
            return this.getID() == ((User) obj).getID();
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(friends);

        dest.writeLong(id);
        dest.writeString(userName);
        dest.writeString(email);
        dest.writeInt(isAdmin ? 1 : 0);
        dest.writeLong(getRating());
        dest.writeLong(getSalesReportsNum());
        if (profilePicture != null) {
            dest.writeValue(profilePicture.getBitmap());
        }
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    //endregion


    //region Instance Methods

    /**
     * An enumeration representing the result of an add friend operation.
     */
    public static enum AddFriendResult {
        SUCCESS, ALREADY_FRIENDS, NO_SUCH_USER, UNKNOWN
    }

    /**
     * Adds a user to the current user's friends.
     * @param friendEmail the registered email address of the given friend.
     * @param friendUsername the displayed username of the given friend.
     * @return an <code>AddFriendResult</code> containing the result of the add operation.
     */
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

    /**
     * Removes a user from the current user's friends.
     * @param friend a <code>User</code> object representing the friend to remove.
     */
    public void removeFriend(User friend) {
        friends.remove(friend);

        this.removeFriendFromDatabase(friend.getID());
    }

    /**
     * Removes a user from the current user's friends.
     * @param userID a <code>long</code> value representing the ID of the friend to remove.
     */
    public void removeFriend(long userID) {
        this.removeFriend(new User(userID, null, null, null));
    }

    private void removeFriendFromDatabase(final long id) {

        AsyncTask<Object, Object, Object> tskRemove = new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();

                db.delete(DBHelper.FRIENDS_TABLE.NAME,
                        String.format("%s=%d AND %s=%d",
                                DBHelper.FRIENDS_TABLE.KEY_ID, ShopWithFriends.getCurrentUser().getID(),
                                DBHelper.FRIENDS_TABLE.KEY_FRIEND_ID, id),
                        null);

                // Don't care about return
                return null;
            }
        };

        tskRemove.execute();
    }

    /**
     * Updates the friend list asynchronously.
     * @param adapterToNotify - the RecyclerView adapter to notify when a friend has been updated.
     */
    public void updateFriends(final RecyclerView.Adapter adapterToNotify) {

        AsyncTask<Object, Object, Object> tskUpdate = new AsyncTask<Object, Object, Object>() {

            @Override
            protected Object doInBackground(Object... params) {
                // Clear list of friends
                friends.clear();
                // Notify entire DataSet has changed (typically a bad practice, good here)
                if (adapterToNotify != null) {
                    adapterToNotify.notifyDataSetChanged();
                }

                SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();

                // Get all friends of current user
                String query = String.format("SELECT %s FROM %s WHERE %s=?",
                        DBHelper.FRIENDS_TABLE.KEY_FRIEND_ID,
                        DBHelper.FRIENDS_TABLE.NAME,
                        DBHelper.FRIENDS_TABLE.KEY_ID);

                String userID = Long.toString(getID());

                Log.d("User.updateFriends", "Loading friends for " + userID);

                Cursor cursor = db.rawQuery(query,
                        new String[]{ userID });

                if (cursor.getCount() < 1)
                    return null;

                Log.d("User.updateFriends", String.format("Loaded %d friend(s) for %s", cursor.getCount(), userID));

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
                cursor = db.query(DBHelper.USERS_TABLE.NAME, null,
                        buildSelection.toString(), null, null, null, null);

                // Iterate through results
                User friend;
                while ((friend = User.fromCursor(cursor)) != null) {
                    Log.d("User.updateFriends", String.format("Adding %s to Friend List", friend.getUsername()));

                    if (friends.add(friend) && (adapterToNotify != null)) {
                        // NOTE: Must call this synchronously
                        adapterToNotify.notifyItemInserted(friends.size() - 1);
                    }
                }

                // Close cursor
                cursor.close();
                // Don't care about return type
                return null;
            }
        };

        tskUpdate.execute();
    }

    /**
     * Gets the current user's list of friends.
     * @return the current User instance's list of friends.
     */
    public List<User> getFriends() {
        return friends;
    }

    //endregion


    //region Static Methods

    private static User fromCursor(Cursor cursor) {
        // Handle cursor moving
        if (cursor.moveToNext()) {
            String username = (cursor.getString(cursor.getColumnIndex(DBHelper.USERS_TABLE.KEY_USERNAME)));
            String email = (cursor.getString(cursor.getColumnIndex(DBHelper.USERS_TABLE.KEY_EMAIL)));
            Long id = (cursor.getLong(cursor.getColumnIndex(DBHelper.USERS_TABLE.KEY_ID)));
            return new User(id, username, email, false);
        }
        return null;
    }

    /**
     * A class containing whether a login request is successful,
     * and if successful, the resulting User.
     */
    public static class LoginResult {

        //region Declaration
        private User user;
        private Result result;

        /**
         * An enumeration representing whether a login request is successful.
         */
        public enum Result {
            SUCCESS, INVALID_INPUT, WRONG, UNKNOWN
        }
        //endregion

        private LoginResult(Result result) {
            this.user = null;
            this.result = result;
        }

        private LoginResult(Result result, User user) {
            this.user = user;
            this.result = result;
        }

        /**
         * Retrieves the result of the login request.
         * @return a <code>LoginResult</code> enumeration value representing the success value.
         */
        public Result getResult() {
            return this.result;
        }

        /**
         * Retrieves the logged in user if the login request is successful.
         * @return a <code>User</code> object representing the logged in user.
         */
        public User getLoggedInUser() {
            return this.user;
        }

    }

    /**
     * Attempts to login a user, given a username/email and a password combination.
     * @param userEmail the username or email to check.
     * @param password the password to check.
     * @return a <code>LoginResult</code> object with the login result.
     */
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


    /**
     * An enumeration representing whether a register request is successful.
     */
    public static enum RegisterResult {
        SUCCESS, EMAIL_EXISTS, USERNAME_EXISTS, UNKNOWN
    }

    /**
     * Attempts to register a new user with the given credentials.
     * @param username the new user's display username.
     * @param email the new user's registered email.
     * @param password the new user's password.
     * @return a <code>RegisterResult</code> object with the registration result.
     */
    public static RegisterResult register(String username, String email, String password) {
        // Create SQL entries
        ContentValues values = new ContentValues();
        values.put(DBHelper.USERS_TABLE.KEY_USERNAME, username);
        values.put(DBHelper.USERS_TABLE.KEY_EMAIL, email);
        values.put(DBHelper.USERS_TABLE.KEY_PASSWORD, password);

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

    
    /**
     * Retrieves a User from database, given user ID.
     * @param userID the ID of the user to retrieve.
     * @return a <code>User</code> object representing the user, or <code>null</code> if the
     * user retrieving failed.
     */
    public static User getUser(long userID) {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();

        String query = String.format("SELECT * FROM %s WHERE %s=%s",
                DBHelper.USERS_TABLE.NAME,
                DBHelper.USERS_TABLE.KEY_ID,
                userID);


        Log.d("User.getUser", "Retrieving user " + userID);

        // Make query
        Cursor cursor = db.rawQuery(query, null);

        // Return result
        return User.fromCursor(cursor);
    }

    //endregion

}
