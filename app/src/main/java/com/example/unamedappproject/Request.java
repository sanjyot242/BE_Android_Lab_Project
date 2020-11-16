
package com.example.unamedappproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.CaseMap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.unamedappproject.MainActivity.Mdescription;
import static com.example.unamedappproject.MainActivity.Mtitle;
import static com.example.unamedappproject.MainActivity.c;
import static com.example.unamedappproject.MainActivity.dbRef;
import static com.example.unamedappproject.MainActivity.description;
import static com.example.unamedappproject.MainActivity.owner;
import static com.example.unamedappproject.MainActivity.title;
import static com.example.unamedappproject.RecyclerViewAdapterHome.UplodingDataSetName;
import static com.example.unamedappproject.SignUpActivity.mAuth;

public class Request extends AppCompatActivity {
    private EditText requestTitle,requestDescription;
    private TextView addBackground;
    private Button createRequest,pickImages;
    private GridView grid;
    private GridViewAdapter gridAdapter;

    private StorageReference mStorage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbRef=db.collection("user");
    private CollectionReference requestRef=db.collection("AllRequests");
    Map<String, Object> Requests = new HashMap<>();
    ArrayList<ImageItem> bitmaps = new ArrayList<ImageItem>();
    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    int up = 0;
    int k =0;
    Map<String, Object> image_data = new HashMap<>();
    String background_url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        mStorage= FirebaseStorage.getInstance().getReference();
        requestTitle = findViewById(R.id.input_title);
        requestDescription = findViewById(R.id.input_description);
        createRequest = findViewById(R.id.button);
        pickImages = findViewById(R.id.add_images);
        grid = (GridView) findViewById(R.id.gridView);
//        addBackground=findViewById(R.id.AddBackground);

        gridAdapter = new GridViewAdapter(this,R.layout.grid_item_layout,bitmaps);
        grid.setAdapter(gridAdapter);

        pickImages.setOnClickListener(v -> {
            if(ActivityCompat.checkSelfPermission(Request.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Request.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        110);
                return;
            }

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            intent.setType("image/*");
            startActivityForResult(intent,1);
        });

//        addBackground.setOnClickListener(v->{
//            if(ActivityCompat.checkSelfPermission(Request.this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE)
//                    !=PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(Request.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        110);
//                return;
//            }
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
//            intent.setType("image/*");
//            startActivityForResult(intent,2);
//        });




        createRequest.setOnClickListener(v -> {
            Log.i("TAG", "onCreate: "+mArrayUri);
            String Title = requestTitle.getText().toString();
            String Description = requestDescription.getText().toString();

            if(TextUtils.isEmpty(Title)||TextUtils.isEmpty(Description)){
                Toast.makeText(this, "All fields are Mandatory", Toast.LENGTH_SHORT).show();
            }else{
                c++;
                description.add(Description);
                title.add(Title);
                owner.add(FirebaseAuth.getInstance().getUid());
                Mtitle.add(Title);
                Mdescription.add(Description);
                fragmentLeft.recyclerViewAdapterLeft.notifyDataSetChanged();
//                owner.add(MainHostActivity.name);
//                title.add(Title);
//                description.add(Description);
//                FragmentHome.recyclerViewAdapter.notifyDataSetChanged();
                Requests.put("Title",Title);
                Requests.put("Description",Description);
                dbRef.document(mAuth.getUid()).collection("Request").document(Title).set(Requests).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Request.this, "Upload and created", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("TAG", "onFailure: "+e.getMessage());
                    }
                });
                Requests.put("Owner",FirebaseAuth.getInstance().getUid());
                requestRef.document(Title).set(Requests);

//                FragmentHome.description=new String[MainActivity.c];
//                FragmentHome.title=new String[MainActivity.c];
//                FragmentHome.owner=new String[MainActivity.c];
//
//                for(int i = 0,x=0 ; i <= MainActivity.c -1 ; i++){
//                    FragmentHome.description[x]=MainActivity.description.get(i);
//                    FragmentHome.title[x]=MainActivity.title.get(i);
//                    FragmentHome.owner[x]=MainActivity.owner.get(i);
//                    x++;
//                }
               // recyclerViewAdapter = new RecyclerViewAdapterHome(getApplicationContext(),FragmentHome.description,FragmentHome.title,FragmentHome.owner);
               // FragmentHome.recyclerViewAdapter.notifyDataSetChanged();



                while (up < mArrayUri.size()){
                    StorageReference filepath = FirebaseStorage.getInstance().getReference().child("DataSets").child(Title).child(mArrayUri.get(k).getLastPathSegment());
                    filepath.putFile(mArrayUri.get(k)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                           // Uri downloadURL = taskSnapshot.getUploadSessionUri();
                               filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    Log.i("TAG", "url"+url);
                                    //append this map to imageUrls
                                    image_data.put("img_url", url);
                                    image_data.put("verified", true);
                                    image_data.put("correct", true);
                                    //dbRef.document(UplodingDataSetName).update("imageUrls", FieldValue.arrayUnion(url));
                                    requestRef.document(Title).update("imageUrls", FieldValue.arrayUnion(image_data));
                                }
                            });
//                            Toast.makeText(Request.this, downloadURL.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    up++;
                    k++;

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK){

            ClipData clipData = data.getClipData();

            if(clipData != null){
                for(int i=0;i<clipData.getItemCount();i++){
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    mArrayUri.add(imageUri);
                    try{
                        InputStream is = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bitmaps.add(new ImageItem(bitmap));
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }
            else{
                Uri imageUri = data.getData();
                try{
                    InputStream is = getContentResolver().openInputStream(imageUri);

                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    bitmaps.add(new ImageItem(bitmap));

                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        }
        gridAdapter.notifyDataSetChanged();
        Log.i("TAG", "onActivityResult: "+bitmaps);


//        if(requestCode==2&&resultCode==RESULT_OK){
//            Uri imageUri = data.getData();
//            StorageReference filePath = mStorage.child("DataSets").child(requestTitle.getText().toString())
//                    .child(imageUri.getLastPathSegment());
//            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            background_url=uri.toString();
//                        }
//                    });
//                }
//            });
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainHostActivity.class).putExtra("From","MyRequest"));
    }
}