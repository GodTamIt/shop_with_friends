package com.theratio.shopwithfriends;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.theratio.utilities.DBHelper;
import com.theratio.ShopWithFriends;
import com.theratio.utilities.User;
import com.theratio.utilities.Utility;


public class AddFriendActivity extends ActionBarActivity {

    //region Declarations
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
    //endregion


    //region Overridden Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show content
        setContentView(R.layout.activity_add_friend);

        // Assign UI components
        btnAddFriend = (Button) findViewById(R.id.add_friend_btnAddFriend);
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

    //endregion


    //region UI

    public void onbtnAddFriendClick(View view) {
        btnAddFriend.setEnabled(false);
        AsyncTask<Object, Object, User.AddFriendResult> tskAddFriend = new AsyncTask<Object, Object, User.AddFriendResult>() {
            @Override
            protected User.AddFriendResult doInBackground(Object... params) {
                return ShopWithFriends.getCurrentUser().addFriend(txtUserEmail.getText().toString(), txtUsername.getText().toString());
            }

            @Override
            protected void onPostExecute(User.AddFriendResult params) {
                onAddFriendComplete(params);
            }
        };

        tskAddFriend.execute();
    }

    //endregion


    //region Functioning

    private void onAddFriendComplete(User.AddFriendResult result) {
        if (result == User.AddFriendResult.SUCCESS) {
            Utility.showDialog(this, getResources().getString(R.string.added_friend_activity_add_friend), null);
            // Clear fields
            txtUsername.setText(null);
            txtUserEmail.setText(null);

            // Focus on txtUsername
            txtUsername.requestFocus();
        }
        else if (result == User.AddFriendResult.ALREADY_FRIENDS) {
            // Already friends
        }
        else if (result == User.AddFriendResult.NO_SUCH_USER) {
            Utility.showDialog(this, getResources().getString(R.string.failed_friend_activity_add_friend), null);
        }
        else {
            // Unknown error
        }

        btnAddFriend.setEnabled(true);
    }

    //endregion
}
