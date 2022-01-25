package com.example.healthcareapp.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.healthcareapp.activities.MainAdminActivity;
import com.example.healthcareapp.adapter.AdapterIllnessDataAdmin;
import com.example.healthcareapp.Constants;
import com.example.healthcareapp.activities.LoginActivity;
import com.example.healthcareapp.ModelIllnessData;
import com.example.healthcareapp.R;
import com.example.healthcareapp.adapter.AdapterIllnessDataUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SearchFragment extends Fragment implements View.OnClickListener {

    private TextView searchTv, filteredIllnessTv, categoryAge, categoryEthnicity, categorySex;
    private ImageButton logoutBtn;
    private EditText searchIllnessEt;
    private RecyclerView illnessRv;

    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelIllnessData> illnessDataList;
    private AdapterIllnessDataUser adapterIllnessDataUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        searchTv = view.findViewById(R.id.searchTv);
        logoutBtn = view.findViewById(R.id.logout);
        searchIllnessEt = view.findViewById(R.id.searchIllnessEt);
        filteredIllnessTv = view.findViewById(R.id.filteredIllnessTv);
        illnessRv = view.findViewById(R.id.illnessRv2);

        categoryAge = view.findViewById(R.id.categoryAge);
        categoryEthnicity = view.findViewById(R.id.categoryEthnicity);
        categorySex = view.findViewById(R.id.categorySex);

        firebaseAuth = FirebaseAuth.getInstance();
        loadAllIllness();

        adapterIllnessDataUser = new AdapterIllnessDataUser(getContext(), illnessDataList);
        illnessRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        illnessRv.setAdapter(adapterIllnessDataUser);

        //search
        searchIllnessEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterIllnessDataUser.getFilter().filter(s);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        categoryAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Category:")
                        .setItems(Constants.ageCategory1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected category
                                String selected = Constants.ageCategory1[which];
                                filteredIllnessTv.setText("Showing: " + selected);
                                if (selected.equals("All")){
                                    //load all data
                                    loadAllIllness();
                                    //change to black if all is selected
                                    categoryAge.setTextColor(getResources().getColor(R.color.colorBlack));
                                }
                                else{
                                    //load filtered
                                    loadFilteredIllnesses2(selected);

                                    //text colour change when clicked
                                    categoryAge.setTextColor(getResources().getColor(R.color.colorWhite));
                                    categoryEthnicity.setTextColor(getResources().getColor(R.color.colorBlack));
                                    categorySex.setTextColor(getResources().getColor(R.color.colorBlack));
                                }
                            }
                        })
                        .show();
            }
        });

        categoryEthnicity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //text colour change when clicked

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Category:")
                        .setItems(Constants.ethnicityCategory, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected category
                                String selected2 = Constants.ethnicityCategory[which];
                                filteredIllnessTv.setText("Showing: " + selected2);
                                if (selected2.equals("All")){
                                    //load all data
                                    loadAllIllness();
                                    categoryEthnicity.setTextColor(getResources().getColor(R.color.colorBlack));
                                }
                                else{
                                    //load filtered
                                    loadFilteredIllnesses2(selected2);
                                    categoryAge.setTextColor(getResources().getColor(R.color.colorBlack));
                                    categoryEthnicity.setTextColor(getResources().getColor(R.color.colorWhite));
                                    categorySex.setTextColor(getResources().getColor(R.color.colorBlack));
                                }
                            }
                        })
                        .show();

            }
        });

        categorySex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //text colour change when clicked

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Category:")
                        .setItems(Constants.sexCategory, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected category
                                String selected2 = Constants.sexCategory[which];
                                filteredIllnessTv.setText("Showing: " + selected2);
                                if (selected2.equals("All")){
                                    //load all data
                                    loadAllIllness();
                                    categorySex.setTextColor(getResources().getColor(R.color.colorBlack));
                                }
                                else{
                                    //load filtered
                                    //illnessDataArrayList = new ArrayList<>();
                                    loadFilteredIllnesses2(selected2);
                                    categoryAge.setTextColor(getResources().getColor(R.color.colorBlack));
                                    categoryEthnicity.setTextColor(getResources().getColor(R.color.colorBlack));
                                    categorySex.setTextColor(getResources().getColor(R.color.colorWhite));
                                }
                            }
                        })
                        .show();

            }
        });

        return view;
    }


    private void loadFilteredIllnesses2(final String selected) {
        illnessDataList = new ArrayList<>();

        //get all illness data
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("Products").orderByChild("illnessName")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //before getting reset list
                        illnessDataList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){



                            String ageCategory = ""+ds.child("illnessAgeCategory").getValue();
                            String ethnicityCategory = ""+ds.child("illnessEthnicityCategory").getValue();
                            String sexCategory = ""+ds.child("illnessSexCategory").getValue();

                            //if selected category matches age category then add to list
                            if (selected.equals(ageCategory)){
                                ModelIllnessData modelIllnessData = ds.getValue(ModelIllnessData.class);
                                illnessDataList.add(modelIllnessData);
                            }

                            //if selected category matches ethnicity category then add to list
                            if (selected.equals(ethnicityCategory)){
                                ModelIllnessData modelIllnessData = ds.getValue(ModelIllnessData.class);
                                illnessDataList.add(modelIllnessData);
                            }

                            //if selected category matches sex category then add to list
                            if (selected.equals(sexCategory)){
                                ModelIllnessData modelIllnessData = ds.getValue(ModelIllnessData.class);
                                illnessDataList.add(modelIllnessData);
                            }

                        }
                        //com.example.healthcareapp.adapter setup
                        adapterIllnessDataUser = new AdapterIllnessDataUser(getContext(), illnessDataList);
                        illnessRv.setAdapter(adapterIllnessDataUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllIllness() {
        illnessDataList = new ArrayList<>();

        //get all illness data
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("Products").orderByChild("illnessName")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //before getting reset list
                        illnessDataList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelIllnessData modelIllnessData = ds.getValue(ModelIllnessData.class);
                            illnessDataList.add(modelIllnessData);
                        }
                        //com.example.healthcareapp.adapter setup
                        adapterIllnessDataUser = new AdapterIllnessDataUser(getContext(), illnessDataList);
                        illnessRv.setAdapter(adapterIllnessDataUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    @Override
    public void onClick(View v) {

    }
}