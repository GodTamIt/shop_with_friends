package com.theratio;

import android.app.Application;
import android.content.Context;

import com.theratio.utilities.DB;
import com.theratio.utilities.User;

/**
 * Created by Christopher Tam on 2/20/2015.
 */
public class ShopWithFriends extends Application {

    //region Declarations
    private static User currentUser;
    private static Context context;
    //endregion


    //region Overridden Methods
    @Override
    public void onCreate() {
        ShopWithFriends.context = getApplicationContext();

        DB.initInstance(context);
    }
    //endregion


    //region Encapsulation Methods

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        ShopWithFriends.currentUser = currentUser;
    }

    public static Context getAppContext() {
        return ShopWithFriends.context;
    }


    //endregion


}
