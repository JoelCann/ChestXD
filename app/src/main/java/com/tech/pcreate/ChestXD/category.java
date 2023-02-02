package com.tech.pcreate.ChestXD;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class category extends AppCompatActivity {

    private Helpers helpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        helpers = new Helpers(this);

        findViewById(R.id.patientcard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(category.this, Patienthomepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                helpers.put( "usertype", "patient");
                startActivity(intent);
            }
        });

        findViewById(R.id.physiciancard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(category.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                helpers.put( "usertype", "physician");
                startActivity(intent);
            }
        });
    }
}