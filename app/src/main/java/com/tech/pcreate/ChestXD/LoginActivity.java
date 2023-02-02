package com.tech.pcreate.ChestXD;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername,etPassword;
    Button btSubmit;

    String usernamestring, passwordstring;

    private static final int IMG_SIZE = 64;
    //private FloatingActionButton btn;
    //private Button predB;
    //private ImageView imageview;
    //private TextView resText;
    private static final String IMAGE_DIRECTORY = "/ClassifyX";
    private int GALLERY = 1, CAMERA = 2;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private String TAG = "MAIN ACTIVITY";
    boolean doubleBackToExitPressedOnce = false;
    private Uri fileUri;
    private List<Classifier> mClassifiers = new ArrayList<>();
    private static final int IMAGE_MEAN = 0;
    private static final float IMAGE_STD = 255;

    FirebaseAuth mauth;

    Helpers helpers;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cardview_shadow_start_color)));

        helpers = new Helpers(this);

        mauth = FirebaseAuth.getInstance();

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, signup.class));
            }
        });

        etUsername=findViewById(R.id.et_username);
        etPassword=findViewById(R.id.et_password);
        loading=findViewById(R.id.loading);
        btSubmit=findViewById(R.id.bt_submit);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernamestring = etUsername.getText().toString().trim();
                passwordstring = etPassword.getText().toString().trim();

                if (usernamestring.equals("") || passwordstring.equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Invalid Username or Password",Toast.LENGTH_SHORT).show();
                }
                else{
                    LoginUserIn(usernamestring+"@gmail.com", passwordstring);
                }
            }
        });
    }
//defining the LoginUserIN function
    private void LoginUserIn(String usernamestring, String passwordstring) {
        loading.setVisibility(View.VISIBLE);
        mauth.signInWithEmailAndPassword(usernamestring, passwordstring).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),
                            "Login successful",Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    helpers.put("isloggedin", true);
                    AlertDialog.Builder builder=new AlertDialog.Builder(
                            LoginActivity.this
                    );
                    builder.setIcon(R.drawable.ic_check);
                    builder.setTitle(" Login Successful");
                    builder.setMessage("Welcome to ChestXD!");

                    builder.setNegativeButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent movetohomepage = new Intent(LoginActivity.this, physicianhomepage.class);
                            movetohomepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(movetohomepage);
                        }
                    });
                    final AlertDialog alertDialog=builder.create();
                    alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                                    .setTextColor(getResources().getColor(R.color.colorPrimary));
                        }
                    });

                    alertDialog.show();

                }
                else {
                    loading.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}