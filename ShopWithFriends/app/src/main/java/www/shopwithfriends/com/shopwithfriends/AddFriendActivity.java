package www.shopwithfriends.com.shopwithfriends;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import models.DBHelper;


public class AddFriendActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addFriend(View view) {
        SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();

        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();

        EditText uField = (EditText) findViewById(R.id.reg_uname);
        EditText emailField = (EditText) findViewById(R.id.reg_email);
        String uname = uField.getText().toString();
        String email = emailField.getText().toString();

        values.put("username", uname);
        values.put("email", email);


        SQLiteDatabase dbreadable = DBHelper.getInstance(this).getReadableDatabase();

        // Remember to clean value
        Cursor cursor = dbreadable.rawQuery(
                String.format("SELECT 1 FROM %s WHERE %s=? AND %s=?",
                        DBHelper.USERS_TABLE.NAME,
                        DBHelper.USERS_TABLE.KEY_EMAIL,
                        DBHelper.USERS_TABLE.KEY_PASSWORD), new String[] {uname, email});

        if (cursor != null && cursor.getCount() > 0) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            values2.put("id", id);
            values2.put("friend_id",uname);
            try
            {
                //db.insert(DataBaseHelper.DATABASE_TABLE_NAME, null, values);
                db.insert(DBHelper.FRIENDS_TABLE.NAME, null, values2);

                Toast.makeText(getApplicationContext(), "Friend is added!", Toast.LENGTH_SHORT).show();

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Friend not found";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
            toast.show();
        }



    }

    public void cancelAddFriend(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }
}
