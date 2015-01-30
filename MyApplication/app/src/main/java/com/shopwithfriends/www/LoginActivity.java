package com.shopwithfriends.www;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity {

    //String userName = "user";
    //String password = "pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkLogin(View view) {

        Intent intent = new Intent(this, HomeActivity.class);
        EditText uField = (EditText) findViewById(R.id.user_field);
        String uName = uField.getText().toString();
        EditText passField = (EditText) findViewById(R.id.passField);
        String uPass = passField.getText().toString();
        System.out.println(uName);
        System.out.println(uPass);
        if (uName.equals(getString(R.string.userName)) && uPass.equals(getString(R.string.password))) {
                startActivity(intent);
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Login Failed";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    public void register(View view) {
        // Do something in response to button
    }
}
