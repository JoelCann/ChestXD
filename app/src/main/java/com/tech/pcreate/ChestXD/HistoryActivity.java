package com.tech.pcreate.ChestXD;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tech.pcreate.ChestXD.Adapter.DiagnosisAdapter;
import com.tech.pcreate.ChestXD.Models.DiagnosisModel;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView   recyclerView;
    private RecyclerView.Adapter  adapter;
    private RecyclerView.LayoutManager layout;
    private ArrayList<DiagnosisModel>  list;

    private DatabaseReference reference;

    private ProgressBar loading;

    private String saddress, semail, sage, scontact, sfullname, simageurl, sdiagnosis, stimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        loading = findViewById(R.id.loading);

        initializeRecyclerView();

        FetchRecents();
    }

    private void FetchRecents() {
        loading.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("diagnosis");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        FetchPatientID(child.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchPatientID(final String key) {
        list.clear();
        reference = FirebaseDatabase.getInstance().getReference( "diagnosis").child(key);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        if (child.getKey().equals("address")){
                            saddress = child.getValue().toString();
                        }
                        if (child.getKey().equals("age")){
                            sage = child.getValue().toString();
                        }
                        if (child.getKey().equals("contact")){
                            scontact = child.getValue().toString();
                        }
                        if (child.getKey().equals("email")){
                            semail = child.getValue().toString();
                        }
                        if (child.getKey().equals("fullname")){
                            sfullname = child.getValue().toString();
                        }
                    }

                    //FETCH IMAGE DETAILS HERE
                    FetchImageDetails(key, saddress, sage, scontact, semail, sfullname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchImageDetails(final String patientid, String address,
                                   String age, final String scontact, final String email, final String fullname) {
        reference = FirebaseDatabase.getInstance().getReference("diagnosis").child(patientid)
                .child("images");
        //get image id
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        FetchImageAndDiagnosis(patientid, child.getKey(), saddress, sage, scontact, email, fullname);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchImageAndDiagnosis(final String patientid, String imagekey, String address,
                                        final String age, final String scontact, final String email,
                                        final String fullname) {
        reference = FirebaseDatabase.getInstance().getReference("diagnosis").child(patientid)
                .child("images").child(imagekey);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        if (child.getKey().equals("diagnosis")){
                            sdiagnosis = child.getValue().toString();
                        }
                        if (child.getKey().equals("imageurl")){
                            simageurl = child.getValue().toString();
                        }
                        if (child.getKey().equals("timestamp")){
                            stimestamp = child.getValue().toString();
                        }
                    }
                    Toast.makeText(getApplicationContext(),
                            fullname,Toast.LENGTH_SHORT).show();
                    DiagnosisModel obj = new DiagnosisModel(patientid, simageurl, sdiagnosis, fullname, age, scontact, email, stimestamp);
                    list.add(obj);
                    try{
                        adapter.notifyDataSetChanged();
                        loading.setVisibility(View.GONE);
                    }catch (ClassCastException e){
                        e.printStackTrace();
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeRecyclerView() {
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        layout = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, true);

        recyclerView.setLayoutManager(layout);
        adapter = new DiagnosisAdapter(list, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}