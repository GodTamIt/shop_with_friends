package www.shopwithfriends.com.shopwithfriends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import models.DataBaseHelper;


/*public class RegisterActivity extends ActionBarActivity {

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

    public void submitRegistration(View view) throws IOException {

        CheckBox isAdminCheck = (CheckBox) findViewById(R.id.reg_is_admin);
        EditText uField = (EditText) findViewById(R.id.reg_uname);
        EditText emailField = (EditText) findViewById(R.id.reg_email);
        EditText passField = (EditText) findViewById(R.id.reg_pass);
        EditText confPassField = (EditText) findViewById(R.id.reg_confpass);
        String uName = uField.getText().toString();
        String email = emailField.getText().toString();
        String pass = passField.getText().toString();
        String confpass = confPassField.getText().toString();
        Boolean isAdmin = isAdminCheck.isChecked();
        String FILENAME = "registered_users_file";
        User user = new User(uName, email, pass, isAdmin);

        Toast toast;
        Context context = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;

        if (uName != null && email != null && pass != null && confpass != null && pass.equals(confpass)) {

            //ModelSingleton.getInstance().addUser(new User(uName, email, pass, false));

            //FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            //ObjectOutputStream os = new ObjectOutputStream(fos);
            //os.writeObject(user);
            //os.close();
            //fos.close();

            DbHelper.getInstance().getWritableDatabase().

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
}*/

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;


public class RegisterActivity extends Activity implements OnClickListener


{
    // Variable Declaration should be in onCreate()
    private Button mSubmit;
    private Button mCancel;

    private EditText mFname;
    private EditText mLname;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;
    private Spinner mGender;
    private String Gen;

    protected DataBaseHelper DB = new DataBaseHelper(RegisterActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        //Assignment of UI fields to the variables
        mSubmit = (Button)findViewById(R.id.submit);
        //mSubmit.setOnClickListener(this);

        mCancel = (Button)findViewById(R.id.cancel);
        //mCancel.setOnClickListener(this);

        mUsername = (EditText)findViewById(R.id.reg_uname);
        mPassword = (EditText)findViewById(R.id.reg_pass);
        mEmail = (EditText)findViewById(R.id.reg_email);
        setContentView(R.layout.activity_register);
    }



    public void onClick(View v)
    {

        switch(v.getId()){

            case R.id.cancel:
                Intent i = new Intent(getBaseContext(), WelcomeActivity.class);
                startActivity(i);
                //finish();
                break;

            case R.id.submit:

                String pass = mPassword.getText().toString();
                String email = mEmail.getText().toString();
                String uname = mUsername.getText().toString();

                boolean invalid = false;



                if(uname.equals(""))
                {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Please enter your Username", Toast.LENGTH_SHORT).show();
                }
                else


                if(pass.equals(""))
                {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();

                }
                else
                if(email.equals(""))
                {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Please enter your Email ID", Toast.LENGTH_SHORT).show();
                }
                else
                if(invalid == false)
                {
                    addEntry(uname, pass, email);
                    Intent i_register = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(i_register);
                    //finish();
                }

                break;
        }
    }





    public void onDestroy()
    {
        super.onDestroy();
        DB.close();
    }



    private void addEntry(String uname, String pass, String email)
    {

        SQLiteDatabase db = DB.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("username", uname);
        values.put("password", pass);
        values.put("email", email);

        try
        {
            db.insert(DataBaseHelper.DATABASE_TABLE_NAME, null, values);

            Toast.makeText(getApplicationContext(), "your details submitted Successfully...", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void submitRegistration(View view) {
        SQLiteDatabase db = DB.getWritableDatabase();

        ContentValues values = new ContentValues();

        EditText uField = (EditText) findViewById(R.id.reg_uname);
        EditText emailField = (EditText) findViewById(R.id.reg_email);
        EditText passField = (EditText) findViewById(R.id.reg_pass);
        EditText confPassField = (EditText) findViewById(R.id.reg_confpass);
        String uname = uField.getText().toString();
        String email = emailField.getText().toString();
        String pass = passField.getText().toString();

        values.put("username", uname);
        values.put("password", pass);
        values.put("email", email);

        try
        {
            db.insert(DataBaseHelper.DATABASE_TABLE_NAME, null, values);

            Toast.makeText(getApplicationContext(), "your details submitted Successfully...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void cancelRegistration(View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}