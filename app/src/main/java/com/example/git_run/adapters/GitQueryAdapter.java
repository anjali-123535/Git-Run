package com.example.git_run.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.git_run.R;
import com.example.git_run.model.QueryModel;

import java.util.ArrayList;

public class GitQueryAdapter extends RecyclerView.Adapter<GitQueryAdapter.GitViewHolder> {
private Context mcontext;
String weburl=null;
ArrayList<QueryModel>list;
private GitQueryOnClickHandler mclick;
private  interface GitQueryOnClickHandler{
void onClick();
}

    public GitQueryAdapter(Context context,ArrayList<QueryModel>mlist) {
        mcontext = context;
        list=mlist;
    }

    @NonNull
    @Override
    public GitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false) ;
        return new GitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GitViewHolder holder, final int position) {
holder.search_text.setText(list.get(position).getSearch_text());
holder.author.setText(list.get(position).getAuthor());
holder.desc.setText(list.get(position).getDesc());
holder.project_name.setText(list.get(position).getProject_name());
holder.view.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        //Toast.makeText(mcontext,"clicked",Toast.LENGTH_SHORT).show();
        Uri weburl= Uri.parse(list.get(position).getWeb_url());
        Intent intent=new Intent(Intent.ACTION_VIEW,weburl);
        mcontext.startActivity(intent);
    }
});
    }

    @Override
    public int getItemCount() {
        if(list!=null)
         return list.size();
  return 0;
    }

    class GitViewHolder extends RecyclerView.ViewHolder {
        TextView desc;
        View view;
         TextView author;TextView search_text,project_name;
        ImageView share;
        public GitViewHolder(@NonNull View itemView) {
            super(itemView);
            desc=itemView.findViewById(R.id.desc);
            author=itemView.findViewById(R.id.author);
            project_name=itemView.findViewById(R.id.project_name);
                  view=itemView.findViewById(R.id.card_view);
                    search_text=itemView.findViewById(R.id.search_text);
            share=itemView.findViewById(R.id.share_image_card);
        }
    }
}
