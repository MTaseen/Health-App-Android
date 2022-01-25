package com.example.healthcareapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.healthcareapp.ModelIllnessData;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewDataUserActivity extends AppCompatActivity {

    private TextView illnessNameTv, illnessDetailTv, categoryAgeTv, categoryEthnicityTv, categorySexTv, symptomsTv, treatmentTv;
    private ImageButton backBtn;

    private FirebaseAuth firebaseAuth;

    public ArrayList<ModelIllnessData> illnessDataList, filter;

    private String illnessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_user);

        backBtn = findViewById(R.id.backBtn);
        illnessNameTv = findViewById(R.id.illnessName);
        illnessDetailTv = findViewById(R.id.illnessDetail);
        categoryAgeTv = findViewById(R.id.categoryAge);
        categoryEthnicityTv = findViewById(R.id.categoryEthnicity);
        categorySexTv = findViewById(R.id.categorySex);
        symptomsTv = findViewById(R.id.symptoms);
        treatmentTv = findViewById(R.id.treatments);

        firebaseAuth = FirebaseAuth.getInstance();

        //get id of the illness from intent
        illnessId = getIntent().getStringExtra("illnessId");

        loadIllnessData();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }



    private void loadIllnessData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child("Products").child(illnessId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get data
                        String illnessId = ""+snapshot.child("illnessId").getValue();
                        String illnessName = ""+snapshot.child("illnessName").getValue();
                        String illnessDescription = ""+snapshot.child("illnessDescription").getValue();
                        String illnessAgeCategory = ""+snapshot.child("illnessAgeCategory").getValue();
                        String illnessEthnicityCategory = ""+snapshot.child("illnessEthnicityCategory").getValue();
                        String illnessSexCategory = ""+snapshot.child("illnessSexCategory").getValue();
                        String illnessSummary = ""+snapshot.child("illnessSummary").getValue();
                        String illnessSymptoms = ""+snapshot.child("illnessSymptoms").getValue();
                        String illnessTreatments = ""+snapshot.child("illnessTreatments").getValue();
                        String timestamp = ""+snapshot.child("timestamp").getValue();
                        String uid = ""+snapshot.child("uid").getValue();

                        //set data to textviews
                        illnessNameTv.setText(illnessName);
                        illnessDetailTv.setText(illnessName + " Description: \n \n" + illnessDescription);
                        categoryAgeTv.setText("Age: " + illnessAgeCategory);
                        categoryEthnicityTv.setText("Ethnicity: " + illnessEthnicityCategory);
                        categorySexTv.setText("Sex: " + illnessSexCategory);
                        symptomsTv.setText("Symptoms: \n" + illnessSymptoms);
                        treatmentTv.setText("How to reduce the risk of getting " + illnessName +": \n \n" + illnessTreatments);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}