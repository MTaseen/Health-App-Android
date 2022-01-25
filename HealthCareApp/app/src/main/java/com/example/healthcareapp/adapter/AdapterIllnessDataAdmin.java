package com.example.healthcareapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.FilterProduct;
import com.example.healthcareapp.ModelIllnessData;
import com.example.healthcareapp.R;
import com.example.healthcareapp.activities.EditDataActivity;
import com.example.healthcareapp.activities.ViewDataAdminActivity;

import java.util.ArrayList;

public class AdapterIllnessDataAdmin extends RecyclerView.Adapter<AdapterIllnessDataAdmin.HolderIllnessDataAdmin> implements Filterable {

    private Context context;
    public ArrayList<ModelIllnessData> illnessDataArrayList, filterList;
    private FilterProduct filter;

    public AdapterIllnessDataAdmin(Context context, ArrayList<ModelIllnessData> illnessDataArrayList) {
        this.context = context;
        this.illnessDataArrayList = illnessDataArrayList;
        this.filterList = illnessDataArrayList;
    }

    @NonNull
    @Override
    public HolderIllnessDataAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_illness_data_admin, parent, false);
        return new HolderIllnessDataAdmin(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderIllnessDataAdmin holder, int position) {
        //get data
        final ModelIllnessData modelIllnessData = illnessDataArrayList.get(position);
        final String id = modelIllnessData.getIllnessId();
        String uid = modelIllnessData.getUid();
        String title = modelIllnessData.getIllnessName();
        String illnessDescription = modelIllnessData.getIllnessDescription();
        String ageCategory = modelIllnessData.getIllnessAgeCategory();
        String ethnicityCategory = modelIllnessData.getIllnessEthnicityCategory();
        String countryCategory = modelIllnessData.getIllnessCountryCategory();
        String illnessSummary = modelIllnessData.getIllnessSummary();
        String illnessSymptoms = modelIllnessData.getIllnessSymptoms();
        String timestamp = modelIllnessData.getTimestamp();

        //Set data
        holder.titleTv.setText(title);
        holder.summaryTv.setText(illnessSummary);
        holder.symptomsTv.setText("Symptoms: \n" +
                "" + illnessSymptoms);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass id of illness
                Intent intent = new Intent(context, ViewDataAdminActivity.class);
                intent.putExtra("illnessId", id);
                context.startActivity(intent);
            }
        });
    }




    @Override
    public int getItemCount() {
        return illnessDataArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterProduct(this, filterList);
        }
        return filter;
    }

    public static class HolderIllnessDataAdmin extends RecyclerView.ViewHolder {
        //holds views of recyclerview

        private TextView titleTv, summaryTv, symptomsTv;

        public HolderIllnessDataAdmin(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.titleTv);
            summaryTv = itemView.findViewById(R.id.summaryTv);
            symptomsTv = itemView.findViewById(R.id.symptomsTv);

        }
    }
}
