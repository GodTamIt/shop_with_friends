package com.theratio.shopwithfriends;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.theratio.ShopWithFriends;
import com.theratio.utilities.Post;
import com.theratio.utilities.User;
import com.theratio.utilities.Utility;


public class PostItemActivity extends ActionBarActivity {

    private User curUser = ShopWithFriends.getCurrentUser();
    private EditText itemName;
    private EditText thresholdPrice;
    private EditText itemDescription;
    private Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);

        btnPost = (Button) findViewById(R.id.post_item_btn);
        itemDescription = (EditText) findViewById(R.id.item_description);
        itemName = (EditText) findViewById(R.id.item_name);
        thresholdPrice = (EditText) findViewById(R.id.threshold_price);

        itemName.addTextChangedListener(textValidator);
        thresholdPrice.addTextChangedListener(textValidator);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_item, menu);
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

    // Text Validator
    private TextWatcher textValidator = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Test if all fields are valid
            boolean enabled =
                    itemName.getText().length() >= 3 &&
                            thresholdPrice.getText().toString().length() > 0;

            // Set enabled
            btnPost.setEnabled(enabled);
        }
    };



    public void onbtnPostItem(View view) {

        Post.createPost(curUser.getID(), Post.TYPE.BUY, itemName.getText().toString(), Float.valueOf(thresholdPrice.getText().toString()), itemDescription.getText().toString());
        final Context current = this;
        Utility.showDialog(this, getResources().getString(R.string.successful_activity_post), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(current, HomeActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }
}