package com.theratio.shopwithfriends;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.theratio.utilities.DBHelper;
import com.theratio.ShopWithFriends;
import com.theratio.utilities.User;


public class FriendListActivity extends Activity {

    protected RecyclerView mRecyclerView;
    protected FriendAdapter mAdapter;
    protected List<User> mFriends;
    protected User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call superclass constructor
        super.onCreate(savedInstanceState);

        // Show layout
        setContentView(R.layout.activity_friends_list);

        // Set RecyclerView layout
        mRecyclerView = (RecyclerView) findViewById(R.id.friends_list);
        mRecyclerView.setHasFixedSize(true);

        // Set layout of RecyclerView
        //LinearLayoutManager llm = new LinearLayoutManager(this);
        //llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Attach adapter to RecyclerView
        mAdapter = new FriendAdapter(this, getFriends());
        mRecyclerView.setAdapter(mAdapter);

        currentUser = ((ShopWithFriends) getApplicationContext()).getCurrentUser();
        Log.d("Home Activity", String.format("Displaying home for user %s", currentUser.getUserName()));
    }

    public List<User> getFriends() {
        // Give an empty list for friends
        mFriends = new ArrayList();

        AsyncTask<Context, Integer, Object> tskLoadFriends = new AsyncTask<Context, Integer, Object>() {
            @Override
            protected Integer doInBackground(Context... params) {
                SQLiteDatabase db = DBHelper.getInstance(params[0]).getReadableDatabase();

                // Get all friends of current user
                String query = String.format("SELECT %s FROM %s WHERE %s=?",
                        DBHelper.FRIENDS_TABLE.KEY_FRIEND_ID,
                        DBHelper.FRIENDS_TABLE.NAME,
                        DBHelper.FRIENDS_TABLE.KEY_ID);

                String userID = Long.toString(currentUser.getID());

                Log.d("tskLoadFriends", "Loading friends for " + userID);

                Cursor cursor = db.rawQuery(query,
                        new String[]{ userID });

                if (cursor.getCount() < 1)
                    return null;

                Log.d("tskLoadFriends", String.format("Loaded %d friend(s) for %s", cursor.getCount(), userID));

                // Create temporary ArrayList of friendIDs
                ArrayList<Long> friendIDs = new ArrayList<Long>(cursor.getCount());

                while (cursor.moveToNext()) {
                    friendIDs.add(cursor.getLong(cursor.getColumnIndex(DBHelper.FRIENDS_TABLE.KEY_FRIEND_ID)));
                }

                // Close cursor
                cursor.close();

                // Build selection query from friendIDs
                StringBuilder buildSelection = new StringBuilder((DBHelper.USERS_TABLE.KEY_ID.length() + 8) * friendIDs.size());
                for (Long id : friendIDs) {
                    buildSelection.append(String.format(" OR %s=%d", DBHelper.USERS_TABLE.KEY_ID, id));
                }
                // Removing leading " OR ", (inclusive, exclusive)
                buildSelection.delete(0, 4);

                // Run database query (TableName, Columns to Select, Selection)
                cursor = db.query(DBHelper.USERS_TABLE.NAME,
                        null,
                        buildSelection.toString(), null, null, null, null);

                // Iterate through results
                User friend;
                while ((friend = User.fromCursor(cursor)) != null) {
                    Log.d("tskLoadFriends", String.format("Adding %s to Friend List", friend.getUserName()));

                    if (mFriends.add(friend)) {
                        publishProgress(mFriends.size() - 1);
                    }
                }

                // Close cursor
                cursor.close();

                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... params) {
                mAdapter.notifyItemInserted(params[0]);
            }
        };

        // Execute task asynchronously
        tskLoadFriends.execute(this);

        // Return list of friends
        return mFriends;
    }
}

class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {

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

        Log.d("FriendAdapter", "Inflated new row");
        // Send inflated view to ViewHolder (handles recycling of views)
        FriendViewHolder holder = new FriendViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        // Get current user
        User current = friends.get(position);

        // Set text and image
        holder.lblFriend.setText(current.getUserName());

        Drawable pic = current.getProfilePicture();
        if (pic == null) {
            // Replace with default image
        }
        else {
            holder.imgProfile.setImageDrawable(current.getProfilePicture());
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}

class FriendViewHolder extends RecyclerView.ViewHolder {
    ImageView imgProfile;
    TextView lblFriend;
    public FriendViewHolder(View itemView) {
        // Call superclass constructor
        super(itemView);

        // Find text
        lblFriend = (TextView) itemView.findViewById(R.id.friend_list_label);
        imgProfile = (ImageView) itemView.findViewById(R.id.friend_list_icon);
    }
}


/*
public class ListViewLoader extends ListActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;

    // These are the Contacts rows that we will retrieve
    static final String[] PROJECTION = new String[] {ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME};

    // This is the select criteria
    static final String SELECTION = "((" +
            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);

        // For the cursor adapter, specify which columns go into which views
        String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
        int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null,
                fromColumns, toViews, 0);
        setListAdapter(mAdapter);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                PROJECTION, SELECTION, null, null);
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
}
*/