package com.gurukul;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gurukul.helpers.MyStaticClass;
import com.gurukul.modals.Students;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;


public class UpdateActivity extends AppCompatActivity {

    EditText std_name, std_class, std_rollno, std_password, std_confirm_password, std_phone;
    String std_name_txt, std_class_txt, std_rollno_txt, std_password_txt, std_confirm_pass_txt, std_phone_txt;
    Button update_btn;
    ImageView update_image;
    LinearLayout pic_layout;
    Uri imguri;
    StorageReference storageref;
    static String profilePicUrl;
    DatabaseReference reference;
    private static final int gpick = 1;
    static String pass;
    int backpress = 1;
    ProgressBar update_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        std_name = findViewById(R.id.update_name);
        std_class = findViewById(R.id.update_class);
        std_rollno = findViewById(R.id.update_roll_no);
        std_password = findViewById(R.id.update_password);
        std_confirm_password = findViewById(R.id.update_confirm_password);
        std_phone = findViewById(R.id.update_phone);
        update_btn = findViewById(R.id.update_btn);
        update_image = findViewById(R.id.update_profile_image);
        pic_layout = findViewById(R.id.update_profile_image_layout);
        update_bar = findViewById(R.id.progressBar);
        update_bar.setVisibility(View.INVISIBLE);
        Paper.init(this);


        std_name.setText(MyStaticClass.currentOnlineStudent.getNAME());
        std_class.setText(MyStaticClass.currentOnlineStudent.getCLASS());
        std_rollno.setText(MyStaticClass.currentOnlineStudent.getROLLNO());
        std_password.setText(MyStaticClass.currentOnlineStudent.getPASSWORD());
        std_confirm_password.setText(MyStaticClass.currentOnlineStudent.getPASSWORD());
        std_phone.setText(MyStaticClass.currentOnlineStudent.getPHONE());
        Picasso.get().load(MyStaticClass.currentOnlineStudent.getPICURL()).into(update_image);
        storageref = FirebaseStorage.getInstance().getReference().child("USER");
        reference = FirebaseDatabase.getInstance().getReference().child("CLASSES");

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                std_name_txt = std_name.getText().toString();
                std_phone_txt = std_phone.getText().toString();
                std_password_txt = std_password.getText().toString();
                std_confirm_pass_txt = std_confirm_password.getText().toString();
                std_rollno_txt = std_rollno.getText().toString();
                std_class_txt = std_class.getText().toString();
                if (check(std_name_txt, std_phone_txt, std_password_txt, std_confirm_pass_txt)) {
                    //if all are filled then here
                    if (!(std_phone_txt.length() == 10)) {
                        std_phone.setError("ENTER CORRECT PHONE");
                    } else {
                        if ((std_password_txt.equals(std_confirm_pass_txt))) {
                            update_bar.setVisibility(View.VISIBLE);
                            std_name.setEnabled(false);
                            std_phone.setEnabled(false);
                            std_confirm_password.setEnabled(false);
                            std_password.setEnabled(false);
                            pic_layout.setEnabled(false);
                            if (MyStaticClass.currentOnlineStudent.getPICURL().equals("https://firebasestorage.goog" +
                                    "leapis.com/v0/b/gurukul-f0593.appspot.com/o/USER%2Fstudent.png?alt=media&token" +
                                    "=b6196674-18d7-4899-9dab-a33eddab93ae")) {
                                Toast.makeText(UpdateActivity.this, "Please Add Profile Picture",
                                        Toast.LENGTH_SHORT).show();
                                pic_layout.setEnabled(true);
                                update_bar.setVisibility(View.INVISIBLE);
                            } else {
                                SavePicInDatabase();
                                update_bar.setVisibility(View.VISIBLE);
                            }

                        } else {
                            std_password.setText("");
                            std_confirm_password.setText("");
                            std_confirm_password.setError("BOTH PASSWORDS MUST BE SAME");
                        }
                    }
                }

            }
        });
        pic_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
    }

    private void SavePicInDatabase() {
        final StorageReference storageReference = storageref.child("CLASS" + MyStaticClass.currentOnlineStudent.getCLASS())
                .child(MyStaticClass.currentOnlineStudent.getROLLNO());
        if (imguri == null) {
            profilePicUrl = MyStaticClass.currentOnlineStudent.getPICURL();
            updateAccount();
        } else {
            storageReference.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profilePicUrl = String.valueOf(uri);
                            updateAccount();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateAccount() {
        Students students = new Students(std_name_txt, profilePicUrl, std_class_txt, std_rollno_txt,
                std_phone_txt, std_password_txt);
        reference.child("CLASS" + MyStaticClass.currentOnlineStudent.getCLASS()).child("ROLLNO")
                .child(MyStaticClass.currentOnlineStudent.getROLLNO()).setValue(students)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(UpdateActivity.this, "Account info Updated", Toast.LENGTH_SHORT).show();
                Paper.book().destroy();
                MyStaticClass.currentOnlineStudent = null;
                startActivity(new Intent(UpdateActivity.this, LoginActivity.class));
                finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateActivity.this, StudentHome.class));
                finish();
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
            update_image.setImageURI(imguri);
            MyStaticClass.currentOnlineStudent.setPICURL("okay");
        }
    }

    private boolean check(String std_name_txt, String std_phone_txt, String std_password_txt, String std_confirm_pass_txt) {

        Boolean result = false;
        int n = 0;
        if (std_name_txt.length() == 0) {
            std_name.setError("PLEASE ENTER NAME");
            n = 1;
        }
        if (std_password_txt.length() == 0) {
            std_password.setError("PLEASE ENTER PASSWORD");
            n = n + 1;
        }
        if (std_phone_txt.length() == 0) {
            std_phone.setError("PLEASE ENTER NUMBER");
            n = n + 1;
        }
        if (std_confirm_pass_txt.length() == 0) {
            std_confirm_password.setError("PLEASE ENTER PASSWORD AGAIN");
            n = n + 1;
        }
        if (n == 0) {
            result = true;
        }
        return result;
    }

    @Override
    public void onBackPressed() {

        if (backpress > 1) {
            startActivity(new Intent(this, StudentHome.class));
            finishAffinity();
        } else {
            backpress = (backpress + 1);
            Toast.makeText(getApplicationContext(), " Press Back again to go back ", Toast.LENGTH_SHORT).show();
        }
    }
}
