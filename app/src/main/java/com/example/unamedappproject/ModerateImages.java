package com.example.unamedappproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Map;

import static com.example.unamedappproject.MainActivity.mAuth;
import static com.example.unamedappproject.RecyclerViewAdapterHome.currentVisitedDatasetName;

public class ModerateImages extends AppCompatActivity {
    RecyclerView recyclerViewModerateImage;
    public static ArrayList<Map> moderateImageUrls;
    private ModerateImagesAdapter moderateImagesAdapter;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private CollectionReference dbRef=db.collection("users").document(mAuth.getUid())
//            .collection("Request");
public static CollectionReference dbRef=db.collection("AllRequests");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderate_images);

        recyclerViewModerateImage = findViewById(R.id.moderateImageRecyclerView);
        recyclerViewModerateImage.setHasFixedSize(true);
        recyclerViewModerateImage.setLayoutManager(new LinearLayoutManager(this));
        moderateImageUrls = new ArrayList<Map>();
        //Log.i("TAG", "onCreate: ");


        dbRef.document(currentVisitedDatasetName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.i("TAG", "onComplete: "+currentVisitedDatasetName);
                if (task.isSuccessful()) {
                    moderateImageUrls.clear();
                    DocumentSnapshot document = task.getResult();
                    moderateImageUrls= (ArrayList<Map>) document.get("imageUrls");
                    Log.e("TAG", "onComplete: "+moderateImageUrls);
                    moderateImagesAdapter = new ModerateImagesAdapter(ModerateImages.this, moderateImageUrls);
                    recyclerViewModerateImage.setAdapter(moderateImagesAdapter);
                }else{
                    Toast.makeText(ModerateImages.this, "UnsuccessFull", Toast.LENGTH_SHORT).show();
                }
            }
        });




      //  Log.i("TAG", "onCreate: "+dbRef.document(currentVisitedDatasetName).get(Source.valueOf("imageUrls")));




//        dbRef.document(currentVisitedDatasetName).collection("ImageStatus")
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.i("TAG!","" +document.getId());
//                    }
//                }
//            }
////        } {
////            @Override
////            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                Log.i("TAG", "onComplete: "+currentVisitedDatasetName);
////                if (task.isSuccessful()) {
////                    moderateImageUrls.clear();
////                    DocumentSnapshot document = task.getResult();
////                    moderateImageUrls= (ArrayList<String>) document.get("imageUrls");
////                    moderateImagesAdapter = new ModerateImagesAdapter(ModerateImages.this, moderateImageUrls);
////                    recyclerViewModerateImage.setAdapter(moderateImagesAdapter);
////                    Log.i("TAG", "onCreate: " + moderateImageUrls);
////
////                }
////            }
//        });
    }
}
