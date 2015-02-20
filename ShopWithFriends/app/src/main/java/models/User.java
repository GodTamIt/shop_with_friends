package models;

import android.database.Cursor;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Stores data about User.
 */
public class User implements Serializable {


    private static final long serialVersionUID = -7060210544600464481L;

    private long id;
    private String userName;
    private String email;
    private Boolean isAdmin;
    private long rating;
    private long salesReportsNum;
    Drawable profilePicture;

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


    public long getID() { return id; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Drawable getProfilePicture() { return profilePicture; }

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
}
