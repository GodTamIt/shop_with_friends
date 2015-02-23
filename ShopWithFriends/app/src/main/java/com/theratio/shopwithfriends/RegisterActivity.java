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
import android.widget.Button;
import android.widget.EditText;

import com.theratio.utilities.DBHelper;
import com.theratio.utilities.User;
import com.theratio.utilities.Utility;


public class RegisterActivity extends ActionBarActivity {

    //region Declarations
    private Button btnRegister;
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
            btnRegister.setEnabled(enabled);
        }
    };

    //endregion

    //region Overridden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call superclass method
        super.onCreate(savedInstanceState);

        // Show content
        setContentView(R.layout.activity_register);

        //Assignment of UI fields to the variables
        btnRegister = (Button) findViewById(R.id.register_btnSubmit);
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


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //endregion

    //region UI

    public void onbtnRegisterClick(View view) {
        this.setAllEnabled(false);

        AsyncTask<String, Object, User.RegisterResult> tskRegister = new AsyncTask<String, Object, User.RegisterResult>() {
            @Override
            protected User.RegisterResult doInBackground(String... params) {
                return User.register(params[0], params[1], params[2]);
            }

            @Override
            protected void onPostExecute(User.RegisterResult params) {
                onRegisterComplete(params);
            }
        };

        tskRegister.execute(txtUsername.getText().toString(), txtEmail.getText().toString(), txtPassword.getText().toString());
    }

    private void setAllEnabled(boolean enabled) {
        txtUsername.setEnabled(enabled);
        txtPassword.setEnabled(enabled);
        txtConfPass.setEnabled(enabled);
        txtEmail.setEnabled(enabled);
        btnRegister.setEnabled(enabled);
    }

    //endregion

    //region Registration

    private void onRegisterComplete(User.RegisterResult result) {
        if (result == User.RegisterResult.SUCCESS) {
            final Context current = this;
            Utility.showDialog(this, getResources().getString(R.string.successful_activity_register), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(current, LoginActivity.class);
                    startActivity(intent);

                    finish();
                }
            });
        }
        else if (result == User.RegisterResult.EMAIL_EXISTS) {
            // Email already taken
        }
        else if (result == User.RegisterResult.USERNAME_EXISTS) {
            // Username already taken
        }
        else {
            // Unknown error
            Utility.showDialog(this, getResources().getString(R.string.unknown_error), null);
        }
        btnRegister.setEnabled(true);
    }

    //endregion
}