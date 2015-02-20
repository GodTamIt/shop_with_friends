package com.theratio.shopwithfriends;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.theratio.utilities.DBHelper;
import com.theratio.utilities.Utility;


public class RegisterActivity extends ActionBarActivity implements OnClickListener {
    // Variable Declaration should be in onCreate()
    private Button btnSubmit;
    private EditText txtUsername;
    private EditText txtPassword;
    private EditText txtConfPass;
    private EditText txtEmail;

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
                    Utility.validateUsername(txtUsername) &&
                    txtPassword.getText().length() != 0 &&
                    txtPassword.getText().toString().equals(txtConfPass.getText().toString()) &&
                    Utility.validateEmail(txtEmail);

            // Set enabled
            btnSubmit.setEnabled(enabled);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call superclass method
        super.onCreate(savedInstanceState);

        // Show content
        setContentView(R.layout.activity_register);

        //Assignment of UI fields to the variables
        btnSubmit = (Button) findViewById(R.id.register_btnSubmit);
        txtUsername = (EditText) findViewById(R.id.register_txtUsername);
        txtPassword = (EditText) findViewById(R.id.register_txtPassword);
        txtConfPass = (EditText) findViewById(R.id.register_txtConfPass);
        txtEmail = (EditText) findViewById(R.id.register_txtEmail);

        // Show 'up' icon
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Add validation listeners
        txtUsername.addTextChangedListener(textValidator);
        txtPassword.addTextChangedListener(textValidator);
        txtConfPass.addTextChangedListener(textValidator);
        txtEmail.addTextChangedListener(textValidator);
    }


    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    public void onDestroy() {
        super.onDestroy();
    }


    public void btnSubmit_Clicked(View view) {
        AsyncTask<Context, Object, Integer> tskRegister = new AsyncTask<Context, Object, Integer>() {
            @Override
            protected Integer doInBackground(Context... params) {
                String username = txtUsername.getText().toString();
                String email = txtEmail.getText().toString();
                String pass = txtPassword.getText().toString();

                // Create SQL entry
                ContentValues values = new ContentValues();
                values.put("username", username);
                values.put("password", pass);
                values.put("email", email);

                try {
                    // Get writable instance of database
                    SQLiteDatabase db = DBHelper.getInstance(params[0]).getWritableDatabase();

                    db.insert(DBHelper.USERS_TABLE.NAME, null, values);
                } catch (Exception e) {
                    Log.e("Registration", "Error occurred with database.", e);
                    return 1;
                }
                return 0;
            }

            @Override
            protected void onPostExecute(Integer params) {
                if (params == 0) {
                    finishRegistration();
                } else {
                    showError(params);
                }
            }
        };

        tskRegister.execute(this);
    }


    private void finishRegistration() {
        Utility.showDialog(this, getResources().getString(R.string.successful_activity_register));

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void showError(Integer errorCode) {

    }

}