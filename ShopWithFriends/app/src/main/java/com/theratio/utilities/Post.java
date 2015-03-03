package com.theratio.utilities;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Christopher Tam on 3/3/2015.
 */
public class Post implements Parcelable {

    //region Declarations
    private long userID;
    private TYPE postType;
    private long postID;
    private String itemName;
    private float worstPrice;
    private float autoPrice;
    private String description;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
    public static Post createPost(long userID, TYPE postType, String itemName, float worstPrice, String description) {


        return new Post(0, TYPE.BUY, 0, "testing");
    }

    public static enum TYPE {
        BUY, SELL
    }
    //endregion

    //region Constructors


    public Post(long userID, TYPE postType, long postID, String itemName) {
        this(userID, postType, postID, itemName, 0.0F, 0.0F,"");
    }

    public Post(long userID, TYPE postType, long postID, String itemName, float worstPrice, String description) {
        this(userID, postType, postID, itemName, worstPrice, 0.0F, description);
    }

    public Post(long userID, TYPE postType, long postID, String itemName, float worstPrice, float autoPrice, String description) {
        this.userID = userID;
        this.postType = postType;
        this.postID = postID;
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

    //endregion
    
}
