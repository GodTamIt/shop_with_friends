package com.theratio.shopwithfriends;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;

import com.theratio.ShopWithFriends;
import com.theratio.ui.CircularImageView;
import com.theratio.utilities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FriendProfileActivity extends ActionBarActivity {

    private User curUser;
    private User user;
    private Button addRemoveBtn;
    private boolean isFriend;
    Drawable mDefaultPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        Bundle bundle = getIntent().getExtras();

        user = (User) getIntent().getParcelableExtra("user");
        curUser = ShopWithFriends.getCurrentUser();

        Drawable pic = user.getProfilePicture();
        mDefaultPic = getResources().getDrawable(R.drawable.user_no_profile);
        if (pic == null) {
            pic = mDefaultPic;
        }

        isFriend = isFriend();


        ((CircularImageView)findViewById(R.id.profile_pic)).setImageDrawable(pic);
        TextView usernameText =(TextView)findViewById(R.id.username);
        TextView emailText = (TextView)findViewById(R.id.email);
        TextView ratingText = (TextView)findViewById(R.id.rating);
        TextView salesReportsNumText = (TextView)findViewById(R.id.sales_reports_num);
        addRemoveBtn = (Button)findViewById(R.id.profile_add_remove_button);

        if (isFriend) {
            addRemoveBtn.setText(this.getString(R.string.remove_from_friends_button));
        }



        setTitle(user.getUsername());

        usernameText.append(": " + user.getUsername());
        emailText.append(": " + user.getEmail());
        ratingText.append(": 0");
        salesReportsNumText.append(": 0");
    }

    //used to check if the user is in friends with our current user
    public boolean isFriend(){


        List<User> friendList = curUser.getFriends();

        for (User i:friendList) {
            if (user.equals(i)) {
                return true;
            }
        }

        return false;
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

    public void onAddRemoveBtn(View view) {
        if (isFriend) {
            curUser.removeFriend(user);
            addRemoveBtn.setText(this.getString(R.string.add_to_friends_button));
            isFriend = false;

        } else {
            curUser.addFriend(user.getEmail(),user.getUsername());
            addRemoveBtn.setText(this.getString(R.string.remove_from_friends_button));
            
            isFriend = true;
        }
    }
}
