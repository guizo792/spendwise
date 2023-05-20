package com.example.spendwise.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spendwise.R;
import com.example.spendwise.activities.HomeActivity;
import com.example.spendwise.activities.LoginActivity;
import com.example.spendwise.activities.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterService {
    public static void signup(ProgressDialog mDialog,FirebaseAuth mAuth,Activity registerActivity){

        Button signupButton=registerActivity.findViewById(R.id.btn_signup);
        TextView LoginText=registerActivity.findViewById(R.id.login);

        EditText EntredEmail= registerActivity.findViewById(R.id.email_signup);
        EditText EntredPassword=registerActivity.findViewById(R.id.password_signup);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=EntredEmail.getText().toString().trim();
                String password=EntredPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    EntredEmail.setError("Email cannot be empty. Please enter a valid Email id");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    EntredPassword.setError("Password cannot be empty!");
                    return;
                }
                Log.i("val", email);
                Log.i("val", password);
                mDialog.setMessage("Please wait while we process your data...");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            mDialog.dismiss();
                            Toast.makeText(registerActivity.getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                            registerActivity.startActivity(new Intent(registerActivity.getApplicationContext(), HomeActivity.class));
                        }
                        else{
                            mDialog.dismiss();
                            Toast.makeText(registerActivity.getApplicationContext(), "Registration Failed!",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
        LoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerActivity.startActivity(new Intent(registerActivity.getApplicationContext(), LoginActivity.class));
            }
        });

    }
}
