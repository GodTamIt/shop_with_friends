package com.theratio.shopwithfriends;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.theratio.ShopWithFriends;
import com.theratio.ui.CircularImageView;
import com.theratio.utilities.User;

import java.util.List;


public class ProfileActivity extends ActionBarActivity {

    //region Declarations
    private User user;
    private Button addRemoveBtn;
    private boolean isFriend;
    private Drawable mDefaultPic;
    //endregion


    //region Overridden Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Bundle bundle = getIntent().getExtras();

        user = (User) getIntent().getParcelableExtra("user");

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

        return super.onOptionsItemSelected(item);
    }

    //endregion


    //region UI

    public void onAddRemoveBtn(View view) {
        if (isFriend) {
            ShopWithFriends.getCurrentUser().removeFriend(user);
            addRemoveBtn.setText(this.getString(R.string.add_to_friends_button));
            isFriend = false;

        } else {
            ShopWithFriends.getCurrentUser().addFriend(user.getEmail(),user.getUsername());
            addRemoveBtn.setText(this.getString(R.string.remove_from_friends_button));
            isFriend = true;
        }
    }

    //endregion


    //region Functioning

    public boolean isFriend() {
       return ShopWithFriends.getCurrentUser().getFriends().contains(this.user);
    }

    //endregion



}
