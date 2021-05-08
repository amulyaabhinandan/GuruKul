package com.gurukul.sub_acts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gurukul.MainActivity;
import com.gurukul.R;
import com.gurukul.Recycle_Adapters.Documents_Adapter;
import com.gurukul.helpers.MyStaticClass;
import com.gurukul.modals.Documents;

import java.util.ArrayList;

public class LecturesView extends AppCompatActivity {
    TextView subname;
    RecyclerView myrecycle;
    ArrayList<Documents> documents, docs2;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures_view);
        subname = findViewById(R.id.subject_name_lecture);
        subname.setText(MyStaticClass.subjectname);
        myrecycle = findViewById(R.id.lecture_docs_layout);
        documents = new ArrayList<>();
        docs2 = new ArrayList<>();

        if (MyStaticClass.currentOnlineStudent == null) {
            startActivity(new Intent(LecturesView.this, MainActivity.class));
            finishAffinity();
        } else {


            databaseReference = FirebaseDatabase.getInstance().getReference().child("LECTURE").child("CLASS" + MyStaticClass.currentOnlineStudent.getCLASS()).child(MyStaticClass.subjectname);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Documents docs;
                    if (dataSnapshot.getChildrenCount() == 0) {
                        myrecycle.setPadding(250, 250, 250, 250);
                        myrecycle.setBackground(getDrawable(R.drawable.no_available_student));
                    } else {
                        myrecycle.setBackground(null);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            docs = snapshot.getValue(Documents.class);
                            documents.add(docs);
                        }
                        for (int i = documents.size() - 1; i >= 0; i--) {
                            docs2.add(documents.get(i));
                        }
                        Documents_Adapter student_adapter = new Documents_Adapter(LecturesView.this, "LECTURE", docs2);
                        myrecycle.setAdapter(student_adapter);
                        myrecycle.setLayoutManager(new LinearLayoutManager(LecturesView.this));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
}