package com.tech.pcreate.ChestXD.Adapter;

import android.content.Context;
import android.icu.util.ValueIterator;
import android.provider.SyncStateContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tech.pcreate.ChestXD.Helpers;
import com.tech.pcreate.ChestXD.Models.DiagnosisModel;
import com.tech.pcreate.ChestXD.R;

import java.util.ArrayList;

public class DiagnosisAdapter extends RecyclerView.Adapter<DiagnosisAdapter.ViewHolder> {
    ArrayList<DiagnosisModel>  itemList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    public DiagnosisAdapter(ArrayList<DiagnosisModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public DiagnosisAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.diagnosisattachment, parent, false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }

    @Override
    public void onBindViewHolder(DiagnosisAdapter.ViewHolder holder, int position) {
        final TextView diagnosistext = holder.view.findViewById(R.id.diagnosistext);
        final ImageView diagnosisimage = holder.view.findViewById(R.id.image);
        final TextView patientname = holder.view.findViewById(R.id.name);
        final TextView patientage = holder.view.findViewById(R.id.age);
        final TextView patientphone = holder.view.findViewById(R.id.phonenumber);
        final TextView patientemail = holder.view.findViewById(R.id.email);
        final TextView datetaken = holder.view.findViewById(R.id.timestamp);

        diagnosistext.setText(itemList.get(position).getDiagnosis());

        //load image here
        try{
            Picasso.with(context).load(itemList.get(position).getImageuri()).into(diagnosisimage);
            patientname.setText(itemList.get(position).getPatientname());
            patientage.setText(itemList.get(position).getPatientage());
            patientphone.setText(itemList.get(position).getPatientphone());
            patientemail.setText(itemList.get(position).getPatientemail());
            datetaken.setText(new Helpers(context).timeformat(itemList.get(position).getTimestamp()));

        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        //image loading ends here

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
