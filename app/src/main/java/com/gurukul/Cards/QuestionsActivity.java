package com.gurukul.Cards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gurukul.R;
import com.gurukul.Recycle_Adapters.Notes_Adapter;
import com.gurukul.Recycle_Adapters.Questions_Adapter;
import com.gurukul.helpers.MyStaticClass;
import com.gurukul.modals.Subjects;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {
    ArrayList<String> subjects;
    ArrayList<Subjects> subjectsArrayList;
    DatabaseReference databaseReference_subjects, databaseReferencedetails_subjects;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        recyclerView=findViewById(R.id.Question_recycler);

        subjects = new ArrayList<>();
        subjectsArrayList = new ArrayList<>();
        databaseReferencedetails_subjects = FirebaseDatabase.getInstance().getReference().child("ALL_SUBJECTS");
        databaseReference_subjects = FirebaseDatabase.getInstance().getReference().child("CLASSES").child("CLASS" + MyStaticClass.currentOnlineStudent.getCLASS()).child("SUBJECTS");
        databaseReference_subjects.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    subjects.add(snapshot.getValue().toString());
                }
                databaseReferencedetails_subjects.addListenerForSingleValueEvent(new ValueEventListener() {
                    Subjects subs;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (int i = 0; i < subjects.size(); i++) {
                            subs = dataSnapshot.child(subjects.get(i)).getValue(Subjects.class);
                            subjectsArrayList.add(subs);
                        }
                        Questions_Adapter questions_adapter = new Questions_Adapter(QuestionsActivity.this, subjectsArrayList);
                        recyclerView.setAdapter(questions_adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(QuestionsActivity.this));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
