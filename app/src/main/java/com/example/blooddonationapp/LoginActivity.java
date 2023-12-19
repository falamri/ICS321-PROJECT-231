package com.example.blooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView hyperlinkTextView = findViewById(R.id.hyperlinkTextView);
        makeTextViewHyperlink(hyperlinkTextView);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                // Start HomeActivity when the login button is clicked
            }
        });




    }
    private void login(){
        EditText editTextEmail = findViewById(R.id.email);
        EditText editTextPassword = findViewById(R.id.password);

        String enteredEmail = editTextEmail.getText().toString();
        String enteredPassword = editTextPassword.getText().toString();

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        Person user =dbHelper.loginUser(enteredEmail, enteredPassword);
        if (user!=null) {
            if(dbHelper.isAdmin(enteredEmail,enteredPassword)) {
                Intent intent = new Intent(LoginActivity.this, AdminPage.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(LoginActivity.this, UserPage.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
            }
         else {
            Toast.makeText(this, "Email or Password are incorrect", Toast.LENGTH_SHORT).show();}

    }
    private void makeTextViewHyperlink(TextView textView) {
        SpannableString spannableString = new SpannableString(textView.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Handle the click event, for example, navigate to the next activity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        };

        // Set the clickable span to the entire text
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Apply the clickable span to the TextView
        textView.setText(spannableString);
        textView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

}
