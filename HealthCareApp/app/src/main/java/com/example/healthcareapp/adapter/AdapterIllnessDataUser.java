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

import com.example.healthcareapp.FilterProductUser;
import com.example.healthcareapp.ModelIllnessData;
import com.example.healthcareapp.R;
import com.example.healthcareapp.activities.IllnessSearchActivity;
import com.example.healthcareapp.activities.ViewDataUserActivity;

import java.util.ArrayList;

public class AdapterIllnessDataUser extends RecyclerView.Adapter<AdapterIllnessDataUser.HolderIllnessDataUser> implements Filterable {

    private Context context;
    public ArrayList<ModelIllnessData> illnessDataList, filterList;
    private FilterProductUser filter;

    public AdapterIllnessDataUser(Context context, ArrayList<ModelIllnessData> illnessDataList) {
        this.context = context;
        this.illnessDataList = illnessDataList;
        this.filterList = illnessDataList;
    }

    @NonNull
    @Override
    public HolderIllnessDataUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_illness_data_user, parent, false);
        return new HolderIllnessDataUser(view);
    }

    @Override
    public int getItemCount() {
        return illnessDataList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull HolderIllnessDataUser holder, int position) {
        ModelIllnessData modelIllnessData = illnessDataList.get(position);
        final String illnessId = modelIllnessData.getIllnessId();
        String illnessName = modelIllnessData.getIllnessName();
        String illnessSummary = modelIllnessData.getIllnessSummary();
        String illnessSymptoms = modelIllnessData.getIllnessSymptoms();

        //set data
        holder.illnessNameTv.setText(illnessName);
        holder.summaryTv.setText(illnessSummary);
        holder.symptomsTv.setText(illnessSymptoms);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show illness details
                Intent intent = new Intent(context, ViewDataUserActivity.class);
                intent.putExtra("illnessId", illnessId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterProductUser(this,filterList);
        }
        return filter;
    }

    public  static class HolderIllnessDataUser extends RecyclerView.ViewHolder{
        //Holds views of recyclerview

        private TextView illnessNameTv, summaryTv, symptomsTv;

        public HolderIllnessDataUser(@NonNull View itemView) {
            super(itemView);

            illnessNameTv = itemView.findViewById(R.id.illnessNameTv);
            summaryTv = itemView.findViewById(R.id.summaryTv);
            symptomsTv = itemView.findViewById(R.id.symptomsTv);
        }



    }

}
