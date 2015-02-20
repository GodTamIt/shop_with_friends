package com.theratio;

import android.app.Application;

import com.theratio.utilities.User;

/**
 * Created by Christopher Tam on 2/20/2015.
 */
public class ShopWithFriends extends Application {

    // BEGIN: Declarations
    private User currentUser;
    // END: Declarations

    // BEGIN: Encapsulation Methods

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    // END: Encapsulation Methods


}
