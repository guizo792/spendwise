package com.example.spendwise.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spendwise.R;


import com.example.spendwise.activities.HomeActivity;
import com.example.spendwise.activities.RegisterActivity;
import com.example.spendwise.activities.ResetPassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginService {

    public static void login(ProgressDialog progressDialog,
                             FirebaseAuth firebaseAuth){

        Activity loginActivity =new AppCompatActivity(R.layout.activity_login) ;

        Button loginButton=loginActivity.findViewById(R.id.btn_login);
        TextView forgotPasswordText=loginActivity.findViewById(R.id.forgot_password);
        TextView SignupText=loginActivity.findViewById(R.id.signup);

        EditText EntredEmail= loginActivity.findViewById(com.example.spendwise.R.id.email_login);
        EditText EntredPassword=loginActivity.findViewById(R.id.password_login);


        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email=EntredEmail.getText().toString().trim();
                String password=EntredPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    EntredEmail.setError("Email cannot be empty. Please enter a valid Email id");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    EntredPassword.setError("Password cannot be empty.");
                    return;
                }

                progressDialog.setMessage("Processing");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(loginActivity.getApplicationContext(), "Login Successful!",Toast.LENGTH_SHORT).show();
                            loginActivity.startActivity(new Intent((loginActivity.getApplicationContext()), HomeActivity.class));
                            loginActivity.finish();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(loginActivity.getApplicationContext(), "Login Failed!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActivity.startActivity(new Intent(loginActivity.getApplicationContext(), ResetPassword.class));

            }
        });

        SignupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActivity.startActivity(new Intent(loginActivity.getApplicationContext(), RegisterActivity.class));
            }
        });


    }


}
