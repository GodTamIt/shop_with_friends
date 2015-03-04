package com.theratio.utilities;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

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


    public static enum TYPE {
        BUY(0),
        SELL(1);

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
                    return BUY;
                case 1:
                    return SELL;
                default:
                    return BUY;
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
    }

    private Post(long postID, long userID, TYPE postType, String itemName, float worstPrice, float autoPrice, String description) {
        this.postID = postID;
        this.userID = userID;
        this.postType = postType;
        this.itemName = itemName;
        this.worstPrice = worstPrice;
        this.autoPrice = autoPrice;
        this.description = description;
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

    /*public void setPostID(long postID) {
        this.postID = postID;
    }*/

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

    public String getDescription() {
        return description;
    }

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
        return Post.createPost(userID, postType, itemName, worstPrice, 0.0F, "");
    }

    public static CreatePostResult createPost(long userID, TYPE postType, String itemName,
                                              float worstPrice, float autoPrice, String description) {
        // Create SQL entries
        ContentValues values = new ContentValues();
        values.put(DBHelper.POSTS_TABLE.KEY_USER_ID, userID);
        values.put(DBHelper.POSTS_TABLE.KEY_POST_TYPE, postType.getValue());
        values.put(DBHelper.POSTS_TABLE.KEY_ITEM_NAME, itemName);
        values.put(DBHelper.POSTS_TABLE.KEY_WORST_PRICE, worstPrice);
        values.put(DBHelper.POSTS_TABLE.KEY_AUTO_PRICE, autoPrice);
        values.put(DBHelper.POSTS_TABLE.KEY_DESCRIPTION, description);

        // Retrieve database
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        long dbInsert = db.insert(DBHelper.USERS_TABLE.NAME, null, values);

        if (dbInsert < 0) {
            return new CreatePostResult(CreatePostResult.Result.UNKNOWN);
        }

        Post post = new Post(dbInsert, userID, postType, itemName, worstPrice, autoPrice, description);

        return new CreatePostResult(CreatePostResult.Result.SUCCESS, post);
    }

    //endregion
    
}
