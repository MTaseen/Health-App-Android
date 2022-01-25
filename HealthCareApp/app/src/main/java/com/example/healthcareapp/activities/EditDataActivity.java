package com.example.healthcareapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Constants;
import com.example.healthcareapp.ModelIllnessData;
import com.example.healthcareapp.R;
import com.example.healthcareapp.adapter.AdapterIllnessDataAdmin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EditDataActivity extends AppCompatActivity {

    //ui views
    private ImageButton backBtn;
    private EditText titleEt, descriptionEt, illnessSummaryEt, illnessSymptomsEt, illnessTreatmentEt;
    private TextView ageCategory, ethnicityCategory, sexCategory;
    private Button updateDataBtn;

    public ArrayList<ModelIllnessData> illnessDataArrayList;

    private String illnessId;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser User;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        //init ui views
        backBtn = findViewById(R.id.backBtn);
        titleEt = findViewById(R.id.titleEt);
        descriptionEt = findViewById(R.id.descriptionEt);
        ageCategory = findViewById(R.id.ageCategory);
        ethnicityCategory = findViewById(R.id.ethnicityCategory);
        sexCategory = findViewById(R.id.sexCategory);
        illnessSummaryEt = findViewById(R.id.illnessSummary);
        illnessSymptomsEt = findViewById(R.id.symptomsEt);
        illnessTreatmentEt = findViewById(R.id.treatmentEt);
        updateDataBtn = findViewById(R.id.updateDataBtn);

        //get id of the product from intent
        illnessId = getIntent().getStringExtra("illnessId");

        firebaseDatabase = FirebaseDatabase.getInstance();
        loadIllnessData();

        //progress dialog setup
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ageCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick age category
                ageCategoryDialog();
            }
        });

        ethnicityCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick Ethnicity category
                ethnicityCategoryDialog();
            }
        });

        sexCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick sex category
                sexCategoryDialog();
            }
        });

        updateDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1) Input Data
                // 2) Validate Data
                // 3) Update Data to Database
                inputData();
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
                        String timestamp = ""+snapshot.child("timestamp").getValue();
                        String uid = ""+snapshot.child("uid").getValue();

                        titleEt.setText(illnessName);
                        descriptionEt.setText(illnessDescription);
                        ageCategory.setText(illnessAgeCategory);
                        ethnicityCategory.setText(illnessEthnicityCategory);
                        sexCategory.setText(illnessSexCategory);
                        illnessSummaryEt.setText(illnessSummary);
                        illnessSymptomsEt.setText(illnessSymptoms);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }



    private String illnessTitle, illnessDescription, illnessAgeCategory, illnessEthnicityCategory, illnessSexCategory, illnessSummary, illnessSymptoms, illnessTreatments;
    private void inputData() {
        //Input Data
        illnessTitle = titleEt.getText().toString().trim();
        illnessDescription = descriptionEt.getText().toString().trim();
        illnessAgeCategory = ageCategory.getText().toString().trim();
        illnessEthnicityCategory = ethnicityCategory.getText().toString().trim();
        illnessSexCategory = sexCategory.getText().toString().trim();
        illnessSummary = illnessSummaryEt.getText().toString().trim();
        illnessSymptoms = illnessSymptomsEt.getText().toString().trim();
        illnessTreatments = illnessTreatmentEt.getText().toString().trim();


        //Validate Data
        if (TextUtils.isEmpty(illnessTitle)){
            Toast.makeText(this, "Illness Name required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(illnessDescription)){
            Toast.makeText(this, "Illness description required", Toast.LENGTH_SHORT).show();
            return;
        }

        updateData();

    }

    private void updateData() {
        //show progress
        progressDialog.setMessage("Updating Data");
        progressDialog.show();

        //data set up in hashmap to update
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("illnessName", "" + illnessTitle);
        hashMap.put("illnessDescription", "" + illnessDescription);
        hashMap.put("illnessAgeCategory", "" + illnessAgeCategory);
        hashMap.put("illnessEthnicityCategory", "" + illnessEthnicityCategory);
        hashMap.put("illnessSexCategory", "" + illnessSexCategory);
        hashMap.put("illnessSummary", "" + illnessSummary);
        hashMap.put("illnessSymptoms", "" + illnessSymptoms);
        hashMap.put("illnessTreatments", ""+illnessTreatments);

        //update data in db
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("Products").child(illnessId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //update successful
                        progressDialog.dismiss();
                        Toast.makeText(EditDataActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //update failed
                        progressDialog.dismiss();
                        Toast.makeText(EditDataActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ageCategoryDialog() {
        //dialog for age category
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Age Category")
                .setItems(Constants.ageCategory1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get picked category
                        String ageRangeCategory = Constants.ageCategory1[which];

                        //set picked category
                        ageCategory.setText(ageRangeCategory);


                    }
                })
                .show();
    }

    private void ethnicityCategoryDialog() {
        //dialog for age category
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ethnicity Category")
                .setItems(Constants.ethnicityCategory, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get picked category
                        String pickEthnicityCategory = Constants.ethnicityCategory[which];

                        //set picked category
                        ethnicityCategory.setText(pickEthnicityCategory);


                    }
                })
                .show();
    }

    private void sexCategoryDialog() {
        //dialog for age category
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sex Category")
                .setItems(Constants.sexCategory, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get picked category
                        String pickSexCategory = Constants.sexCategory[which];

                        //set picked category
                        sexCategory.setText(pickSexCategory);


                    }
                })
                .show();
    }

}