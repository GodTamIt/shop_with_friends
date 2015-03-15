package com.theratio.shopwithfriends;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.theratio.utilities.Post;
import com.theratio.utilities.User;


public class ViewPostActivity extends ActionBarActivity {

    private User user;
    private Post post;
    private String itemName;
    private float thresholdPrice;
    private String itemDescription;
    private String postedBy;
    private Post.TYPE itemType;
    private String itemLocation;//used for reports, hardcoded for now

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        Bundle bundle = getIntent().getExtras();

        post = (Post) getIntent().getParcelableExtra("post");
        user = User.getUser(post.getUserID());
        itemName = post.getItemName();
        thresholdPrice = post.getWorstPrice();
        itemDescription = post.getDescription();
        itemType = post.getPostType();
        itemLocation = post.getLocation();
        postedBy = user.getUsername();

        TextView itemNameText =(TextView)findViewById(R.id.item_name);
        TextView thresholdPriceText = (TextView)findViewById(R.id.threshold_price);
        TextView postedByText = (TextView)findViewById(R.id.posted_by);
        TextView itemDescriptionText = (TextView)findViewById(R.id.item_description);
        TextView itemLocationText = (TextView)findViewById(R.id.location);

        if (itemType == Post.TYPE.REPORT) {
            itemLocationText.append(": " + itemLocation);
            postedByText.append(": " + postedBy);
        }   else {
            itemLocationText.setText("");
            postedByText.setText("");
        }
        setTitle(itemName);

        itemNameText.append(": " + itemName);
        thresholdPriceText.append(": " + Float.toString(thresholdPrice));
        itemDescriptionText.append(": " + itemDescription);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
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
