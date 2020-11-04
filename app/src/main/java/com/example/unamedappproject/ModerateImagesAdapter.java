package com.example.unamedappproject;

import android.content.Context;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModerateImagesAdapter extends RecyclerView.Adapter<ModerateImagesAdapter.MyViewHolder>  {
    private Context mContext;
    private List<String> moderateImageUrls;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("AllRequests").document(RecyclerViewAdapterHome.currentVisitedDatasetName);
    public ModerateImagesAdapter(Context context,List<String> urls){
        mContext = context;
        moderateImageUrls = urls;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moderation_item,parent,false);
        ModerateImagesAdapter.MyViewHolder vHolder = new ModerateImagesAdapter.MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String currentUrl = moderateImageUrls.get(position);
        Picasso.get()
                .load(currentUrl)
                .into(holder.imageView);
        holder.delete.setOnClickListener(v -> {
            //updates.put(String.valueOf(position), FieldValue.delete());
            docRef.update("imageUrls",FieldValue.arrayRemove(moderateImageUrls.get(position)));
            moderateImageUrls.remove(position);
            notifyDataSetChanged();

        });

        holder.verify.setOnClickListener(v -> {
            Toast.makeText(mContext, "Image will e verified", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return moderateImageUrls.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private Button verify,delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageModerate);
            verify=(Button) itemView.findViewById(R.id.verifyImage);
            delete=(Button) itemView.findViewById(R.id.deleteImage);

        }
    }
}
