package com.example.ikujetwende;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    TextInputLayout textInputLayout1,textInputLayout2,textInputLayout3,textInputLayout4,textInputLayout5;
    TextView textViewDate,textViewTime;
    Button btnSubmit;
    int tHour,tMinute;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        textInputLayout1=findViewById(R.id.inputActivity);
        textInputLayout2=findViewById(R.id.inputVolunteer);
        textInputLayout3=findViewById(R.id.inputSurname);
        textInputLayout4=findViewById(R.id.inputDescription);
        textInputLayout5=findViewById(R.id.inputLocation);
        textViewDate=findViewById(R.id.date);
        textViewTime=findViewById(R.id.time);
        btnSubmit=findViewById(R.id.submit);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Activity_Details");
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        String date=day+"/"+month+"/"+year;
                        textViewDate.setText(date);
                    }
                },year,month,day
                );
                datePickerDialog.show();
            }
        });

        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(AddActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                tHour=hourOfDay;
                                tMinute=minute;

                                Calendar calendar1=Calendar.getInstance();

                                calendar1.set(0,0,0,tHour,tMinute);

                                textViewTime.setText(DateFormat.format("hh:mm aa",calendar1));
                            }
                        },
                        12,0,false);

                timePickerDialog.updateTime(tHour,tMinute);
                timePickerDialog.show();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addActivities();
            }
        });
    }

    private void addActivities() {
        String act=textInputLayout1.getEditText().getText().toString().trim();
        String firstname=textInputLayout2.getEditText().getText().toString().trim();
        String surname=textInputLayout3.getEditText().getText().toString().trim();
        String description=textInputLayout4.getEditText().getText().toString().trim();
        String location=textInputLayout5.getEditText().getText().toString().trim();
        String date=textViewDate.getText().toString().trim();
        String time=textViewTime.getText().toString().trim();

        if(act.isEmpty()){
            textInputLayout1.setError("Activity Required");
            textInputLayout1.requestFocus();
        }else if(firstname.isEmpty()){
            textInputLayout2.setError("FirstName Required");
            textInputLayout2.requestFocus();
        }else if(surname.isEmpty()){
            textInputLayout3.setError("Surname Required");
            textInputLayout3.requestFocus();
        }else if(description.isEmpty()){
            textInputLayout4.setError("Description Required");
            textInputLayout4.requestFocus();
        }else if(location.isEmpty()){
            textInputLayout5.setError("Location Required");
            textInputLayout5.requestFocus();
        }else if(date.isEmpty()){
            textViewDate.setError("Date Required");
            textViewDate.requestFocus();
        }else if(time.isEmpty()){
            textViewTime.setError("Time Required");
            textViewTime.requestFocus();
        }else{
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            String message=act+"\n"+"Location :"+location+"\n"+"Date:"+date+"\n"+"Time:"+time+"\n"+"FName:"+firstname+"\n"+"LName:"+surname+"\n";
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityDetails details=new ActivityDetails(act,firstname,surname,description,location,date,time);

                            databaseReference.push().setValue(details);
                            Toast.makeText(AddActivity.this, "Info added successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getBaseContext(),MainActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getBaseContext(), "No activity added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
            AlertDialog alertDialog= builder.create();
            alertDialog.show();
        }
    }
}