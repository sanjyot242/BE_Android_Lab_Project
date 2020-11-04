package com.example.unamedappproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterLeft extends RecyclerView.Adapter<RecyclerViewAdapterLeft.MyViewHolder> {
    private ArrayList<String> description;
    private ArrayList<String> title;
    Context mContext;

    public RecyclerViewAdapterLeft(Context mContext, ArrayList<String> description, ArrayList<String> tittle ){
        this.mContext = mContext;
        this.description =  description;
        this.title = tittle;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item,parent,false);
        RecyclerViewAdapterLeft.MyViewHolder vHolder = new RecyclerViewAdapterLeft.MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.description.setText(description.get(position));
        holder.title_name.setText(title.get(position));

        holder.upload.setOnClickListener(v -> {
            RecyclerViewAdapterHome.currentVisitedDatasetName = holder.title_name.getText().toString().trim();
            Intent i = new Intent(mContext,ModerateImages.class);
            mContext.startActivity(i);
            Toast.makeText(mContext, "Open recycler view to display images ! ", Toast.LENGTH_SHORT).show();
            //will open recycler view to display the sample images !
//            currentVisitedDatasetName = holder.title_name.getText().toString().trim();
//            Intent i = new Intent(mContext,exploreRequest.class);
//            mContext.startActivity(i);
//            Toast.makeText(mContext, "Open recycler view to display sample images ! ", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return description.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title_name,description;
        private Button upload,explore;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            title_name = (TextView) itemView.findViewById(R.id.title_dataset);
            description = (TextView) itemView.findViewById(R.id.description);
            upload=(Button) itemView.findViewById(R.id.upload);

        }
    }
}
