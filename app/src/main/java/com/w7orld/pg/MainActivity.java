package com.w7orld.pg;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static final String STR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    public static final String STR_UPPERCASE = STR_LOWERCASE.toUpperCase();
    public static final String STR_SPECIAL_CHARACTERS = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    public static final String STR_DIGITS = "0123456789";

    //default password length
    private static final int DEFAULT_PWD_LENGTH = 10;

    //UI
    private CheckBox lowerCaseCheckBox;
    private CheckBox upperCaseCheckBox;
    private CheckBox digitsCheckBox;
    private CheckBox specialCharactersCheckBox;
    private EditText pwdLengthEditText;
    private TextView pwdGeneratedTextView;
    private View pwdStrengthLine;
    private TextView pwdStrengthStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind ui
        lowerCaseCheckBox = findViewById(R.id.lowercase);
        upperCaseCheckBox = findViewById(R.id.uppercase);
        digitsCheckBox = findViewById(R.id.digits);
        specialCharactersCheckBox = findViewById(R.id.specialCharacters);
        pwdLengthEditText = findViewById(R.id.pwd_length);
        pwdGeneratedTextView = findViewById(R.id.pwdGenerated);
        pwdStrengthLine = findViewById(R.id.pwdStrength);
        pwdStrengthStatusTextView = findViewById(R.id.pwdStrengthStatus);

        //set default length
        pwdLengthEditText.setText(String.valueOf(DEFAULT_PWD_LENGTH));

    }

    private int getPwdLength() {
        //get password length from EditText
        String getValue = pwdLengthEditText.getText().toString().trim();
        if (TextUtils.isEmpty(getValue)) {
            //the user not entered the length.
            return DEFAULT_PWD_LENGTH;//return default length
        }

        try {
            //convert string to integer
            return Integer.parseInt(getValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return DEFAULT_PWD_LENGTH;//return default length
    }

    public void lengthCounter_plusBtn(View view) {
        //update password length.
        //get current value
        int currentValue = getPwdLength();
        //set current value + 1.
        pwdLengthEditText.setText(String.valueOf((currentValue + 1)));
    }

    public void lengthCounter_minusBtn(View view) {
        //update password length.
        //get current value
        int currentValue = getPwdLength();

        //check current value is not less then 4
        if (currentValue > 4) {
            //set current value - 1.
            pwdLengthEditText.setText(String.valueOf((currentValue - 1)));
        }
    }

    public synchronized void btnGeneratePwd(View view) {
        //generate password.

        //password length
        int length = getPwdLength();

        //check if length not less then 4
        if (length < 4) {
            //set error message
            pwdLengthEditText.setError(getString(R.string.short_password_msg));
            pwdLengthEditText.requestFocus();
            return;
        } else {
            //set error null
            pwdLengthEditText.setError(null);
        }

        StringBuilder passwordGenerated = null;
        boolean isGenerated = false;
        //password pattern
        Pattern pattern = null;
        String mixChars = null;


        if (lowerCaseCheckBox.isChecked() && upperCaseCheckBox.isChecked() && specialCharactersCheckBox.isChecked() && digitsCheckBox.isChecked()) {
            //Create a password containing:
            // Lowercase character
            // Uppercase character
            // Digits
            // Special character

            mixChars = STR_LOWERCASE + STR_UPPERCASE + STR_DIGITS + STR_SPECIAL_CHARACTERS;

            //password pattern
            pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~])(?=.*\\d).+$");

        } else if (lowerCaseCheckBox.isChecked() && upperCaseCheckBox.isChecked() && specialCharactersCheckBox.isChecked()) {
            //Create a password containing:
            // Lowercase character
            // Uppercase character
            // Special character

            mixChars = STR_LOWERCASE + STR_UPPERCASE + STR_SPECIAL_CHARACTERS;
            //password pattern
            pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~]).+$");

        } else if (lowerCaseCheckBox.isChecked() && upperCaseCheckBox.isChecked() && digitsCheckBox.isChecked()) {
            //Create a password containing:
            // Lowercase character
            // Uppercase character
            // Digits

            mixChars = STR_LOWERCASE + STR_UPPERCASE + STR_DIGITS;

            //password pattern
            pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");

        } else if (lowerCaseCheckBox.isChecked() && specialCharactersCheckBox.isChecked() && digitsCheckBox.isChecked()) {
            //Create a password containing:
            // Lowercase character
            // Digits
            // Special character

            mixChars = STR_LOWERCASE + STR_DIGITS + STR_SPECIAL_CHARACTERS;

            //password pattern
            pattern = Pattern.compile("^(?=.*[a-z])(?=.*[!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~])(?=.*\\d).+$");

        } else if (upperCaseCheckBox.isChecked() && specialCharactersCheckBox.isChecked() && digitsCheckBox.isChecked()) {
            //Create a password containing:
            // Uppercase character
            // Digits
            // Special character

            mixChars = STR_UPPERCASE + STR_DIGITS + STR_SPECIAL_CHARACTERS;

            //password pattern
            pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~])(?=.*\\d).+$");

        }else if (lowerCaseCheckBox.isChecked() && upperCaseCheckBox.isChecked()) {
            // Create a password containing:
            // Lowercase character
            // Uppercase character

            mixChars = STR_LOWERCASE + STR_UPPERCASE;

            //password pattern
            pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z]).+$");

        } else if (lowerCaseCheckBox.isChecked() && specialCharactersCheckBox.isChecked()) {
            //Create a password containing:
            // Lowercase character
            // Special character

            mixChars = STR_LOWERCASE + STR_SPECIAL_CHARACTERS;

            //password pattern
            pattern = Pattern.compile("^(?=.*[a-z])(?=.*[!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~]).+$");

        } else if (lowerCaseCheckBox.isChecked() && digitsCheckBox.isChecked()) {
            //Create a password containing:
            // Lowercase character
            // Numbers

            mixChars = STR_LOWERCASE + STR_DIGITS;

            //password pattern
            pattern = Pattern.compile("^(?=.*[a-z])(?=.*\\d).+$");

        } else if (upperCaseCheckBox.isChecked() && specialCharactersCheckBox.isChecked()) {
            //Create a password containing:
            // Uppercase character
            // Special character

            mixChars = STR_UPPERCASE + STR_SPECIAL_CHARACTERS;

            //password pattern
            pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~]).+$");

        } else if (upperCaseCheckBox.isChecked() && digitsCheckBox.isChecked()) {
            //Create a password containing:
            // Uppercase character
            // Numbers

            mixChars = STR_UPPERCASE + STR_DIGITS;

            //password pattern
            pattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d).+$");

        } else if (specialCharactersCheckBox.isChecked() && digitsCheckBox.isChecked()) {
            //Create a password containing:
            // Numbers
            // Special character

            mixChars = STR_DIGITS + STR_SPECIAL_CHARACTERS;

            //password pattern
            pattern = Pattern.compile("^(?=.*[!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~])(?=.*\\d).+$");

        } else if (lowerCaseCheckBox.isChecked()) {
            //Create a password containing lowercase character

            mixChars = STR_LOWERCASE;

            //password pattern
            pattern = Pattern.compile("^(?=.*[a-z]).+$");

        } else if (upperCaseCheckBox.isChecked()) {
            //create password containing uppercase character

            mixChars = STR_UPPERCASE;

            //password pattern
            pattern = Pattern.compile("^(?=.*[A-Z]).+$");

        } else if (specialCharactersCheckBox.isChecked()) {
            //create password containing special character

            mixChars = STR_SPECIAL_CHARACTERS;

            //password pattern
            pattern = Pattern.compile("^(?=.*[!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~]).+$");

        } else if (digitsCheckBox.isChecked()) {
            //create password containing digits

            mixChars = STR_DIGITS;

            //password pattern
            pattern = Pattern.compile("^(?=.*\\d).+$");

        } else {
            //Noting selected. Show message
            Toast.makeText(this, "يجب تحديد على احد الخيارات التي بالاعلى حتى يتم إنشاء كلمة المرور", Toast.LENGTH_SHORT).show();
        }

        //check if the pattern is not null and the random characters not null
        if (pattern != null && mixChars != null) {

            //attempts finding the password
            int attempts = 0;

            //lopping to find the password that matching the pattern
            while (!isGenerated && attempts < 500) {

                //create new instance from StringBuilder
                passwordGenerated = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    //get random chars
                    passwordGenerated.append(mixChars.charAt(new Random().nextInt(mixChars.length() - 1)));
                }

                //check if the password match the pattern.
                //If matched set isGenerated true to stop while loop
                isGenerated = pattern.matcher(passwordGenerated.toString()).matches();

                //check if password was not generated
                if (!isGenerated) {
                    //create new instance from StringBuilder to be empty
                    passwordGenerated = new StringBuilder();
                }

                //update attempts
                attempts++;

            }//end while

            //check if password is not generated
            if (passwordGenerated.toString().isEmpty()) {
                //something went wrong!!
                return;
            }

            //set password generated to textView
            pwdGeneratedTextView.setText(passwordGenerated.toString());

            //check password strength
            PasswordStrength passwordStrength = PasswordStrength.calculate(passwordGenerated.toString());
            //set color to view
            pwdStrengthLine.setBackgroundColor(passwordStrength.color);
            //set password strength to textView
            pwdStrengthStatusTextView.setText(passwordStrength.msg);

            Log.d("MainActivity", "The attempts finding the password:" + attempts);
        }

    }

    public void btnCopyPwd(View view) {
        //get generated password
        String getPassword = pwdGeneratedTextView.getText().toString();

        // check if the password was generated
        if (!getPassword.isEmpty()) {
            //copy the password to clipboard
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("password", getPassword);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.about_item) {
            //Show about dialog
            AlertDialog.Builder aboutDialog = new AlertDialog.Builder(MainActivity.this);
            aboutDialog.setTitle(getString(R.string.about));
            //Create TextView to load html in it
            TextView message = new TextView(this);
            String html = "\n" +
                    "<h4 style=\"text-align:center\" dir=\"rtl\">برمجة وتطوير<a href=\"https://w7orld.com\"> W7orld</a></h4>";

            message.setMovementMethod(LinkMovementMethod.getInstance());
            message.setClickable(true);
            message.setText(Html.fromHtml(html));


            //set textView message to dialog
            aboutDialog.setView(message);
            //set dialog close button
            aboutDialog.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            //show the dialog
            aboutDialog.create().show();
        }

        return super.onOptionsItemSelected(item);
    }
}