package com.theratio.shopwithfriends;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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

    protected enum RegisterResult {
        SUCCESS, EMAIL_EXISTS, USERNAME_EXISTS, UNKNOWN
    }

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

    }


    public void onDestroy() {
        super.onDestroy();
    }


    public void btnSubmit_Clicked(View view) {
        AsyncTask<Context, Object, RegisterResult> tskRegister = new AsyncTask<Context, Object, RegisterResult>() {
            @Override
            protected RegisterResult doInBackground(Context... params) {
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
                    SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

                    db.insert(DBHelper.USERS_TABLE.NAME, null, values);
                } catch (Exception e) {
                    Log.e("Registration", "Error occurred with database.", e);
                    return RegisterResult.UNKNOWN;
                }

                return RegisterResult.SUCCESS;
            }

            @Override
            protected void onPostExecute(RegisterResult params) {
                finishRegistration(params);
            }
        };

        tskRegister.execute(this);
    }


    private void finishRegistration(RegisterResult result) {

        if (result == RegisterResult.SUCCESS) {
            Utility.showDialog(this, getResources().getString(R.string.successful_activity_register), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showLogin();
                }
            });
        }
        else if (result == RegisterResult.EMAIL_EXISTS) {
            // Email already taken
        }
        else if (result == RegisterResult.USERNAME_EXISTS) {
            // Username already taken
        }
        else {
            // Unknown error
            Utility.showDialog(this, getResources().getString(R.string.unknown_error), null);
        }
    }

    private void showLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}