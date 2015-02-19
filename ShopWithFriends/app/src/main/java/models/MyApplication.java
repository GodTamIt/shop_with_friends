package models;

import android.app.Application;
import android.content.Context;

/**
 * Created by filoleg on 2/19/15.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}