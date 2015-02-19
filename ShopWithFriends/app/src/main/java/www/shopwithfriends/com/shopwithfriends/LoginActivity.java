package www.shopwithfriends.com.shopwithfriends;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import models.DBHelper;

public class LoginActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private String userName = "user";
    private String password = "pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //toolbar  = (Toolbar) findViewById(R.id.action_bar);
        //setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // inflate menu from xml

        // Don't show menu
        // getMenuInflater().inflate(R.menu.menu_login, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkLogin(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        EditText uField = (EditText) findViewById(R.id.emailField);
        String uName = uField.getText().toString();
        EditText passField = (EditText) findViewById(R.id.passField);
        String uPass = passField.getText().toString();

        SQLiteDatabase db = DBHelper.getInstance(this).getReadableDatabase();

        // Remember to clean value
        Cursor cursor = db.rawQuery(
                String.format("SELECT 1 FROM %s WHERE %s=? AND %s=?",
                        DBHelper.USERS_TABLE.NAME,
                        DBHelper.USERS_TABLE.KEY_EMAIL,
                        DBHelper.USERS_TABLE.KEY_PASSWORD), new String[] {uName, uPass});

        if (cursor != null && cursor.getCount() > 0) {
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

    public void cancelLogin(View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void register(View view) {

    }
}

