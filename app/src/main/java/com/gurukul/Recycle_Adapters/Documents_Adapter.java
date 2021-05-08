package com.gurukul.Recycle_Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gurukul.R;
import com.gurukul.modals.Documents;
import java.util.ArrayList;

public class Documents_Adapter extends RecyclerView.Adapter<Documents_Adapter.Documents_Holder> {
    Context context;
    String activityname;
    ArrayList<Documents> documents;

    public Documents_Adapter(Context context, String activityname, ArrayList<Documents> documents) {
        this.context = context;
        this.activityname = activityname;
        this.documents = documents;
    }

    @NonNull
    @Override
    public Documents_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.document_row,parent,false);
        return new Documents_Holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Documents_Adapter.Documents_Holder holder, final int position) {
        holder.topic.setText(documents.get(position).getTopic());
        holder.date.setText(documents.get(position).getDate());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityname.equals("LECTURE")) {
                    DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(documents.get(position).getDocumentUrl());

                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setTitle(documents.get(position).getTopic()+".mp4");
                    request.setDescription("Downloading");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setVisibleInDownloadsUi(true);

                    downloadmanager.enqueue(request);
                }
                else {
                    DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(documents.get(position).getDocumentUrl());

                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setTitle(documents.get(position).getTopic()+".pdf");
                    request.setDescription("Downloading");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setVisibleInDownloadsUi(true);

                    downloadmanager.enqueue(request);
                }
            }
        });
        if(activityname.equals("LECTURE"))
        {
            holder.document_iamge.setImageResource(R.drawable.video);
        }
        else if(activityname.equals("NOTES"))
        {
            holder.document_iamge.setImageResource(R.drawable.pdf);
        }
        else if(activityname.equals("EXAMPAPER"))
        {
            holder.document_iamge.setImageResource(R.drawable.pdf);
        }
        else if(activityname.equals("HOMEWORK"))
        {
            holder.document_iamge.setImageResource(R.drawable.pdf);
        }
    }



    @Override
    public int getItemCount() {
        return documents.size();
    }

    public class Documents_Holder extends RecyclerView.ViewHolder {
        TextView topic,date;
        ImageView document_iamge;
        LinearLayout layout;
        public Documents_Holder(@NonNull View itemView) {
            super(itemView);
            topic=itemView.findViewById(R.id.document_topic);
            date=itemView.findViewById(R.id.document_date);
            document_iamge=itemView.findViewById(R.id.document_image);
            layout=itemView.findViewById(R.id.docs_container);
        }
    }
}
