package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.Model.Users;
import com.example.whatsapp.databinding.ActivityMainBinding;
import com.example.whatsapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.BaseProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
   ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database ;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;


    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //checking for users existing : saving users
        if (firebaseUser != null){
            Intent i = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        //Intailazing the progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating New Acocunt");
        progressDialog.setMessage("Signing Up Please Wait...");

// Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        binding.btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.emailet.getText().toString();
                String password = binding.passwordet.getText().toString();

                if (TextUtils.isEmpty(email)|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    binding.emailet.setError("Please Enter a Valid Email");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                  binding.passwordet.setError("Enter your Password");
                    return;
                }
                if ( password.length() < 6){
                    binding.passwordet.setError("your Password should contain at least 6 characters");
                    return;
                }
                if (!(binding.confirmpassword.getText().toString().equals(password))){
                    binding.confirmpassword.setError("the Passwords you entered are not the same");
                    return;
                }


                progressDialog.show();

                auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    Users user = new Users(binding.usernameet.getText().toString(),email,password);
                                    String id = task.getResult().getUser().getUid();
                                    database.getReference().child("Users").child(id).setValue(user);
                                    Toast.makeText(SignUpActivity.this,"Created successfuly", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(SignUpActivity.this,MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();

                                }else{
                                    Toast.makeText(SignUpActivity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
            }
        });

        binding.alreadyhaveaccounttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(i);

            }
        });

    }

}