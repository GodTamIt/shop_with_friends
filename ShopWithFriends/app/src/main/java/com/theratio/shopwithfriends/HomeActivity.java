package com.theratio.shopwithfriends;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.theratio.ShopWithFriends;
import com.theratio.utilities.DBHelper;
import com.theratio.utilities.User;


public class HomeActivity extends ActionBarActivity {

    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currentUser = ((ShopWithFriends) getApplicationContext()).getCurrentUser();
        Log.d("Home Activity", String.format("Displaying home for user %s", currentUser.getUserName()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_friend) {
            Intent intent = new Intent(this, AddFriendActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.friend_list) {
            Intent intent = new Intent(this, FriendListActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.logout) {
            // Clear current user session
            ((ShopWithFriends) getApplicationContext()).setCurrentUser(null);

            // Show WelcomeActivity
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);

            // Do not allow returning to this activity
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
