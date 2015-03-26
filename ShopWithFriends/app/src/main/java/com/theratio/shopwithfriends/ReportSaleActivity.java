package com.theratio.shopwithfriends;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.larvalabs.svgandroid.SVGParser;
import com.theratio.ShopWithFriends;
import com.theratio.utilities.Post;
import com.theratio.utilities.Utility;


public class ReportSaleActivity extends ActionBarActivity {

    private EditText itemName;
    private EditText thresholdPrice;
    private EditText itemDescription;
    private EditText location;
    private Button btnPost;
    private ImageButton btnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sale);

        btnPost = (Button) findViewById(R.id.report_sale_btn);
        btnLocation = (ImageButton) findViewById(R.id.report_sale_btnLocation);
        itemDescription = (EditText) findViewById(R.id.report_sale_item_description);
        itemName = (EditText) findViewById(R.id.report_sale_item_name);
        thresholdPrice = (EditText) findViewById(R.id.report_sale_threshold_price);
        location = (EditText) findViewById(R.id.report_sale_location);


        itemName.addTextChangedListener(textValidator);
        thresholdPrice.addTextChangedListener(textValidator);
        location.addTextChangedListener(textValidator);

        btnLocation.setImageResource(R.mipmap.location);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_sale, menu);
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
                    thresholdPrice.getText().toString().length() > 0 &&
                    location.getText().toString().length() >= 3;//possibly integrate google maps later on

            // Set enabled
            btnPost.setEnabled(enabled);
        }
    };



    public void onbtnReportSale(View view) {
        // TODO: Check if post was successful
        Post.createPost(ShopWithFriends.getCurrentUser().getID(), Post.TYPE.REPORT, itemName.getText().toString(), Float.valueOf(thresholdPrice.getText().toString()), itemDescription.getText().toString());
        final Context current = this;
        Utility.showDialog(this, getResources().getString(R.string.successful_report_activity_report_sale), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(current, HomeActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    public void onbtnLocation(View view) {
        Intent i = new Intent(this, PickLocationActivity.class);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                location.setText(data.getStringExtra("Location"));
            }
        }
    }
}
