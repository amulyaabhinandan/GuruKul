package com.gurukul;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gurukul.modals.Students;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    private static final int gpick = 1;
    EditText name, phone,std_class, confirm_password, password, roll_no;
    Button verify_btn, register_btn;
    LinearLayout add_image_layout;
    ImageView profile_pic;
    String name_txt, phone_txt, std_class_txt, confirm_password_txt, password_txt, roll_no_txt;
    Uri imguri;
    StorageReference storageref;
    static String profilePicUrl;
    DatabaseReference reference;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        add_image_layout = findViewById(R.id.profile_add_layout);
        profile_pic = findViewById(R.id.profile_pic_add);
        verify_btn = findViewById(R.id.verify);
        register_btn = findViewById(R.id.register);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        std_class = findViewById(R.id.std_class);
        confirm_password = findViewById(R.id.confirm_password);
        password = findViewById(R.id.reg_password);
        roll_no= findViewById(R.id.rollno);
        progressBar=findViewById(R.id.progressBar_register);
        progressBar.setVisibility(View.INVISIBLE);

        reference = FirebaseDatabase.getInstance().getReference().child("CLASSES");
        storageref = FirebaseStorage.getInstance().getReference("USER");
        register_btn.setVisibility(View.INVISIBLE);
        register_btn.setEnabled(false);
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_txt = name.getText().toString();
                phone_txt = phone.getText().toString();
                password_txt = password.getText().toString();
                confirm_password_txt = confirm_password.getText().toString();
                roll_no_txt = roll_no.getText().toString();
                std_class_txt = std_class.getText().toString();
                if (check(name_txt, password_txt, phone_txt, roll_no_txt, std_class_txt, confirm_password_txt)) {
                    //if all are filled then here
                    if (!(phone_txt.length() == 10)) {
                        phone.setError("ENTER CORRECT PHONE");
                    } else {
                        if ((password_txt.equals(confirm_password_txt))) {
                                name.setEnabled(false);
                                phone.setEnabled(false);
                                confirm_password.setEnabled(false);
                                password.setEnabled(false);
                                std_class.setEnabled(false);
                                roll_no.setEnabled(false);
                                add_image_layout.setEnabled(false);
                                verify_btn.setEnabled(false);
                                verify_btn.setVisibility(View.INVISIBLE);
                                checkAccount();
                                progressBar.setVisibility(View.VISIBLE);
                        } else {
                            password.setText("");
                            confirm_password.setText("");
                            confirm_password.setError("BOTH PASSWORDS MUST BE SAME");
                        }
                    }
                }

            }
        });
        add_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (imguri == null) {
                    profilePicUrl = "https://firebasestorage.googleapis.com/" +
                            "v0/b/gurukul-f0593.appspot.com/o/USER%2Fstudent.png?alt=media&token=b6196674-18d7-4899-9dab-a33eddab93ae";
                    AddUserData();
                } else {
                    SavePicInDatabase();
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
                if (dataSnapshot.child("CLASSES").child("CLASS"+std_class_txt).child("ROLLNO").child(roll_no_txt).exists()) {
                    Toast.makeText(RegisterActivity.this,"Account Already Exists,Redirecting to Login Page",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                }
                else {
                    if(dataSnapshot.child("CLASSES").child("CLASS"+std_class_txt).exists())
                    {
                        VerifyMyNumber("+91" + phone_txt);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                name.setEnabled(true);
                                phone.setEnabled(true);
                                confirm_password.setEnabled(true);
                                password.setEnabled(true);
                                std_class.setEnabled(true);
                                roll_no.setEnabled(true);
                                add_image_layout.setEnabled(true);
                                verify_btn.setEnabled(true);
                                verify_btn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(RegisterActivity.this
                                        , "Phone Number Must be in the Phone You are using to Register",
                                        Toast.LENGTH_LONG).show();
                            }
                        }, 30000); 
                    }
                    else {
                        name.setEnabled(true);
                        phone.setEnabled(true);
                        confirm_password.setEnabled(true);
                        password.setEnabled(true);
                        std_class.setEnabled(true);
                        roll_no.setEnabled(true);
                        add_image_layout.setEnabled(true);
                        verify_btn.setEnabled(true);
                        verify_btn.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this, "Class "+std_class_txt+ " Do not Exist,Ask your teacher to create One.", Toast.LENGTH_SHORT).show();
                    }
                   

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AddUserData() {
        Students students=new Students(name_txt,profilePicUrl,std_class_txt,roll_no_txt,phone_txt,password_txt);
        reference.child("CLASS"+std_class_txt).child("ROLLNO").child(roll_no_txt).setValue(students);
        Toast.makeText(this, "NOW U CAN LOGIN", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    private void SavePicInDatabase() {
        final StorageReference storageReference = storageref.child("CLASS"+std_class_txt).child(roll_no_txt);
        storageReference.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profilePicUrl = String.valueOf(uri);
                        AddUserData();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void OpenGallery() {
        Intent galaryint = new Intent();
        galaryint.setAction(Intent.ACTION_GET_CONTENT);
        galaryint.setType("image/*");
        startActivityForResult(Intent.createChooser(galaryint, "select"), gpick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == gpick && resultCode == RESULT_OK && data.getData() != null) {
            imguri = data.getData();
            profile_pic.setImageURI(imguri);
        }
    }

    private void VerifyMyNumber(final String s) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(s, 30L, TimeUnit.SECONDS, RegisterActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(RegisterActivity.this, "Phone Verified", Toast.LENGTH_SHORT).show();
                register_btn.setEnabled(true);
                register_btn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(RegisterActivity.this, "OTP Sent...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                name.setEnabled(true);
                phone.setEnabled(true);
                confirm_password.setEnabled(true);
                password.setEnabled(true);
                std_class.setEnabled(true);
                roll_no.setEnabled(true);
                add_image_layout.setEnabled(true);
                verify_btn.setEnabled(true);
                verify_btn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }



    private boolean check(String name_txt, String password_txt, String phone_txt, String std_class_txt, String roll_no_txt, String confirm_password_txt) {
        Boolean result = false;
        int n = 0;
        if (name_txt.length() == 0) {
            name.setError("PLEASE ENTER NAME");
            n = 1;
        }
        if (password_txt.length() == 0) {
            password.setError("PLEASE ENTER PASSWORD");
            n = n + 1;
        }
        if (phone_txt.length() == 0) {
            phone.setError("PLEASE ENTER NUMBER");
            n = n + 1;
        }
        if (std_class_txt.length() == 0) {
            std_class.setError("PLEASE ENTER CLASS");
            n = n + 1;
        }
        if (roll_no_txt.length() == 0) {
            roll_no.setError("PLEASE ENTER ROLL NO");
            n = n + 1;
        }
        if (confirm_password_txt.length() == 0) {
            confirm_password.setError("PLEASE ENTER PASSWORD AGAIN");
            n = n + 1;
        }
        if (n == 0) {
            result = true;
        }
        return result;
    }
}
