package com.tech.pcreate.ChestXD;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class landing1 extends AppCompatActivity {

    private Button skip;
    private TextView next;

    private Helpers helpers;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing1);

        mauth = FirebaseAuth.getInstance();

        helpers = new Helpers(landing1.this);

        skip = findViewById(R.id.skip);
        next = findViewById(R.id.next);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ekow = new Intent(landing1.this, category.class);
                landing1.this.startActivity(ekow);
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ekow1= new Intent(landing1.this,landing2.class);
                landing1.this.startActivity(ekow1);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mauth.getCurrentUser() != null){
            if (helpers.getBool("isloggedin")){
                Intent movetohomepage = new Intent(this, physicianhomepage.class);
                movetohomepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(movetohomepage);
            }
        }
    }
}