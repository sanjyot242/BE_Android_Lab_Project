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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.unamedappproject.RecyclerViewAdapterHome.currentVisitedDatasetName;

public class exploreRequest extends AppCompatActivity {
    private static final String TAG ="exploreRequest" ;
    private RecyclerView recyclerViewExplore;
    private ExploreRequestsAdapter imageAdapter;

    private ArrayList<String> downloadUrl;
    ArrayList<Map> urls;
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
        urls = new ArrayList<Map>();

        dbRef.document(currentVisitedDatasetName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                urls.clear();
                Log.i(TAG, "onComplete: "+currentVisitedDatasetName);
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.i(TAG, "document:" + document.toString());
                    urls = (ArrayList<Map>) document.get("imageUrls");

                    if (urls != null) {
                        Log.i("gathered URLS:", urls.toString());

                        int regionIndex = 1;
                        //logic to only pass url withboth true
                        for (Map region : urls) {

                            System.out.println("\nPart of list - " + regionIndex);
                            System.out.println("============================"
                                    + "======================");

                            // get entrySet() into Set
                            Set<String> setOfIndianStates = region.keySet();

                            // Collection Iterator
                            Iterator<String> iterator =
                                    setOfIndianStates.iterator();

                            // iterate using while-loop after getting Iterator
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                System.out.println(key
                                        + "\t " + region.get(key));
                                if (key == "correct" && region.get(key) == "true") {
                                    downloadUrl.add(String.valueOf(region.get("img_url")));
                                }
                            }

                            // increment region index by 1
                            regionIndex++;
                        }

                        Log.i(TAG, "onComplete: " + downloadUrl);


                        for (int i = 0; i <= urls.size() - 1; i++) {
                            if (urls.get(i).get("correct").toString() != "true") {
                                Log.i(TAG, "onComplete: " + urls.get(i).get("correct").toString());
                                urls.remove(i);
                            }

                            Log.i(TAG, "onComplete: " + i);
                        }

                        if (urls != null) {
                            Log.i("gathered URLS:", urls.toString());
                            imageAdapter = new ExploreRequestsAdapter(exploreRequest.this, urls);
                            recyclerViewExplore.setAdapter(imageAdapter);
                            Log.i(TAG, "onCreate: " + urls);

                        } else {
                            Toast.makeText(exploreRequest.this, "Empty ImageSet", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(exploreRequest.this, "Images are not Verified !!!!", Toast.LENGTH_SHORT).show();
                    }
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