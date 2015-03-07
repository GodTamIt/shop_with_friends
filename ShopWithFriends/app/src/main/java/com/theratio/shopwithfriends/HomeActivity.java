package com.theratio.shopwithfriends;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theratio.ShopWithFriends;
import com.theratio.utilities.Post;
import com.theratio.utilities.User;

import java.util.Collections;
import java.util.List;


public class HomeActivity extends ActionBarActivity {

    private User currentUser;
    Drawable mDefaultPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Store default profile picture to memory (eventually implement Lru and Disk cache and replace it with something else)
        mDefaultPic = getResources().getDrawable(R.drawable.user_no_profile);

        currentUser = ShopWithFriends.getCurrentUser();
        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
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
            ShopWithFriends.setCurrentUser(null);

            // Show WelcomeActivity
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);

            // Do not allow returning to this activity
            finish();
        } else if (id == R.id.post_item) {
            // Post an item you are interested in
            Intent intent = new Intent(this, PostItemActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

        private LayoutInflater inflater;
        private List<Post> posts = Collections.emptyList();

        public PostAdapter(Context context, List<Post> posts) {
            inflater = LayoutInflater.from(context);
            this.posts = posts;
        }


        @Override
        public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Retrieve and inflate friend list row
            View view = inflater.inflate(R.layout.post_list_recycler_card, parent, false);

            // Send inflated view to ViewHolder (handles recycling of views)
            PostViewHolder holder = new PostViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(PostViewHolder holder, int position) {
            // Get current user
            Post current = posts.get(position);

            Log.d("Friend list size", Integer.toString(posts.size()));

            // Set text and image
            holder.lblItem.setText(current.getItemName());

            Drawable pic = current.getPicture();
            if (pic == null) {
                pic = mDefaultPic;
            }

            holder.imgProfile.setImageDrawable(pic);
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }

    }

    private class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView lblItem;
        public PostViewHolder(View itemView) {
            // Call superclass constructor
            super(itemView);

            // Find text
            lblItem = (TextView) itemView.findViewById(R.id.post_item_name);
            //imgProfile = (ImageView) itemView.findViewById(R.id.post_list_icon);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Post post = ShopWithFriends.getCurrentUser().getPosts().get(getPosition());
                    Intent postPage = new Intent(v.getContext(), PostActivity.class);
                    postPage.putExtra("post",(Parcelable) post);

                    startActivity(postPage);
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    Post post = ShopWithFriends.getCurrentUser().getPosts().get(getPosition());
                    Intent postPage = new Intent(v.getContext(), PostActivity.class);
                    postPage.putExtra("post",(Parcelable) post);

                    startActivity(postPage);
                    return true;

                }
            });
        }
    }
}
