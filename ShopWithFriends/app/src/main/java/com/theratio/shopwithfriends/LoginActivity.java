package com.theratio.shopwithfriends;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.theratio.utilities.DBHelper;
import com.theratio.ShopWithFriends;
import com.theratio.utilities.User;
import com.theratio.utilities.Utility;

public class LoginActivity extends ActionBarActivity {

    private Button btnSignIn;
    private EditText txtUserEmail;
    private EditText txtPassword;

    private User loginUser;

    private enum LoginResult {
        SUCCESS, INVALID_INPUT, WRONG, UNKNOWN
    }

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
            boolean enabled = txtUserEmail.getText().length() != 0 && txtPassword.getText().length() != 0;

            // Set enabled
            btnSignIn.setEnabled(enabled);
        }
    };

    // BEGIN: Overridden Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call superclass method
        super.onCreate(savedInstanceState);

        // Show content
        setContentView(R.layout.activity_login);

        // Assign UI components
        btnSignIn = (Button) findViewById(R.id.login_btnSignIn);
        txtUserEmail = (EditText) findViewById(R.id.login_txtUserEmail);
        txtPassword = (EditText) findViewById(R.id.login_txtPassword);
        loginUser = null;

        // Add validator
        txtUserEmail.addTextChangedListener(textValidator);
        txtPassword.addTextChangedListener(textValidator);

        // Show 'up' icon
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {

        }

        return super.onOptionsItemSelected(item);
    }

    // END: Overridden Methods

    public void btnSignIn_Clicked(View view) {
        AsyncTask<User, Object, LoginResult> tskCheckLogin = new AsyncTask<User, Object, LoginResult>() {

            @Override
            protected LoginResult doInBackground(User... params) {
                String userEmail = txtUserEmail.getText().toString();
                String loginType;

                if (userEmail.contains("@")) {
                    if (!Utility.validateEmail(userEmail))
                        return LoginResult.INVALID_INPUT;

                    loginType = DBHelper.USERS_TABLE.KEY_EMAIL;
                } else {
                    if (!Utility.validateUsername(userEmail))
                        return LoginResult.INVALID_INPUT;

                    loginType = DBHelper.USERS_TABLE.KEY_USERNAME;
                }

                SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();

                String query = String.format("SELECT * FROM %s WHERE %s=? AND %s=?",
                        DBHelper.USERS_TABLE.NAME,
                        loginType,
                        DBHelper.USERS_TABLE.KEY_PASSWORD);

                // Remember to clean SQL
                Cursor cursor = db.rawQuery(query,
                        new String[]{userEmail, txtPassword.getText().toString()});

                if (cursor.getCount() < 1)
                    return LoginResult.WRONG;

                loginUser = User.fromCursor(cursor);

                // Close cursor
                cursor.close();
                return loginUser == null ? LoginResult.UNKNOWN : LoginResult.SUCCESS;
            }

            @Override
            protected void onPostExecute(LoginResult params) {
                onLoginFinished(params);
            }
        };
        tskCheckLogin.execute(loginUser);
    }

    private void onLoggedIn(User user) {

    }
    private void onLoginFinished(LoginResult result) {
        switch (result) {
            case SUCCESS:
                ShopWithFriends app = (ShopWithFriends) getApplicationContext();
                app.setCurrentUser(loginUser);
                Intent intent = new Intent(app, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case INVALID_INPUT:
                Utility.showDialog(this, getResources().getString(R.string.invalid_input_activity_login), null);
                break;
            case WRONG:
                Utility.showDialog(this, getResources().getString(R.string.failed_login_activity_login), null);
                break;
            case UNKNOWN:
                Utility.showDialog(this, getResources().getString(R.string.unknown_error), null);
                break;
        }
    }

    private void showLoginError(int error) {

    }

}

