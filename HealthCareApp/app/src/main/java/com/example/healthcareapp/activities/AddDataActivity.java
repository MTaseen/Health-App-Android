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
import com.example.healthcareapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddDataActivity extends AppCompatActivity {

    //ui views
    private ImageButton backBtn;
    private EditText titleEt, descriptionEt, illnessSummaryEt, illnessSymptomsEt, illnessTreatmentEt;
    private TextView ageCategory, ethnicityCategory, sexCategory;
    private Button addDataBtn;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

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
        addDataBtn = findViewById(R.id.addDataBtn);

        firebaseAuth = FirebaseAuth.getInstance();

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

        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1) Input Data
                // 2) Validate Data
                // 3) Add Data to Database
                inputData();
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
            Toast.makeText(this, "Illness required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(illnessDescription)){
            Toast.makeText(this, "Illness description required", Toast.LENGTH_SHORT).show();
            return;
        }

        addData();

    }

    private void addData() {
        //Add illness data to database
        progressDialog.setMessage("Add Illness Data");
        progressDialog.show();

        String timestamp = ""+System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("illnessId", ""+timestamp);
        hashMap.put("illnessName", ""+illnessTitle);
        hashMap.put("illnessDescription", ""+illnessDescription);
        hashMap.put("illnessAgeCategory", ""+illnessAgeCategory);
        hashMap.put("illnessEthnicityCategory", ""+illnessEthnicityCategory);
        hashMap.put("illnessSexCategory", ""+illnessSexCategory);
        hashMap.put("illnessSummary", ""+illnessSummary);
        hashMap.put("illnessSymptoms", ""+illnessSymptoms);
        hashMap.put("illnessTreatments", ""+illnessTreatments);
        hashMap.put("timestamp", ""+timestamp);
        hashMap.put("uid", ""+firebaseAuth.getUid());
        //add to database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("Products").child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //added to database
                        progressDialog.dismiss();
                        Toast.makeText(AddDataActivity.this, "Illness Data Added", Toast.LENGTH_SHORT).show();
                        clearData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed adding to database
                        progressDialog.dismiss();
                        Toast.makeText(AddDataActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearData(){
        //clear data entry fields after uploading to database
        titleEt.setText("");
        descriptionEt.setText("");
        ageCategory.setText("");
        ethnicityCategory.setText("");
        sexCategory.setText("");
        illnessSummaryEt.setText("");
        illnessSymptomsEt.setText("");
        illnessTreatmentEt.setText("");

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