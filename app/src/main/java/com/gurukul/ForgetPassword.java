package com.gurukul;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gurukul.modals.Students;

import java.util.concurrent.TimeUnit;

public class ForgetPassword extends AppCompatActivity {
    EditText std_class, std_rollno;
    Button send_otp;
    String std_class_text,std_rollno_text;
    ProgressBar forget_bar;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        std_class = findViewById(R.id.forget_class);
        std_rollno = findViewById(R.id.forget_roll);
        send_otp = findViewById(R.id.Send_Otp_btn);
        forget_bar=findViewById(R.id.forget_password_bar);
        forget_bar.setVisibility(View.INVISIBLE);
        reference = FirebaseDatabase.getInstance().getReference().child("CLASSES");
        
        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                std_class_text=std_class.getText().toString();
                std_rollno_text=std_rollno.getText().toString();
                if(std_class_text.length()!=0)
                {
                    if(std_rollno_text.length()!=0)
                    {
                        std_rollno.setEnabled(false);
                        std_class.setEnabled(false);
                        forget_bar.setVisibility(View.VISIBLE);
                        send_otp.setEnabled(false);
                        checkAccount();
                    }
                    else {
                        std_rollno.setError("Please Enter Roll");
                    }
                }
                else
                {
                    std_class.setError("Please Enter Class");
                }

            }
        });
    }

    private void checkAccount() {
            DatabaseReference rootref;
            rootref = FirebaseDatabase.getInstance().getReference();
            rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("CLASSES").child("CLASS"+std_class_text).child("ROLLNO").child(std_rollno_text).exists()) {
                        Students students=dataSnapshot.child("CLASSES").child("CLASS"+std_class_text).child("ROLLNO").child(std_rollno_text).getValue(Students.class);
                        VerifyNumber("+91"+students.getPHONE());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                std_rollno.setEnabled(true);
                                std_class.setEnabled(true);
                                forget_bar.setVisibility(View.INVISIBLE);
                                send_otp.setEnabled(true);
                                Toast.makeText(ForgetPassword.this, "Phone Number Must be in the Phone You are using to Register", Toast.LENGTH_SHORT).show();
                            }
                        }, 30000);
                    }
                    else {
                        Toast.makeText(ForgetPassword.this, "ACCOUNT NOT FOUND", Toast.LENGTH_SHORT).show();
                        std_rollno.setEnabled(true);
                        std_class.setEnabled(true);
                        forget_bar.setVisibility(View.INVISIBLE);
                        send_otp.setEnabled(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    private void VerifyNumber(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 30L, TimeUnit.SECONDS, ForgetPassword.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(ForgetPassword.this, "Verification Complete.", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ForgetPassword.this,ResetPassword.class);
                intent.putExtra("class",std_class_text);
                intent.putExtra("roll",std_rollno_text);
                startActivity(intent);
                finishAffinity();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(ForgetPassword.this, "CODE SENT !!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(ForgetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                std_rollno.setEnabled(true);
                std_class.setEnabled(true);
                forget_bar.setVisibility(View.INVISIBLE);
                send_otp.setEnabled(true);
            }
        });
    }
}
