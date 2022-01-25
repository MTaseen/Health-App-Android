package com.example.healthcareapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.R;
import com.example.healthcareapp.adapter.AdapterIllnessDataAdmin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.example.healthcareapp.ModelIllnessData;

public class ViewDataAdminActivity extends AppCompatActivity {

    private TextView illnessNameTv, illnessDetailTv, categoryAgeTv, categoryEthnicityTv, categorySexTv, symptomsTv, treatmentTv;
    private ImageButton backBtn, editBtn, deleteBtn;

    private FirebaseAuth firebaseAuth;

    public ArrayList<ModelIllnessData> illnessDataArrayList, filterList;

    private String illnessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_admin);

        backBtn = findViewById(R.id.backBtn);
        editBtn = findViewById(R.id.editDataBtn);
        deleteBtn = findViewById(R.id.deleteDataBtn);

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

        //final ModelIllnessData modelIllnessData =

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open edit data activity
                //passes illness id
                Intent intent = new Intent(ViewDataAdminActivity.this , EditDataActivity.class);
                String id = illnessId;
                intent.putExtra("illnessId", id);
                startActivity(intent);
            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDataDialog();
            }
        });

    }

    private void showDeleteDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");

        LinearLayout linearLayout = new LinearLayout(this);

        builder.setPositiveButton("Delete Data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //delete
                Intent i = new Intent(ViewDataAdminActivity.this, MainAdminActivity.class);
                startActivity(i);
                deleteIllness(illnessId);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void deleteIllness(String illnessId) {
        //delete illness data entry by using its Id


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("Products").child(illnessId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //illness entry deleted
                        Toast.makeText(ViewDataAdminActivity.this, "Entry deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed deleting entry
                        Toast.makeText(ViewDataAdminActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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