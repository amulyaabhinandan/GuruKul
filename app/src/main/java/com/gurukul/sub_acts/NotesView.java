package com.gurukul.sub_acts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gurukul.R;
import com.gurukul.Recycle_Adapters.Documents_Adapter;
import com.gurukul.helpers.MyStaticClass;
import com.gurukul.modals.Documents;

import java.util.ArrayList;

public class NotesView extends AppCompatActivity {
    TextView subname;
    RecyclerView myrecycle;
    ArrayList<Documents> documents,docs2;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);
        subname=findViewById(R.id.subject_name_notes);
        subname.setText(MyStaticClass.subjectname);
        myrecycle=findViewById(R.id.notes_docs_layout);
        documents=new ArrayList<>();
        docs2=new ArrayList<>();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("NOTES").child("CLASS"+MyStaticClass.currentOnlineStudent.getCLASS()).child(MyStaticClass.subjectname);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Documents docs;
                if (dataSnapshot.getChildrenCount() == 0) {
                    myrecycle.setPadding(250,250,250,250);
                    myrecycle.setBackground(getDrawable(R.drawable.no_available_student));
                } else {
                    myrecycle.setBackground(null);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        docs = snapshot.getValue(Documents.class);
                        documents.add(docs);
                    }
                    for(int i=documents.size()-1;i>=0;i--)
                    {
                        docs2.add(documents.get(i));
                    }
                    Documents_Adapter student_adapter = new Documents_Adapter(NotesView.this,"NOTES", docs2);
                    myrecycle.setAdapter(student_adapter);
                    myrecycle.setLayoutManager(new LinearLayoutManager(NotesView.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}

