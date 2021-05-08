package com.gurukul;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gurukul.helpers.MyStaticClass;
import com.gurukul.modals.Students;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    AnimationDrawable rocketAnimation;
    String classkey,roll,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        final ImageView rocketImage =findViewById(R.id.imageView);
        rocketImage.setBackgroundResource(R.drawable.loading);
        if(rocketImage!=null) {
            rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
            rocketAnimation.start();
            classkey = Paper.book().read(MyStaticClass.userClassKey);
            roll = Paper.book().read(MyStaticClass.userRollNoKey);
            password = Paper.book().read(MyStaticClass.userPasswordKey);
            if (TextUtils.isEmpty(classkey)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rocketAnimation.stop();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, rocketImage, "splash_animation");
                        startActivity(intent, optionsCompat.toBundle());
                        finishAffinity();
                    }
                }, 7500);
            }
            else {
                LoginUser();
            }
        }

    }

    private void LoginUser() {
        final DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference().child("CLASSES").child("CLASS"+classkey).child("ROLLNO");
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(roll).exists())
                {
                    Students students=dataSnapshot.child(roll).getValue(Students.class);
                    if(students.getPASSWORD().equals(password))
                    {
                        MyStaticClass.currentOnlineStudent=students;
                        Paper.book().write(MyStaticClass.userRollNoKey,roll);
                        Paper.book().write(MyStaticClass.userClassKey,classkey);
                        Paper.book().write(MyStaticClass.userPasswordKey,password);
                        startActivity(new Intent(MainActivity.this,StudentHome.class));
                        finishAffinity();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Password is Changed", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Can't find User", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }
}
