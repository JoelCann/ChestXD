package com.tech.pcreate.ChestXD;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity {

    EditText username, password, confirmpass;

    String usernamestring, passwordstring, confirmpassstring;

    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mauth = FirebaseAuth.getInstance();

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        confirmpass = findViewById(R.id.confirm_password);

        findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernamestring = username.getText().toString().trim();
                passwordstring = password.getText().toString().trim();
                confirmpassstring = confirmpass.getText().toString().trim();
                if(usernamestring.equals("")){
                    Toast.makeText(getApplicationContext(),
                            "username required",Toast.LENGTH_SHORT).show();
                }
                else if(passwordstring.equals("")){
                    Toast.makeText(getApplicationContext(),
                            "password required",Toast.LENGTH_SHORT).show();
                }
                else if(!passwordstring.equals(confirmpassstring)){
                    Toast.makeText(getApplicationContext(),
                            "password mismatch",Toast.LENGTH_SHORT).show();
                }
                else{
                    CreateUserAccount(usernamestring, passwordstring);
                }
            }
        });

           findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signup.this, LoginActivity.class));
            }
        });


    }

    private void CreateUserAccount(String usernamestring, String passwordstring) {
        mauth.createUserWithEmailAndPassword(usernamestring + "@gmail.com", passwordstring).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){
                  Toast.makeText(getApplicationContext(),  "Sign up successful", Toast.LENGTH_LONG).show();
                  finish();
              }else {
                  Toast.makeText(getApplicationContext(),  "Couldn't complete registration", Toast.LENGTH_LONG).show();
                  finish();
              }
            }
        });
    }
}