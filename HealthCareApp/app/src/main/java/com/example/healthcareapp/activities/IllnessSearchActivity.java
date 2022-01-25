package com.example.healthcareapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import com.example.healthcareapp.adapter.AdapterIllnessDataUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IllnessSearchActivity extends AppCompatActivity {

    private TextView nameTv, filteredIllnessTv;
    private ImageButton logoutBtn, filterIllnessBtn, addAdminBtn;
    private EditText searchIllnessEt;
    private FloatingActionButton addDataBtn;
    private RecyclerView illnessRv;

    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelIllnessData> illnessDataList;
    private AdapterIllnessDataUser adapterIllnessDataUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illness_search);

        nameTv = findViewById(R.id.nameTv);
        logoutBtn = findViewById(R.id.logout);
        addDataBtn = findViewById(R.id.addDataBtn);
        searchIllnessEt = findViewById(R.id.searchIllnessEt);
        filterIllnessBtn = findViewById(R.id.filterIllnessBtn);
        filteredIllnessTv = findViewById(R.id.filteredIllnessTv);
        illnessRv = findViewById(R.id.illnessRv2);
        addAdminBtn = findViewById(R.id.addAdminBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        //checkUser();
        loadAllIllness();



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
                //checkUser();
            }
        });


        filterIllnessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IllnessSearchActivity.this);
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
                                }
                                else{
                                    //load filtered
                                    loadFilteredIllnesses(selected);
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void loadFilteredIllnesses(final String selected) {
        illnessDataList = new ArrayList<>();

        //get all illness data
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //before getting reset list
                        illnessDataList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){

                            String ageCategory = ""+ds.child("illnessAgeCategory").getValue();

                            //if selected category matches age category then add to list
                            if (selected.equals(ageCategory)){
                                ModelIllnessData modelIllnessData = ds.getValue(ModelIllnessData.class);
                                illnessDataList.add(modelIllnessData);
                            }

                        }
                        //com.example.healthcareapp.adapter setup
                        adapterIllnessDataUser = new AdapterIllnessDataUser(IllnessSearchActivity.this, illnessDataList);
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
        reference.child("Products")
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
                        adapterIllnessDataUser = new AdapterIllnessDataUser(IllnessSearchActivity.this, illnessDataList);
                        illnessRv.setAdapter(adapterIllnessDataUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    /*private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(IllnessSearchActivity.this, LoginActivity.class));
            finish();
        }
        else {
            loadMyInfo();
        }
    }*/

    /*private void loadMyInfo() {
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
                            nameTv.setText("User-" + name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }*/
}