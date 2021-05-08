package com.gurukul;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gurukul.helpers.MyStaticClass;
import com.gurukul.modals.Students;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    Button login_btn;
    EditText Class_edittext,Roll_edittext,Password_edittext;
    TextView Register_textview,Forget_password_textview;
    int backpress=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Password_edittext=findViewById(R.id.password_login);
        Class_edittext=findViewById(R.id.class_login);
        Roll_edittext=findViewById(R.id.roll_login);
        Register_textview=findViewById(R.id.register_text);
        Forget_password_textview=findViewById(R.id.forget_text);
        Paper.init(this);
        login_btn=findViewById(R.id.school_verify_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_user();
            }
        });

        Register_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        Forget_password_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgetPassword.class));
            }
        });
    }

    private boolean check( String password_txt,String std_class_txt, String roll_no_txt) {
        Boolean result = false;
        int n = 0;

        if (password_txt.length() == 0) {
            Password_edittext.setError("PLEASE ENTER PASSWORD");
            n = n + 1;
        }
        if (std_class_txt.length() == 0) {
            Class_edittext.setError("PLEASE ENTER CLASS");
            n = n + 1;
        }
        if (roll_no_txt.length() == 0) {
            Roll_edittext.setError("PLEASE ENTER ROLL NO");
            n = n + 1;
        }

        if (n == 0) {
            result = true;
        }
        return result;
    }

    private void login_user() {
    String ClassNO=Class_edittext.getText().toString();
    String RollNo=Roll_edittext.getText().toString();
    String Password=Password_edittext.getText().toString();
    if(check(Password,ClassNO,RollNo))
    {
        Connect_to_database(ClassNO,RollNo,Password);
    }
    }

    private void Connect_to_database(final String ClassNO, final String RollNo, final String password) {
        final DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference().child("CLASSES").child("CLASS"+ClassNO).child("ROLLNO");
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(RollNo).exists())
                {
                   Students students=dataSnapshot.child(RollNo).getValue(Students.class);
                   if(students.getPASSWORD().equals(password))
                   {
                       MyStaticClass.currentOnlineStudent=students;
                       Paper.book().write(MyStaticClass.userRollNoKey,RollNo);
                       Paper.book().write(MyStaticClass.userClassKey,ClassNO);
                       Paper.book().write(MyStaticClass.userPasswordKey,password);
                       startActivity(new Intent(LoginActivity.this,StudentHome.class));
                       finishAffinity();
                   }
                   else {
                       Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                   }
                }
                else {
                    Toast.makeText(LoginActivity.this, "User Don't Exists", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }

    @Override
    public void onBackPressed() {

            if (backpress > 1) {
                finishAffinity();
            } else {
                backpress = (backpress + 1);
                Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();


        }
    }
}
