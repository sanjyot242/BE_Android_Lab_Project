package com.example.unamedappproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.startActivity;
import static com.example.unamedappproject.exploreRequest.dbRef;

public class RecyclerViewAdapterHome extends RecyclerView.Adapter<RecyclerViewAdapterHome.MyViewHolder> {
    Context mContext;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static String currentPhotoPath;
    public static String UplodingDataSetName;
    public static String DownloadDataSetName;
    public static String currentVisitedDatasetName;
    private String[] description;
    private String[] title;
    private String[] owner;
    ArrayList<Map> downloadUrls = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();

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
            UplodingDataSetName = holder.title_name.getText().toString();
            dispatchTakePictureIntent();
        });

        holder.download.setOnClickListener(v->{
            downloadUrls.clear();
            urls.clear();
            DownloadDataSetName = holder.title_name.getText().toString();
            dbRef.document(DownloadDataSetName)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        downloadUrls = (ArrayList<Map>) document.get("imageUrls");
                        //Log.i("TAG", "onComplete: "+downloadUrls);

                        if (downloadUrls != null) {
                           // Log.i("gathered URLS:", downloadUrls.toString());

                            for (int i = 0; i <= downloadUrls.size() - 1; i++) {
                                if (downloadUrls.get(i).get("correct").toString() == "true") {
                                    Log.i("TAG", "onComplete: " + downloadUrls.get(i).get("correct").toString());
                                   DownloadImage(downloadUrls.get(i).get("img_url").toString(),DownloadDataSetName);
                                }
                            }
//                            Log.i("TAG", "onComplete: " + downloadUrls);

                    }
                }
            };
        });
        });



        holder.explore.setOnClickListener(v -> {
            //will open recycler view to display the sample images !
            currentVisitedDatasetName = holder.title_name.getText().toString().trim();
            Intent i = new Intent(mContext,exploreRequest.class);
            mContext.startActivity(i);
            Toast.makeText(mContext, "Open recycler view to display sample images ! ", Toast.LENGTH_SHORT).show();
        });


    }

    @Override
    public int getItemCount() {
        return description.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title_name,description;
        private ConstraintLayout layout;
        private Button upload,explore,download;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);
            title_name = (TextView) itemView.findViewById(R.id.title_dataset);
            description = (TextView) itemView.findViewById(R.id.description);
            upload=(Button) itemView.findViewById(R.id.upload);
            download=(Button) itemView.findViewById(R.id.downloadSet);
            explore=(Button) itemView.findViewById(R.id.explore);

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


    public void DownloadImage(String url,String folder) {
        Log.i("TAG", "DownloadImage: "+"starting download");
        Picasso.get().load(url).into(new Target() {
            String fileUri;
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.i("TAG", "onBitmapLoaded: ");
                try {
                    File mydir = new File(Environment.getExternalStorageDirectory() + "/DCIM/"+folder);
                    if (!mydir.exists()) {
                        mydir.mkdirs();
                    }

                    fileUri =mydir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
                    FileOutputStream outputStream = new FileOutputStream(fileUri);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(mContext, "Image Downloaded", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

}
