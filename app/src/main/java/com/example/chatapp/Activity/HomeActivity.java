package com.example.chatapp.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.chatapp.R;
import com.example.chatapp.Adaptors.UserAdaptor;
import com.example.chatapp.Models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth auth;

    RecyclerView recyclerView;;
    UserAdaptor userAdaptor;

    private ArrayList<Users> usermodel;

    ImageView logOut,Setting;

    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        logOut=findViewById(R.id.imgLogOut);
        Setting=findViewById(R.id.imgSetting);

        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,SettingActivty.class));
            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(HomeActivity.this,R.style.Dialoge);
                dialog.setContentView(R.layout.custom_dialog);
                TextView yesbtn,nobtn;
                yesbtn=dialog.findViewById(R.id.Yesbtn);
                nobtn=dialog.findViewById(R.id.Nobtn);
                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this, RegistrationActivity.class));
                        dialog.dismiss();
                    }
                });

                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        database = FirebaseDatabase.getInstance();
        usermodel=new ArrayList<>();
        DatabaseReference reference=database.getReference().child("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Users usersgetdata=dataSnapshot.getValue(Users.class);
                    usermodel.add(usersgetdata);
                }
                userAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView=findViewById(R.id.mainUserrecycler);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        userAdaptor=new UserAdaptor(usermodel, HomeActivity.this);
        recyclerView.setAdapter(userAdaptor);

        if (auth.getCurrentUser() == null){
            startActivity(new Intent(HomeActivity.this, RegistrationActivity.class));
        }
    }
}