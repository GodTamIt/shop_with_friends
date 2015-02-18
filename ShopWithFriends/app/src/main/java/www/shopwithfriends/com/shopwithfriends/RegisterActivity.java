package www.shopwithfriends.com.shopwithfriends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import models.ModelSingleton;
import models.User;


public class RegisterActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void submitRegistration(View view) {
        //Boolean isAdmin;
        EditText uField = (EditText) findViewById(R.id.reg_uname);
        EditText emailField = (EditText) findViewById(R.id.reg_email);
        EditText passField = (EditText) findViewById(R.id.reg_pass);
        EditText confPassField = (EditText) findViewById(R.id.reg_confpass);
        String uName = uField.getText().toString();
        String email = emailField.getText().toString();
        String pass = passField.getText().toString();
        String confpass = confPassField.getText().toString();

        Toast toast;
        Context context = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;

        if (uName != null && email != null && pass != null && confpass != null && pass.equals(confpass)) {
            ModelSingleton.getInstance().addUser(new User(uName, email, pass, false));
            text = "Welcome " + uName + "! Please login";
            toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
            toast.show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {
            text = "Sorry Registration Failed, Please try again";
            toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public void cancelRegistration(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
