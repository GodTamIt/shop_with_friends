package com.theratio.shopwithfriends;

import android.content.Intent;
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

import com.theratio.ShopWithFriends;
import com.theratio.utilities.User;
import com.theratio.utilities.Utility;


public class LoginActivity extends ActionBarActivity {

    //region Declarations
    private Button btnSignIn;
    private EditText txtUserEmail;
    private EditText txtPassword;

    private User loginUser;

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
    //endregion


    //region Overridden Methods

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

        //int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    //endregion


    //region UI

    public void onbtnSignInClick(View view) {
        this.setAllEnabled(false);

        AsyncTask<Object, Object, User.LoginResult> tskCheckLogin = new AsyncTask<Object, Object, User.LoginResult>() {
            @Override
            protected User.LoginResult doInBackground(Object... params) {
                return User.login(txtUserEmail.getText().toString(), txtPassword.getText().toString());
            }

            @Override
            protected void onPostExecute(User.LoginResult params) {
                onLoginComplete(params);
            }
        };
        tskCheckLogin.execute();
    }

    private void setAllEnabled(boolean enabled) {
        btnSignIn.setEnabled(enabled);
        txtUserEmail.setEnabled(enabled);
        txtPassword.setEnabled(enabled);
    }

    //endregion


    //region Functioning

    private void onLoginComplete(User.LoginResult result) {
        User.LoginResult.Result status = result.getResult();

        if (status == User.LoginResult.Result.SUCCESS) {
            ShopWithFriends.setCurrentUser(result.getLoggedInUser());

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else if (status == User.LoginResult.Result.INVALID_INPUT) {
            Utility.showDialog(this, getResources().getString(R.string.invalid_input_activity_login), null);
        }
        else if (status == User.LoginResult.Result.WRONG) {
            Utility.showDialog(this, getResources().getString(R.string.failed_login_activity_login), null);
        }
        else {
            Utility.showDialog(this, getResources().getString(R.string.unknown_error), null);
        }

        this.setAllEnabled(true);
    }

    //endregion

}

