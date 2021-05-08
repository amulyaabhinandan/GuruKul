package com.gurukul;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.gurukul.Cards.HomeworkActivity;
import com.gurukul.Cards.LecturesActivity;
import com.gurukul.Cards.NotesActivity;
import com.gurukul.Cards.QuestionsActivity;
import com.gurukul.helpers.MyStaticClass;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;


public class StudentHome extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    int backpress=1;

    Button logout,yes,no,update;
    ActionBarDrawerToggle drawerToggle;

    //cards
    CardView homework_card,notes_card,lecture_card,question_card;
    //navigation
    TextView StdName,StdRoll,StdClass,StdPhone,name_home,class_home,roll_home;

    ImageView pic_navi,pic_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        drawerLayout=findViewById(R.id.navi);
        navigationView=findViewById(R.id.navigation_student);
        toolbar=findViewById(R.id.toolbar);
        name_home=findViewById(R.id.std_name_home);
        class_home=findViewById(R.id.class_home);
        roll_home=findViewById(R.id.roll_home);
        pic_home=findViewById(R.id.profile_image);



        name_home.setText(MyStaticClass.currentOnlineStudent.getNAME());
        class_home.setText(MyStaticClass.currentOnlineStudent.getCLASS());
        roll_home.setText(MyStaticClass.currentOnlineStudent.getROLLNO());
        Picasso.get().load(MyStaticClass.currentOnlineStudent.getPICURL()).into(pic_home);


        //cards declare
        homework_card=findViewById(R.id.homework);
        notes_card=findViewById(R.id.notes);
        question_card=findViewById(R.id.question_paper);
        lecture_card=findViewById(R.id.lectures);


        //cards click



        homework_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentHome.this, HomeworkActivity.class);
                backpress=1;
                startActivity(intent);
            }
        });
        notes_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentHome.this, NotesActivity.class);
                backpress=1;
                startActivity(intent);
            }
        });
        lecture_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentHome.this, LecturesActivity.class);
                backpress=1;
                startActivity(intent);
            }
        });
        question_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentHome.this, QuestionsActivity.class);
                backpress=1;
                startActivity(intent);
            }
        });




        //toolbar
        setSupportActionBar(toolbar);
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        View view= navigationView.getHeaderView(0);
        StdName=view.findViewById(R.id.stdName);
        StdClass=view.findViewById(R.id.stdClass);
        StdRoll=view.findViewById(R.id.stdRollNo);
        StdPhone=view.findViewById(R.id.stdphone);
        pic_navi=view.findViewById(R.id.profile_image_navigation);



        //toolbar values
        StdName.setText(MyStaticClass.currentOnlineStudent.getNAME());
        StdClass.setText(MyStaticClass.currentOnlineStudent.getCLASS());
        StdRoll.setText(MyStaticClass.currentOnlineStudent.getROLLNO());
        StdPhone.setText(MyStaticClass.currentOnlineStudent.getPHONE());
        Picasso.get().load(MyStaticClass.currentOnlineStudent.getPICURL()).into(pic_navi);



        //buttons in navigation bar
        logout=view.findViewById(R.id.logout_user);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(StudentHome.this); // Context, this, etc.
                dialog.setContentView(R.layout.logout_check_dialog);
                dialog.show();
                yes=dialog.findViewById(R.id.custom_dialog_yes);
                no=dialog.findViewById(R.id.custom_dialog_no);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Paper.book().destroy();
                        MyStaticClass.currentOnlineStudent=null;
                        Intent intent=new Intent(StudentHome.this,LoginActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        update=view.findViewById(R.id.update_user);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentHome.this,UpdateActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            this.drawerLayout.closeDrawer(GravityCompat.START);
            backpress=1;
        }
        else {
        if (backpress>1)
        {
            finishAffinity();
        }
        else
        {
            backpress = (backpress + 1);
            Toast.makeText(getApplicationContext(), " Press Back again to Exit.", Toast.LENGTH_SHORT).show();
        }
        }
    }
}
