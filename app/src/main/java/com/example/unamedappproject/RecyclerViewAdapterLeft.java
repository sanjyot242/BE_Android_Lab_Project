package com.example.unamedappproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static com.example.unamedappproject.MainActivity.Mdescription;
import static com.example.unamedappproject.MainActivity.Mtitle;
import static com.example.unamedappproject.MainActivity.loadData;
import static com.example.unamedappproject.MainActivity.mAuth;
import static com.example.unamedappproject.fragmentLeft.initRecylerView;
import static com.example.unamedappproject.fragmentLeft.recyclerViewAdapterLeft;

public class RecyclerViewAdapterLeft extends RecyclerView.Adapter<RecyclerViewAdapterLeft.MyViewHolder> {
    private ArrayList<String> description;
    private ArrayList<String> title;
    Context mContext;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference docRef = db.collection("AllRequests");
    private CollectionReference dbRef=db.collection("user");

    public RecyclerViewAdapterLeft(Context mContext, ArrayList<String> description, ArrayList<String> tittle ){
        this.mContext = mContext;
        this.description =  description;
        this.title = tittle;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moderate_item,parent,false);
        RecyclerViewAdapterLeft.MyViewHolder vHolder = new RecyclerViewAdapterLeft.MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.description.setText(description.get(position));
        holder.title_name.setText(title.get(position));

        holder.moderate.setOnClickListener(v -> {
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

        holder.delete.setOnClickListener(v->{
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(mContext);
            builder.setMessage("Do you want to exit ?");
            builder.setTitle("Alert !");
            builder.setCancelable(false);

            builder.setPositiveButton(
                    "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    delete(holder.title_name.getText().toString(),position);
                                }
                    });

            builder.setNegativeButton(
                    "No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    dialog.cancel();
                                }
                    });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();

            // Show the Alert Dialog box
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return description.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title_name,description;
        private Button moderate, delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            title_name = (TextView) itemView.findViewById(R.id.title_dataset);
            description = (TextView) itemView.findViewById(R.id.description);
            moderate =(Button) itemView.findViewById(R.id.moderate);
            delete=(Button) itemView.findViewById(R.id.delete);

        }
    }

    public void delete(String Dataset,int pos){
        try {
            docRef.document(Dataset)
                    .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    MainActivity.loadData();
                    Toast.makeText(mContext, "DataSet Successfully Deleted ", Toast.LENGTH_SHORT).show();
                    Mdescription.remove(pos);
                    Mtitle.remove(pos);
                    recyclerViewAdapterLeft.notifyDataSetChanged();
                    initRecylerView(mContext);
                }
            });
            dbRef.document(mAuth.getUid()).collection("Request")
                    .document(Dataset)
                    .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("TAG", "onSuccess: ");
                    initRecylerView(mContext);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
