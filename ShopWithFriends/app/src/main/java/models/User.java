package models;

import android.database.Cursor;

import java.io.Serializable;

/**
 * TODO: create header comment
 */
public class User implements Serializable {


    private static final long serialVersionUID = -7060210544600464481L;

    private long id;
    private String userName;
    private String email;
    private Boolean isAdmin;
    private int rating;
    private int salesReportsNum;

    public User(long id, String userName, String email,Boolean isAdmin) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.rating = 0;//placeholders
        this.salesReportsNum = 0;//placeholders yo
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

    public static User fromCursor(Cursor cursor) {
        cursor.moveToFirst();
        String uname = (cursor.getString(cursor.getColumnIndex(DBHelper.USERS_TABLE.KEY_USERNAME)));
        String email = (cursor.getString(cursor.getColumnIndex(DBHelper.USERS_TABLE.KEY_EMAIL)));
        Long id = (cursor.getLong(cursor.getColumnIndex(DBHelper.USERS_TABLE.KEY_ID)));
        return new User(id, uname, email, false);
    }
}
