package com.example.unamedappproject;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;
import java.util.List;

public class ExploreRequestsAdapter extends RecyclerView.Adapter<ExploreRequestsAdapter.ImageViewHolder>{
    private Context mContext;
    private List<Uri> downloadUrl;

    public ExploreRequestsAdapter(Context context,List<Uri> urls){
        mContext = context;
        downloadUrl = urls;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_imageview,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri currentUrl = downloadUrl.get(position);
        Picasso.get()
                .load(String.valueOf(currentUrl))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
       return downloadUrl.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_upload);
        }
    }
}
