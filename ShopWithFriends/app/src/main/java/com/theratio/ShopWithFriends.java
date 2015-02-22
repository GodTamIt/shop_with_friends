package com.theratio;

import android.app.Application;

import com.theratio.utilities.DBHelper;
import com.theratio.utilities.User;

/**
 * Created by Christopher Tam on 2/20/2015.
 */
public class ShopWithFriends extends Application {

    //region Declarations
    private User currentUser;
    //endregion

    //region Overridden Methods
    @Override
    public void onCreate() {
        DBHelper.initInstance(getApplicationContext());
    }
    //endregion

    //region Encapsulation Methods

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    //endregion


}
