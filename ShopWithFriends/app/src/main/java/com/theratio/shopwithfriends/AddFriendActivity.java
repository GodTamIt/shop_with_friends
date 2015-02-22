package com.theratio.shopwithfriends;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.theratio.utilities.DBHelper;
import com.theratio.ShopWithFriends;
import com.theratio.utilities.Utility;


public class AddFriendActivity extends ActionBarActivity {
    private Button btnAddFriend;
    private EditText txtUsername;
    private EditText txtUserEmail;

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
                    Utility.validateUsername(txtUsername) &&
                    Utility.validateEmail(txtUserEmail);

            // Set enabled
            btnAddFriend.setEnabled(enabled);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show content
        setContentView(R.layout.activity_add_friend);

        // Assign UI components
        btnAddFriend = (Button) findViewById(R.id.AddFriendButton);
        txtUsername = (EditText) findViewById(R.id.friendUsernameField);
        txtUserEmail = (EditText) findViewById(R.id.friendEmailField);

        // Attach validator
        txtUsername.addTextChangedListener(textValidator);
        txtUserEmail.addTextChangedListener(textValidator);

        // Show 'up' icon
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_friend, menu);
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

    public void addFriend(View view) {
        String userEmail = txtUserEmail.getText().toString();
        String userName = txtUsername.getText().toString();

        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();

        String query = String.format("SELECT %s FROM %s WHERE %s=? AND %s=?",
                DBHelper.USERS_TABLE.KEY_ID,
                DBHelper.USERS_TABLE.NAME,
                DBHelper.USERS_TABLE.KEY_USERNAME,
                DBHelper.USERS_TABLE.KEY_EMAIL);

        Cursor cursor = db.rawQuery(query, new String[] {userName, userEmail});


        if (cursor.moveToFirst()) {
            // User exists - get Friend's ID from
            cursor.moveToFirst();
            long friendID = cursor.getLong(cursor.getColumnIndex(DBHelper.USERS_TABLE.KEY_ID));

            // Close cursor
            cursor.close();

            db = DBHelper.getInstance().getWritableDatabase();


            // Create SQL entry
            ContentValues values = new ContentValues();
            values.put(DBHelper.FRIENDS_TABLE.KEY_ID, ((ShopWithFriends) getApplicationContext()).getCurrentUser().getID());
            values.put(DBHelper.FRIENDS_TABLE.KEY_FRIEND_ID, friendID);

            try
            {
                db.insert(DBHelper.FRIENDS_TABLE.NAME, null, values);

                Utility.showDialog(this, getResources().getString(R.string.added_friend_activity_add_friend), null);
                return;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        Utility.showDialog(this, getResources().getString(R.string.failed_friend_activity_add_friend), null);

    }

    public void cancelAddFriend(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }
}
