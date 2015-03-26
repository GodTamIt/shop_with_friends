package com.theratio.utilities;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher Tam on 3/3/2015.
 */
public class Post implements Parcelable {

    //region Declarations
    private long postID;
    private long userID;
    private TYPE postType;
    private String itemName;
    private float worstPrice;
    private float autoPrice;
    private String description;
    private String location = "ur mum";//fix it later
    private BitmapDrawable picture;


    public static enum TYPE {
        INTEREST(0),
        REPORT(1);

        private int value;

        private TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static TYPE fromValue(int value) {
            switch (value) {
                case 0:
                    return INTEREST;
                case 1:
                    return REPORT;
                default:
                    return INTEREST;
            }
        }

    }
    //endregion


    //region Constructors

    private Post(Parcel in) {
        this.postID = in.readLong();
        this.postType = TYPE.fromValue(in.readInt());
        this.userID = in.readLong();
        this.itemName = in.readString();
        this.worstPrice = in.readFloat();
        this.autoPrice = in.readFloat();
        this.description = in.readString();
        this.location  = in.readString();
    }

    private Post(long postID, long userID, TYPE postType, String itemName, float worstPrice, float autoPrice, String description) {
        this.postID = postID;
        this.userID = userID;
        this.postType = postType;
        this.itemName = itemName;
        this.worstPrice = worstPrice;
        this.autoPrice = autoPrice;
        this.description = description;
        this.location = "ur mum";//retrieve it from the db later
    }

    //endregion


    //region Encapsulation Methods

    public long getUserID() {
        return userID;
    }

    public TYPE getPostType() {
        return postType;
    }

    public void setPostType(TYPE postType) {
        this.postType = postType;
    }

    public long getPostID() {
        return postID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getWorstPrice() {
        return worstPrice;
    }

    public void setWorstPrice(float worstPrice) {
        this.worstPrice = worstPrice;
    }

    public float getAutoPrice() {
        return autoPrice;
    }

    public void setAutoPrice(float autoPrice) {
        this.autoPrice = autoPrice;
    }

    public String getLocation() {
        return this.postType == TYPE.INTEREST ? location : null;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public BitmapDrawable getPicture() {return picture; }

    public void setDescription(String description) {
        this.description = description;
    }

    //endregion


    //region Overridden Methods

    @Override
    public int hashCode() {
        return (int) (this.postID % Integer.MAX_VALUE);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Post) {
            Post other = (Post) obj;

            // TODO: Eventually implement checking for the update status
            return (this.postID == other.postID);
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userID);
        dest.writeInt(this.postType.getValue());
        dest.writeLong(this.postID);
        dest.writeString(this.itemName);
        dest.writeFloat(this.worstPrice);
        dest.writeFloat(this.autoPrice);
        dest.writeString(this.description);
        dest.writeString(this.location);
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }

    };

    //endregion


    //region Static Methods

    /**
     * A class containing whether a create post request is successful,
     * and if successful, the resulting Post.
     */
    public static class CreatePostResult {

        //region Declaration
        private Post post;
        private Result result;

        /**
         * An enumeration representing whether a create post request is successful.
         */
        public static enum Result {
            SUCCESS, INVALID_INPUT, NO_PERMISSION, UNKNOWN
        }
        //endregion

        private CreatePostResult(Result result) {
            this.post = null;
            this.result = result;
        }

        private CreatePostResult(Result result, Post post) {
            this.post = post;
            this.result = result;
        }

        /**
         * Retrieves the result of the create post request.
         * @return a <code>CreatePostResult.Result</code> enumeration value representing
         * the success value.
         */
        public Result getResult() {
            return this.result;
        }

        /**
         * Retrieves the post if the create post request is successful.
         * @return a <code>Post</code> object representing the new post.
         */
        public Post getPost() {
            return this.post;
        }

    }

    public static CreatePostResult createPost(long userID, TYPE postType, String itemName,
                                              float worstPrice, String description) {
        return Post.createPost(userID, postType, itemName, worstPrice, 0.0F, description);
    }

    public static CreatePostResult createPost(long userID, TYPE postType, String itemName,
                                              float worstPrice, float autoPrice, String description) {
        // Create SQL entries
        ContentValues values = new ContentValues();
        values.put(DB.POSTS_TABLE.KEY_USER_ID, userID);
        values.put(DB.POSTS_TABLE.KEY_POST_TYPE, postType.getValue());
        values.put(DB.POSTS_TABLE.KEY_ITEM_NAME, itemName);
        values.put(DB.POSTS_TABLE.KEY_WORST_PRICE, worstPrice);
        values.put(DB.POSTS_TABLE.KEY_AUTO_PRICE, autoPrice);
        values.put(DB.POSTS_TABLE.KEY_DESCRIPTION, description);

        // Retrieve database
        SQLiteDatabase db = DB.getInstance().getWritableDatabase();

        long dbInsert = db.insert(DB.POSTS_TABLE.NAME, null, values);

        if (dbInsert < 0) {
            return new CreatePostResult(CreatePostResult.Result.UNKNOWN);
        }

        Post post = new Post(dbInsert, userID, postType, itemName, worstPrice, autoPrice, description);

        return new CreatePostResult(CreatePostResult.Result.SUCCESS, post);
    }

    /**
     * Synchronously retrieves posts made by a given user ID.
     * @param userID the user ID to filter posts by.
     * @param results the <code>List</code> to add posts to. If <code>null</code>,
     *                a new <code>List</code> will be initialized.
     * @return a <code>List</code> containing the posts by the given user ID.
     */
    public static List<Post> getPostsByUserID(long userID, List<Post> results) {
        SQLiteDatabase db = DB.getInstance().getReadableDatabase();

        // Get all posts of given user
        String query = String.format("SELECT * FROM %s WHERE %s=%d",
                DB.POSTS_TABLE.NAME,
                DB.POSTS_TABLE.KEY_USER_ID,
                userID);

        Cursor cursor = db.rawQuery(query, null);

        if (results == null)
            results = new ArrayList<Post>(cursor.getCount());


        if (cursor.getCount() < 1)
            return results;

        Log.d("updatePostsByUserID",
                String.format("Loaded %d posts made by %s", cursor.getCount(), userID));


        // Iterate through results
        Post post;
        while ((post = Post.fromCursor(cursor)) != null) {
            results.add(post);
        }

        // Close cursor
        cursor.close();


        return results;
    }

    /**
     * Asynchronously retrieves posts by a given user ID and notifies a RecyclerView adapter.
     * @param userID the user ID to filter posts by.
     * @param results the <code>List</code> to add posts to. If <code>null</code>,
     *                an <code>IllegalArgumentException</code> will be thrown.
     * @param adapterToNotify the <code>RecyclerView.Adapter</code> to notify.
     */
    public static void updatePostsByUserID(final long userID, final List<Post> results,
                                           final RecyclerView.Adapter adapterToNotify, final boolean clearList) {
        if (results == null)
            throw new IllegalArgumentException("Results list cannot be null.");

        AsyncTask<Object, Object, Object> tskPosts = new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                // if clear list
                if (clearList) {
                    results.clear();
                    // Typically bad practice, but we're clearing here
                    adapterToNotify.notifyDataSetChanged();
                }

                SQLiteDatabase db = DB.getInstance().getReadableDatabase();

                // Get all posts of given user
                String query = String.format("SELECT * FROM %s WHERE %s=%s",
                        DB.POSTS_TABLE.NAME,
                        DB.POSTS_TABLE.KEY_USER_ID,
                        userID);

                Cursor cursor = db.rawQuery(query, null);


                if (cursor.getCount() < 1)
                    return null;

                Log.d("updatePostsByUserID",
                        String.format("Loaded %d posts made by %s", cursor.getCount(), userID));


                // Iterate through results
                Post post;
                while ((post = Post.fromCursor(cursor)) != null) {
                    if (results.add(post) && (adapterToNotify != null)) {
                        // NOTE: Must call this synchronously
                        adapterToNotify.notifyItemInserted(results.size() - 1);
                        Log.d("updatePostsByUserID", String.format("Adding %s to Post List", post.getItemName()));
                    }
                }

                // Close cursor
                cursor.close();

                return null;
            }
        };

        tskPosts.execute();
    }

    private static Post fromCursor(Cursor cursor) {
        // Handle cursor moving
        if (cursor.moveToNext()) {
            long postID = cursor.getLong(cursor.getColumnIndex(DB.POSTS_TABLE.KEY_POST_ID));
            long userID = cursor.getLong(cursor.getColumnIndex(DB.POSTS_TABLE.KEY_USER_ID));
            TYPE postType = Post.TYPE.fromValue(cursor.getInt(cursor.getColumnIndex(DB.POSTS_TABLE.KEY_POST_TYPE)));
            String itemName = cursor.getString(cursor.getColumnIndex(DB.POSTS_TABLE.KEY_ITEM_NAME));
            float worstPrice = cursor.getFloat(cursor.getColumnIndex(DB.POSTS_TABLE.KEY_WORST_PRICE));
            float autoPrice = cursor.getFloat(cursor.getColumnIndex(DB.POSTS_TABLE.KEY_AUTO_PRICE));
            String description = cursor.getString(cursor.getColumnIndex(DB.POSTS_TABLE.KEY_DESCRIPTION));

            return new Post(postID, userID, postType, itemName, worstPrice, autoPrice, description);
        }
        return null;
    }

    /**
     * Synchronously retrieves all posts.
     * @param filterType the type of posts to retrieve.
     *                   If <code>null</code>, all posts will be retrieved.
     * @return a <code>List</code> object containing the posts.
     */
    public static List<Post> getAllPosts(TYPE filterType) {
        SQLiteDatabase db = DB.getInstance().getReadableDatabase();

        // Get all posts
        String query = String.format("SELECT * FROM %s", DB.POSTS_TABLE.NAME);

        if (filterType != null) {
            query += String.format(" WHERE %s=%d",
                    DB.POSTS_TABLE.KEY_POST_TYPE,
                    filterType.getValue());
        }

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Post> results = new ArrayList<Post>(cursor.getCount());

        if (cursor.getCount() < 1)
            return results;

        Log.d("getAllPosts",
                String.format("Loaded %d posts", cursor.getCount()));


        // Iterate through results
        Post post;
        while ((post = Post.fromCursor(cursor)) != null) {
            results.add(post);
        }

        // Close cursor
        cursor.close();


        return results;
    }

    /**
     * Asynchronously retrieves all posts.
     * @param results the <code>List</code> object to add results to.
     * @param filterType the type of posts to retrieve.
     *                   If <code>null</code>, all posts will be retrieved.
     * @param adapterToNotify the <code>RecyclerView.Adapter</code> to notify.
     * @param clearList the value determining whether to clear <code>results</code> before
     *                  retrieving posts.
     */
    public static void getAllPosts(final List<Post> results, final TYPE filterType,
                                   final RecyclerView.Adapter adapterToNotify, final boolean clearList) {

        if (results == null)
            throw new IllegalArgumentException("Results list cannot be null.");

        AsyncTask<Object, Object, Object> tskGetPosts = new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                if (clearList) {
                    results.clear();
                }

                SQLiteDatabase db = DB.getInstance().getReadableDatabase();

                // Get all posts
                String query = String.format("SELECT * FROM %s", DB.POSTS_TABLE.NAME);

                if (filterType != null) {
                    query += String.format(" WHERE %s=%d",
                            DB.POSTS_TABLE.KEY_POST_TYPE,
                            filterType.getValue());
                }

                Cursor cursor = db.rawQuery(query, null);

                if (cursor.getCount() < 1)
                    return results;

                Log.d("getAllPosts",
                        String.format("Loaded %d posts", cursor.getCount()));


                // Iterate through results
                Post post;
                while ((post = Post.fromCursor(cursor)) != null) {
                    if (adapterToNotify != null && results.add(post)) {
                        adapterToNotify.notifyItemInserted(results.size() - 1);
                    }
                }

                // Close cursor
                cursor.close();

                // Don't care about return
                return null;
            }
        };

        tskGetPosts.execute();
    }

    //endregion
    
}
