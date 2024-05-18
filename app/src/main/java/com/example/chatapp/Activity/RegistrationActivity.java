package com.example.chatapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.example.chatapp.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    EditText regis_name,regis_email,regis_password,regis_cpassword;
    TextView Sign_Up,Sign_in;

    FirebaseAuth mAuth;

    CircleImageView profile_image;




    //    Image Uri
    Uri uriimage;
    String ImageUri;

    FirebaseDatabase database;
    FirebaseStorage storage;
    String emailPatternregister = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        regis_name=findViewById(R.id.editNameregister);
        regis_email=findViewById(R.id.editEmailregister);
        regis_password=findViewById(R.id.editPasswordregister);
        regis_cpassword=findViewById(R.id.editPasswordconfirmregister);
        Sign_Up=findViewById(R.id.sign_upregister);
        Sign_in=findViewById(R.id.Sign_In);
        profile_image=findViewById(R.id.profile_image);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

//         Firebase Implementation
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();


        Sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name=regis_name.getText().toString();
                String email=regis_email.getText().toString();
                String password=regis_password.getText().toString();
                String cpassword=regis_cpassword.getText().toString();
                String status="Hey There i'm Using Online";



                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(cpassword)){
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this,"Please fill all the fields",Toast.LENGTH_LONG).show();
                } else if (!email.matches(emailPatternregister)) {
                    regis_email.setError("Please Enter Valid Email Address");
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this,"Please enter a valid email",Toast.LENGTH_LONG).show();
                } else if (!password.equals(cpassword)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Password does not match", Toast.LENGTH_LONG).show();
                } else if (password.length()<6) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Password must be at least 6 characters long", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                DatabaseReference reference=database.getReference().child("users").child(mAuth.getUid());
                                StorageReference storageReference=storage.getReference().child("uploads").child(mAuth.getUid());

                                if (uriimage!=null){
                                    storageReference.putFile(uriimage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        ImageUri=uri.toString();
                                                        Users users=new Users(mAuth.getUid(),name,email,ImageUri,status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()){
                                                                    progressDialog.dismiss();
                                                                    startActivity(new Intent(RegistrationActivity.this
                                                                            ,HomeActivity.class));
                                                                    Toast.makeText(RegistrationActivity.this,"User New Created Successfully ",Toast.LENGTH_LONG).show();

                                                                }
                                                                else {
                                                                    Toast.makeText(RegistrationActivity.this,"Failed to create user",Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });


                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {
                                    String status="Hey There i'm Using Online";
                                    ImageUri="https://firebasestorage.googleapis.com/v0/b/chatapp-9bfc0.appspot.com/o/man.png?alt=media&token=ec3c2025-6f2f-4469-94c3-d3de85fccd64";
                                    Users users=new Users(mAuth.getUid(),name,email,ImageUri,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                                progressDialog.dismiss();
                                                startActivity(new Intent(RegistrationActivity.this
                                                        ,HomeActivity.class));
                                                Toast.makeText(RegistrationActivity.this,"User New Created Successfully ",Toast.LENGTH_LONG).show();

                                            }
                                            else {
                                                Toast.makeText(RegistrationActivity.this,"Failed to create user",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }


                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this,"User Creation Failed",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(RegistrationActivity.this,"Registration Successful",Toast.LENGTH_LONG).show();
                }


            }
        });

        Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==10){
            if (data!=null){
                uriimage = data.getData();
                profile_image.setImageURI(uriimage);

            }
        }
    }
}