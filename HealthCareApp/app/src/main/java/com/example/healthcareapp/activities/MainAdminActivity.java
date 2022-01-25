package com.example.healthcareapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.healthcareapp.adapter.AdapterIllnessDataAdmin;
import com.example.healthcareapp.Constants;
import com.example.healthcareapp.ModelIllnessData;
import com.example.healthcareapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainAdminActivity extends AppCompatActivity {

    private TextView nameTv, filteredIllnessTv, categoryAge, categoryEthnicity, categorySex;
    private ImageButton logoutBtn, filterIllnessBtn, filterIllnessBtn2, addAdminBtn;
    private EditText searchIllnessEt;
    private FloatingActionButton addDataBtn;
    private RecyclerView illnessRv;
    private ModelIllnessData modelIllnessData;

    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelIllnessData> illnessDataArrayList, List;
    private AdapterIllnessDataAdmin adapterIllnessDataAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        nameTv = findViewById(R.id.nameTv);
        logoutBtn = findViewById(R.id.logout);
        addDataBtn = findViewById(R.id.addDataBtn);
        searchIllnessEt = findViewById(R.id.searchIllnessEt);
        filteredIllnessTv = findViewById(R.id.filteredIllnessTv);
        illnessRv = findViewById(R.id.illnessRv);
        addAdminBtn = findViewById(R.id.addAdminBtn);

        categoryAge = findViewById(R.id.categoryAge);
        categoryEthnicity = findViewById(R.id.categoryEthnicity);
        categorySex = findViewById(R.id.categorySex);


        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadAllIllness();

        //List = new ArrayList<>();

        //search
        searchIllnessEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterIllnessDataAdmin.getFilter().filter(s);
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
                checkUser();
            }
        });

        addAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainAdminActivity.this, RegisterAdminActivity.class));
            }
        });

        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open add illness activity
                startActivity(new Intent(MainAdminActivity.this, AddDataActivity.class));

            }
        });

        categoryAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //text colour change when clicked


                AlertDialog.Builder builder = new AlertDialog.Builder(MainAdminActivity.this);
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
                                    categoryAge.setTextColor(getResources().getColor(R.color.colorBlack));
                                }
                                else{
                                    //load filtered
                                    loadFilteredIllnesses2(selected);
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


                AlertDialog.Builder builder = new AlertDialog.Builder(MainAdminActivity.this);
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
                                    //illnessDataArrayList = new ArrayList<>();
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

                AlertDialog.Builder builder = new AlertDialog.Builder(MainAdminActivity.this);
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
    }

    private void loadFilteredIllnesses2(final String selected) {
        illnessDataArrayList = new ArrayList<>();

        //get all illness data
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("Products").orderByChild("illnessName")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //before getting reset list
                        illnessDataArrayList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){



                            String ageCategory = ""+ds.child("illnessAgeCategory").getValue();
                            String ethnicityCategory = ""+ds.child("illnessEthnicityCategory").getValue();
                            String sexCategory = ""+ds.child("illnessSexCategory").getValue();

                            //if selected category matches age category then add to list
                            if (selected.equals(ageCategory)){
                                ModelIllnessData modelIllnessData = ds.getValue(ModelIllnessData.class);
                                illnessDataArrayList.add(modelIllnessData);
                            }

                            //if selected category matches ethnicity category then add to list
                            if (selected.equals(ethnicityCategory)){
                                ModelIllnessData modelIllnessData = ds.getValue(ModelIllnessData.class);
                                illnessDataArrayList.add(modelIllnessData);
                            }

                            //if selected category matches sex category then add to list
                            if (selected.equals(sexCategory)){
                                ModelIllnessData modelIllnessData = ds.getValue(ModelIllnessData.class);
                                illnessDataArrayList.add(modelIllnessData);
                            }

                        }
                        //com.example.healthcareapp.adapter setup
                        adapterIllnessDataAdmin = new AdapterIllnessDataAdmin(MainAdminActivity.this, illnessDataArrayList);
                        illnessRv.setAdapter(adapterIllnessDataAdmin);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllIllness() {
        illnessDataArrayList = new ArrayList<>();

        //get all illness data
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("Products").orderByChild("illnessName")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //before getting reset list
                        illnessDataArrayList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelIllnessData modelIllnessData = ds.getValue(ModelIllnessData.class);
                            illnessDataArrayList.add(modelIllnessData);
                        }
                        //com.example.healthcareapp.adapter setup
                        adapterIllnessDataAdmin = new AdapterIllnessDataAdmin(MainAdminActivity.this, illnessDataArrayList);
                        illnessRv.setAdapter(adapterIllnessDataAdmin);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainAdminActivity.this, LoginActivity.class));
            finish();
        }
        else {
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            //get data from database
                            String name = ""+ds.child("name").getValue();
                            String accountType = ""+ds.child("accountType").getValue();

                            //set data on given textview
                            nameTv.setText("Admin-" + name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}