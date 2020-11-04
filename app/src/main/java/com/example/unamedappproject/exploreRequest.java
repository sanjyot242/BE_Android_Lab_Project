package com.example.unamedappproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.unamedappproject.RecyclerViewAdapterHome.currentVisitedDatasetName;

public class exploreRequest extends AppCompatActivity {
    private static final String TAG ="exploreRequest" ;
    private RecyclerView recyclerViewExplore;
    private ExploreRequestsAdapter imageAdapter;

    private ArrayList<Uri> downloadUrl;
    ArrayList<String> urls;
    private StorageReference mStorage;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static CollectionReference dbRef=db.collection("AllRequests");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_request);

        mStorage = FirebaseStorage.getInstance().getReference();

        recyclerViewExplore = findViewById(R.id.imageList);
        recyclerViewExplore.setHasFixedSize(true);
        recyclerViewExplore.setLayoutManager(new LinearLayoutManager(this));

        downloadUrl = new ArrayList<>();
        urls = new ArrayList<>();

        dbRef.document(currentVisitedDatasetName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.i(TAG, "onComplete: "+currentVisitedDatasetName);
                if (task.isSuccessful()) {
                    urls.clear();
                    DocumentSnapshot document = task.getResult();
                    urls= (ArrayList<String>) document.get("imageUrls");
                        imageAdapter = new ExploreRequestsAdapter(exploreRequest.this, urls);
                        recyclerViewExplore.setAdapter(imageAdapter);
                        Log.i(TAG, "onCreate: " + urls);

                }
            }
        });
        StorageReference listRef = mStorage.child("/DataSets/"+currentVisitedDatasetName);




//        listRef.listAll()
//                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
//                    @Override
//                    public void onSuccess(ListResult listResult) {
////                        for (StorageReference prefix : listResult.getPrefixes()) {
////                            // All the prefixes under listRef.
////                            // You may call listAll() recursively on them.
////
//                        for (StorageReference item : listResult.getItems()) {
//                            // All the items under listRef.
//                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    // Got the download URL for 'users/me/profile.png'
//                                    downloadUrl.add(uri);
//                                    imageAdapter = new ExploreRequestsAdapter(exploreRequest.this,downloadUrl);
//                                    recyclerViewExplore.setAdapter(imageAdapter);
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception exception) {
//                                    // Handle any errors
//                                }
//                            });
//                        }
//                        imageAdapter = new ExploreRequestsAdapter(exploreRequest.this,downloadUrl);
//                        recyclerViewExplore.setAdapter(imageAdapter);
//                        Log.i(TAG, "onCreate: "+downloadUrl);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Uh-oh, an error occurred!
//                    }
//                });
//
//
//
//
//
    }


}