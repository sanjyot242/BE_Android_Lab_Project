
package com.example.unamedappproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.FirestoreClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static com.example.unamedappproject.SignUpActivity.mAuth;

public class Request extends AppCompatActivity {
    private EditText requestTitle,requestDescription;
    private Button createRequest;
    private ImageView sample1,sample2;

    private StorageReference mStorage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbRef=db.collection("user");
    private CollectionReference requestRef=db.collection("AllRequests");
    Map<String, Object> Requests = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        mStorage= FirebaseStorage.getInstance().getReference();
        requestTitle = findViewById(R.id.input_title);
        requestDescription = findViewById(R.id.input_description);
        createRequest = findViewById(R.id.button);




        createRequest.setOnClickListener(v -> {
            String Title = requestTitle.getText().toString();
            String Description = requestDescription.getText().toString();

            if(TextUtils.isEmpty(Title)||TextUtils.isEmpty(Description)){
                Toast.makeText(this, "All fields are Mandatory", Toast.LENGTH_SHORT).show();
            }else{
                Requests.put("Title",Title);
                Requests.put("Description",Description);
                dbRef.document(mAuth.getUid()).collection("Request").document("Request"+Title).set(Requests).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                Requests.put("Owner",MainHostActivity.name);
                requestRef.document(Title).set(Requests);



            }
        });
    }
}