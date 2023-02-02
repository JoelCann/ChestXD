package com.tech.pcreate.ChestXD;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class form extends AppCompatActivity {

    private EditText efullname, eage, eemail, econtact, eaddress;

    private String sfullname, sage, semail, scontact, saddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        efullname = findViewById(R.id.full_name);
        eage = findViewById(R.id.age);
        eemail = findViewById(R.id.input_email);
        econtact = findViewById(R.id.input_contact);
        eaddress = findViewById(R.id.input_address);

        findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sfullname = efullname.getText().toString().trim();
                sage = eage.getText().toString().trim();
                semail = eemail.getText().toString().trim();
                scontact = econtact.getText().toString().trim();
                saddress = eaddress.getText().toString().trim();

                if (sfullname.equals("")){
                    Toast.makeText(getApplicationContext(),
                            "fullname required",Toast.LENGTH_SHORT).show();
                }
                else if (sage.equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Age required",Toast.LENGTH_SHORT).show();
                }
                else if (semail.equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Email required",Toast.LENGTH_SHORT).show();
                }
                else if (scontact.equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Contact required",Toast.LENGTH_SHORT).show();
                }
                else if (saddress.equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Address required",Toast.LENGTH_SHORT).show();
                }
                else if (!isValidEmail(semail)) {
                    Toast.makeText(getApplicationContext(),
                            "Invalid email",Toast.LENGTH_SHORT).show();
                }
                else{
                    SavePatientDetails(sfullname, sage, semail, scontact, saddress);
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void SavePatientDetails(String sfullname, String sage, String semail,
                                    String scontact, String saddress) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("diagnosis");
        final String patientid = reference.push().getKey();

        Map<String, String> patients = new HashMap<>();
        patients.put("fullname", sfullname);
        patients.put("age", sage);
        patients.put("email", semail);
        patients.put("contact", scontact);
        patients.put("address", saddress);

        reference.child(patientid).setValue(patients).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),
                        "Patient details saved",Toast.LENGTH_SHORT).show();
                Intent movetoimage = new Intent(form.this, CaptureImageActivity.class);
                movetoimage.putExtra("patientid", patientid);
                startActivity(movetoimage);
            }
        });
    }
}