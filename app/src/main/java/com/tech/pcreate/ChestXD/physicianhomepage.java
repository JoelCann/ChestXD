package com.tech.pcreate.ChestXD;

//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.FloatingActionButton;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.PendingIntent.getActivity;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class physicianhomepage extends AppCompatActivity {


    private Button logout, history;

    private Helpers helpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physicianhomepage);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cardview_shadow_start_color)));

        helpers = new Helpers(this);

        logout= findViewById(R.id.logout);
        history= findViewById(R.id.history);

        findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(physicianhomepage.this, HistoryActivity.class));
            }
        });

        findViewById(R.id.diagnosepatient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(physicianhomepage.this, form.class));
            }
        });

        logout();
    }
    //function of logout button
    private void logout() {

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(
                        physicianhomepage.this
                );
                builder.setIcon(R.drawable.ic_check);
                builder.setTitle("LOGOUT?");
                builder.setMessage("Are you sure you want to logout?");

                builder.setNegativeButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        helpers.clearStore();
                        dialog.cancel();
                        Intent movetohomepage = new Intent(physicianhomepage.this, category.class);
                        movetohomepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(movetohomepage);
                    }
                });

                builder.setPositiveButton("Stay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                final AlertDialog alertDialog=builder.create();
                alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                                .setTextColor(getResources().getColor(R.color.red));
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                .setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                });

                alertDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new Helpers(this).goToHome();
    }
}