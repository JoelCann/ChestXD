package com.tech.pcreate.ChestXD;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class landing3 extends AppCompatActivity {

    private Button skip;
    private TextView next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing3);

        skip = findViewById(R.id.skip);
        next = findViewById(R.id.nextt);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ekow = new Intent(landing3.this, category.class);
                startActivity(ekow);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ekow1= new Intent(landing3.this, LoginActivity.class);
                landing3.this.startActivity(ekow1);
            }
        });
    }
}