package com.example.chatapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText Emaillogin,Passwordlogin;
    TextView signup,Sign_In;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialogs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup=findViewById(R.id.Sign_Up);
        Emaillogin=findViewById(R.id.editEmaillogin);
        Passwordlogin=findViewById(R.id.editPasswordlogin);
        Sign_In=findViewById(R.id.Sign_inlogin);

        progressDialogs=new ProgressDialog(this);
        progressDialogs.setTitle("Loading");
        progressDialogs.setMessage("Please wait...");
        progressDialogs.setCanceledOnTouchOutside(false);

        auth=FirebaseAuth.getInstance();

        Sign_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialogs.show();
                String Email=Emaillogin.getText().toString();
                String Password=Passwordlogin.getText().toString();
                
                if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){
                    progressDialogs.dismiss();
                    Toast.makeText(LoginActivity.this,"Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else if (!Email.matches(emailPattern)) {
                    progressDialogs.dismiss();
                    Emaillogin.setError("InValide Email");
                    Toast.makeText(LoginActivity.this,"InValide Email", Toast.LENGTH_SHORT).show();
                }else if (Password.length()<6){
                    progressDialogs.dismiss();
                    Passwordlogin.setError("Password must be at least 6 characters");
                    Toast.makeText(LoginActivity.this,"Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialogs.dismiss();
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            }else {
                                Toast.makeText(LoginActivity.this,"Error in Login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this,"Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });
        
        signup.setOnClickListener(v -> {
            Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
            startActivity(intent);
        });

    }
}