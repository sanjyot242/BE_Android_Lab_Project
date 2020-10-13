package com.example.unamedappproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class RecyclerViewAdapterHome extends RecyclerView.Adapter<RecyclerViewAdapterHome.MyViewHolder> {
    Context mContext;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static String currentPhotoPath;
    private String[] description;
    private String[] title;
    private String[] owner;
    static final int REQUEST_TAKE_PHOTO = 2;

    public RecyclerViewAdapterHome(Context mContext,String description[],String tittle[],String owner[]){
        this.mContext = mContext;
        this.description =  description;
        this.title = tittle;
        this.owner= owner;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterHome.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item,parent,false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterHome.MyViewHolder holder, int position) {
        holder.description.setText(description[position]);
        holder.title_name.setText(title[position]);
        holder.upload.setOnClickListener(v -> {
            dispatchTakePictureIntent();
        });

    }

    @Override
    public int getItemCount() {
        return description.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title_name,description;
        private Button upload;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title_name = (TextView) itemView.findViewById(R.id.title_dataset);
            description = (TextView) itemView.findViewById(R.id.description);
            upload=(Button) itemView.findViewById(R.id.upload);

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                ((Activity)mContext).startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }




}
