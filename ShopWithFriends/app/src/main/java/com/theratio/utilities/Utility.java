package com.theratio.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.Editable;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by Christopher Tam on 2/19/2015.
 */
public class Utility {

    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
    );

    private static final Pattern USERNAME_ADDRESS_PATTERN = Pattern.compile("[a-z0-9_-]{3,16}");


    // BEGIN: Validate Email

    public static boolean validateEmail(CharSequence email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static boolean validateEmail(Editable email) {
        return validateEmail(email.toString());
    }

    public static boolean validateEmail(EditText email) {
        return validateEmail(email.getText().toString());
    }

    // END: Validate Email


    // BEGIN: Validate Username

    public static boolean validateUsername(CharSequence username) {
        return USERNAME_ADDRESS_PATTERN.matcher(username).matches();
    }

    public static boolean validateUsername(Editable username) {
        return validateUsername(username.toString());
    }

    public static boolean validateUsername(EditText username) {
        return validateUsername(username.getText().toString());
    }

    // END: Validate Username


    // BEGIN: Dialogs

    public static AlertDialog showDialog(Activity source, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(source);
        builder.setMessage(message).setPositiveButton("OK", null);

        return builder.show();
    }

    // END: Dialogs
}
