package www.shopwithfriends.com.shopwithfriends;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class HomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

        else if (id == R.id.add_friend) {
            Intent intent = new Intent(this, AddFriendActivity.class);
            intent.putExtra("USER_ID", getIntent().getLongExtra("USER_ID", 0L));
            startActivity(intent);
        }

        else if (id == R.id.friend_list) {
            Intent intent = new Intent(this, FriendListActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.logout) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
