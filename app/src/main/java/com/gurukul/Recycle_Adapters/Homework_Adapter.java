package com.gurukul.Recycle_Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gurukul.R;
import com.gurukul.helpers.MyStaticClass;
import com.gurukul.modals.Subjects;
import com.gurukul.sub_acts.HomeworkView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Homework_Adapter extends RecyclerView.Adapter<Homework_Adapter.HomeworkHolder> {
    Context context;
    ArrayList<Subjects> subjectsArrayList;
    public Homework_Adapter(Context cnt,ArrayList<Subjects> subjects) {
       context=cnt;
       subjectsArrayList=subjects;
    }

    @NonNull
    @Override
    public HomeworkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.view_description,parent,false);
        return new HomeworkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeworkHolder holder, final int position) {
        holder.sub_code.setText(subjectsArrayList.get(position).getS_CODE());
        holder.sub_name.setText(subjectsArrayList.get(position).getS_NAME());
        Picasso.get().load(subjectsArrayList.get(position).getS_PIC()).into(holder.sub_image);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, HomeworkView.class);
                MyStaticClass.subjectname=subjectsArrayList.get(position).getS_NAME();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectsArrayList.size();
    }

    public class HomeworkHolder extends RecyclerView.ViewHolder{
        TextView sub_name,sub_code;
        ImageView sub_image;
        LinearLayout linearLayout;
        public HomeworkHolder(@NonNull View itemView) {
            super(itemView);
            sub_image=itemView.findViewById(R.id.description_image);
            sub_code=itemView.findViewById(R.id.description_code);
            sub_name=itemView.findViewById(R.id.description_name);
            linearLayout=itemView.findViewById(R.id.description_layout);
        }
    }
}
