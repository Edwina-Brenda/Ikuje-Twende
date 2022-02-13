package com.example.ikujetwende;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText editTextEmail;
    Button btnChange;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        editTextEmail=findViewById(R.id.email);
        btnChange=findViewById(R.id.change);
        progressBar=findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);changePassword();
            }
        });
    }

    private void changePassword() {
        String email=editTextEmail.getText().toString().trim();
        if(email.isEmpty()){
            editTextEmail.setError("Email field required");
            editTextEmail.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Not a valid email");
            editTextEmail.requestFocus();
        }else{
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        editTextEmail.getText().clear();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ForgotPassword.this, "Check your email to reset password", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getBaseContext(),LoginActivity.class));
                    }else{
                        editTextEmail.getText().clear();
                        progressBar.setVisibility(View.GONE);
                        editTextEmail.requestFocus();
                        Toast.makeText(ForgotPassword.this, "Failed to send a link...try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}