package com.theratio.shopwithfriends;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theratio.ShopWithFriends;
import com.theratio.utilities.User;

import java.util.Collections;
import java.util.List;


public class FriendProfileActivity extends ActionBarActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        Bundle bundle = getIntent().getExtras();

        user = (User) getIntent().getSerializableExtra("user");

        TextView usernameText =(TextView)findViewById(R.id.username);
        TextView emailText = (TextView)findViewById(R.id.email);
        TextView ratingText = (TextView)findViewById(R.id.rating);
        TextView salesReportsNumText = (TextView)findViewById(R.id.sales_reports_num);

        setTitle(user.getUsername());

        usernameText.append(": " + user.getUsername());
        emailText.append(": " + user.getEmail());
        ratingText.append(": 0");
        salesReportsNumText.append(": 0");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
