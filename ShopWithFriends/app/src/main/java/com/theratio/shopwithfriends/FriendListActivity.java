package com.theratio.shopwithfriends;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theratio.ShopWithFriends;
import com.theratio.utilities.User;

import java.util.Collections;
import java.util.List;


public class FriendListActivity extends ActionBarActivity {

    //region Declarations
    private RecyclerView mRecyclerView;
    private FriendAdapter mAdapter;
    // Cache for profile pictures
    Drawable mDefaultPic;
    //endregion


    //region Overridden Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call superclass constructor
        super.onCreate(savedInstanceState);

        // Show layout
        setContentView(R.layout.activity_friends_list);

        // Show 'up' icon
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Set RecyclerView layout
        mRecyclerView = (RecyclerView) findViewById(R.id.friends_list);
        mRecyclerView.setHasFixedSize(true);

        // Set layout of RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Store default profile picture to memory (eventually implement Lru and Disk cache)
        mDefaultPic = getResources().getDrawable(R.drawable.user_no_profile);

        // Attach adapter to RecyclerView
        mAdapter = new FriendAdapter(this, ShopWithFriends.getCurrentUser().getFriends());
        ShopWithFriends.getCurrentUser().updateFriends(mAdapter);
        mRecyclerView.setAdapter(mAdapter);
    }

    //endregion


    //region RecyclerView classes

    private class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {

        private LayoutInflater inflater;
        private List<User> friends = Collections.emptyList();

        public FriendAdapter(Context context, List<User> friends) {
            inflater = LayoutInflater.from(context);
            this.friends = friends;
        }


        @Override
        public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Retrieve and inflate friend list row
            View view = inflater.inflate(R.layout.friend_list_recycler_row, parent, false);

            // Send inflated view to ViewHolder (handles recycling of views)
            FriendViewHolder holder = new FriendViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(FriendViewHolder holder, int position) {
            // Get current user
            User current = friends.get(position);

            Log.d("Friend list size", Integer.toString(friends.size()));

            // Set text and image
            holder.lblFriend.setText(current.getUsername());

            Drawable pic = current.getProfilePicture();
            if (pic == null) {
                pic = mDefaultPic;
            }

            holder.imgProfile.setImageDrawable(pic);
        }

        @Override
        public int getItemCount() {
            return friends.size();
        }

    }

    private class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView lblFriend;
        public FriendViewHolder(View itemView) {
            // Call superclass constructor
            super(itemView);

            // Find text
            lblFriend = (TextView) itemView.findViewById(R.id.friend_list_label);
            imgProfile = (ImageView) itemView.findViewById(R.id.friend_list_icon);

            itemView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    User friend = ShopWithFriends.getCurrentUser().getFriends().get(getPosition());
                    Intent friendPage = new Intent(v.getContext(), ProfileActivity.class);
                    friendPage.putExtra("user",(Parcelable) friend);

                    startActivity(friendPage);
                }
            });


            itemView.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    User friend = ShopWithFriends.getCurrentUser().getFriends().get(getPosition());
                    Intent friendPage = new Intent(v.getContext(), ProfileActivity.class);
                    friendPage.putExtra("user", (Parcelable) friend);

                    startActivity(friendPage);
                    return true;

                }
            });
        }
    }

    //endregion

}

