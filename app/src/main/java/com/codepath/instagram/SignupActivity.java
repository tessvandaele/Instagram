package com.codepath.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    private EditText etUsernameSignup;
    private EditText etPasswordSignup;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsernameSignup = findViewById(R.id.etUsernameSignup);
        etPasswordSignup = findViewById(R.id.etPasswordSignup);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        //set up create account button
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a parse user
                ParseUser user = new ParseUser();

                //set core properties of user
                user.setUsername(etUsernameSignup.getText().toString());
                user.setPassword(etPasswordSignup.getText().toString());

                //invoke sign up in background
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null) { //exception occurred
                            Log.e("SignupActivity", "Sign up unsuccessful", e);
                            return;
                        }
                        Toast.makeText(SignupActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        });
    }
}