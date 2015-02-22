package com.theratio.shopwithfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class WelcomeActivity extends ActionBarActivity {

    //region Overridden Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
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

    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.welcome_btnLogin:
                Intent loginPage = new Intent(this, LoginActivity.class);
                startActivity(loginPage);
                break;
            case R.id.welcome_btnRegister:
                Intent registerPage = new Intent(this, RegisterActivity.class);
                startActivity(registerPage);
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
    }

    //endregion

}
