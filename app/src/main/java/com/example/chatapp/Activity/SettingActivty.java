package com.example.chatapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapp.Models.Users;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivty extends AppCompatActivity {

    CircleImageView settingprofile_image;
    ImageView done_button;

    EditText settingName,SettingStatus;

    FirebaseAuth auth;
    FirebaseDatabase database;

    FirebaseStorage storage;
    Uri settingimgUri;

     String email5;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_activty);
        settingprofile_image=findViewById(R.id.Settingprofile_image);
        done_button=findViewById(R.id.Save);
        settingName=findViewById(R.id.setting_name);
        SettingStatus=findViewById(R.id.setting_status);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        DatabaseReference reference=database.getReference().child("users").child(auth.getUid());
        StorageReference storageReference=storage.getReference().child("uploads").child(auth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email5=snapshot.child("email").getValue().toString();
                String name=snapshot.child("name").getValue().toString();
                String status=snapshot.child("status").getValue().toString();
                String profile=snapshot.child("imageuris").getValue().toString();

                settingName.setText(name);
                SettingStatus.setText(status);
                Picasso.get().load(profile).into(settingprofile_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        settingprofile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
              String settingnam=settingName.getText().toString();
                String settingstatus1=SettingStatus.getText().toString();
       if (settingimgUri !=null){
           storageReference.putFile(settingimgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                     String finalimguri=uri.toString();
                        Users settingUser=new Users(auth.getUid(),settingnam,email5,finalimguri,settingstatus1);
                        reference.setValue(settingUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(SettingActivty.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SettingActivty.this,HomeActivity.class));
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SettingActivty.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
               }
           });
            }else{
           storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               @Override
               public void onSuccess(Uri uri) {
                   String finalimguri=uri.toString();
                   Users settingUser=new Users(auth.getUid(),settingnam,email5,finalimguri,settingstatus1);
                   reference.setValue(settingUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){
                               progressDialog.dismiss();
                               Toast.makeText(SettingActivty.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(SettingActivty.this,HomeActivity.class));
                           }else {
                               progressDialog.dismiss();
                               Toast.makeText(SettingActivty.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }
           });
       }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==10){
            if (data!=null){
                settingimgUri=data.getData();
                settingprofile_image.setImageURI(settingimgUri);
            }
        }
    }
}