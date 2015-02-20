package com.theratio.shopwithfriends;

import android.content.Context;
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

import models.DBHelper;
import models.Utility;

public class LoginActivity extends ActionBarActivity {

    private Button btnSignIn;
    private EditText txtUserEmail;
    private EditText txtPassword;

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

    public void btnSignIn_Clicked(View view) {
        AsyncTask<Context, Object, Long> tskCheckLogin = new AsyncTask<Context, Object, Long>() {
            @Override
            protected Long doInBackground(Context... params) {
                String userEmail = txtUserEmail.getText().toString();
                String loginType;

                if (userEmail.contains("@")) {
                    if (!Utility.validateEmail(userEmail)) return -1L;
                    loginType = DBHelper.USERS_TABLE.KEY_EMAIL;
                } else {
                    if (!Utility.validateUsername(userEmail)) return -1L;
                    loginType = DBHelper.USERS_TABLE.KEY_USERNAME;
                }

                SQLiteDatabase db = DBHelper.getInstance(params[0]).getReadableDatabase();

                String query = String.format("SELECT %s FROM %s WHERE %s=? AND %s=?",
                        DBHelper.USERS_TABLE.KEY_ID,
                        DBHelper.USERS_TABLE.NAME,
                        loginType,
                        DBHelper.USERS_TABLE.KEY_PASSWORD);

                // Remember to clean SQL
                Cursor cursor = db.rawQuery(query,
                        new String[] {userEmail, txtPassword.getText().toString()});

                if (cursor != null && cursor.getCount() > 0)
                {
                    cursor.moveToFirst();
                    return cursor.getLong(cursor.getColumnIndex(DBHelper.USERS_TABLE.KEY_ID));
                }
                else
                    return -2L;
            }

            @Override
            protected void onPostExecute(Long params) {
                if (params != null) {
                    showHomeActivity(params);
                }
            }
        };
        tskCheckLogin.execute(this);
    }

    private void showHomeActivity(long userID) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("USER_ID", userID);
        startActivity(intent);
    }

    private void showLoginError(long error) {
        if (error == -1) {
            Utility.showDialog(this, getResources().getString(R.string.invalid_input_activity_login));
        }
        else if (error == -2) {
            Utility.showDialog(this, getResources().getString(R.string.failed_login_activity_login));

        }
    }

}

