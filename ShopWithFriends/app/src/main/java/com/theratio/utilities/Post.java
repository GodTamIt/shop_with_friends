package com.theratio.utilities;


/**
 * Created by Christopher Tam on 3/3/2015.
 */
public class Post {

    //region Declarations
    private long id;
    private TYPE postType;
    private long itemID;
    private String itemName;
    private float worstPrice;
    private float autoPrice;

    public static enum TYPE {
        BUY, SELL
    }
    //endregion

    //region Constructors

    public Post(long id, TYPE postType, long itemID, String itemName) {
        this(id, postType, itemID, itemName, 0.0F, 0.0F);
    }

    public Post(long id, TYPE postType, long itemID, String itemName, float worstPrice, float autoPrice) {
        this.id = id;
        this.postType = postType;
        this.itemID = itemID;
        this.itemName = itemName;
        this.worstPrice = worstPrice;
        this.autoPrice = autoPrice;
    }

    //endregion

    //region Encapsulation Methods

    public long getId() {
        return id;
    }

    public TYPE getPostType() {
        return postType;
    }

    public void setPostType(TYPE postType) {
        this.postType = postType;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
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

    //endregion
    
}
