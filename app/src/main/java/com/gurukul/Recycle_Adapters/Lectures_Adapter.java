package com.gurukul.Recycle_Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gurukul.Cards.LecturesActivity;
import com.gurukul.R;
import com.gurukul.helpers.MyStaticClass;
import com.gurukul.modals.Subjects;
import com.gurukul.sub_acts.HomeworkView;
import com.gurukul.sub_acts.LecturesView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Lectures_Adapter extends RecyclerView.Adapter<Lectures_Adapter.Lectures_Holder> {
    Context context;
    ArrayList<Subjects> subjectsArrayList;

    public Lectures_Adapter(Context cnt,ArrayList<Subjects> subjects) {
        context=cnt;
        subjectsArrayList=subjects;
    }
    @NonNull
    @Override
    public Lectures_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.view_description,parent,false);
        return new Lectures_Adapter.Lectures_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Lectures_Holder holder, final int position) {
        holder.sub_code.setText(subjectsArrayList.get(position).getS_CODE());
        holder.sub_name.setText(subjectsArrayList.get(position).getS_NAME());
        Picasso.get().load(subjectsArrayList.get(position).getS_PIC()).into(holder.sub_image);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, LecturesView.class);
                MyStaticClass.subjectname=subjectsArrayList.get(position).getS_NAME();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectsArrayList.size();
    }

    public class Lectures_Holder extends RecyclerView.ViewHolder {
        TextView sub_name,sub_code;
        ImageView sub_image;
        LinearLayout linearLayout;
        public Lectures_Holder(@NonNull View itemView) {
            super(itemView);
            sub_image=itemView.findViewById(R.id.description_image);
            sub_code=itemView.findViewById(R.id.description_code);
            sub_name=itemView.findViewById(R.id.description_name);
            linearLayout=itemView.findViewById(R.id.description_layout);
        }
    }
}
