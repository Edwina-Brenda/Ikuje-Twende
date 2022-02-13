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
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextUsername,editTextEmail,editTextPhoneNumber,editTextPassword,editTextCPassword;
    Button btnRegister;
    TextView textViewLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextUsername=findViewById(R.id.username);
        editTextEmail=findViewById(R.id.email);
        editTextPhoneNumber=findViewById(R.id.phone);
        editTextPassword=findViewById(R.id.password);
        editTextCPassword=findViewById(R.id.cpassword);
        btnRegister=findViewById(R.id.register);
        textViewLogin=findViewById(R.id.txtLogin);
        progressBar=findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                userRegister();
            }
        });
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),LoginActivity.class));
            }
        });
    }

    private void userRegister() {
        String username=editTextUsername.getText().toString().trim();
        String email=editTextEmail.getText().toString().trim();
        String phone=editTextPhoneNumber.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();
        String cpassword=editTextCPassword.getText().toString().trim();

        if(username.isEmpty()){
            editTextUsername.setError("Username is required");
            editTextUsername.requestFocus();
        }else if(email.isEmpty()){
            editTextEmail.setError("Email field is required");
            editTextEmail.requestFocus();
        }else if(phone.isEmpty()){
            editTextPhoneNumber.setError("Phone number field is required");
            editTextPhoneNumber.requestFocus();
        }else if(phone.length()<10 || phone.length()>13){
            editTextPhoneNumber.setError("Phone number too large");
            editTextPhoneNumber.requestFocus();
        }else if(password.isEmpty()){
            editTextPassword.setError("Password field is required");
            editTextPassword.requestFocus();
        }else if(cpassword.isEmpty()){
            editTextCPassword.setError("Confirm password field is required");
            editTextCPassword.requestFocus();
        }else if(!password.equals(cpassword)){
            editTextPassword.setError("Passwords don't match");
            editTextCPassword.setError("Passwords don't match");
            editTextPassword.requestFocus();
        }else if((password.length() < 6)) {
            editTextPassword.setError("Password too small");
            editTextPassword.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Not a valid email");
            editTextEmail.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user=new User(username,email,phone,password,cpassword);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    editTextUsername.getText().clear();
                                    editTextEmail.getText().clear();
                                    editTextPhoneNumber.getText().clear();
                                    editTextPassword.getText().clear();
                                    editTextCPassword.getText().clear();
                                    editTextUsername.requestFocus();
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                }else{
                                    editTextUsername.getText().clear();
                                    editTextEmail.getText().clear();
                                    editTextPhoneNumber.getText().clear();
                                    editTextPassword.getText().clear();
                                    editTextCPassword.getText().clear();
                                    editTextUsername.requestFocus();
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        editTextUsername.getText().clear();
                        editTextEmail.getText().clear();
                        editTextPhoneNumber.getText().clear();
                        editTextPassword.getText().clear();
                        editTextCPassword.getText().clear();
                        editTextUsername.requestFocus();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
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