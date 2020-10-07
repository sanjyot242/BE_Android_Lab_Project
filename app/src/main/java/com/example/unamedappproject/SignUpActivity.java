package com.example.unamedappproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG ="SignUp Activity" ;
    private FirebaseAuth mAuth;
    private EditText name , email , password , confirmPassword;
    private Button signUp;
    private TextView goToSignIn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbRef=db.collection("user");
    Map<String, Object> userx = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name=findViewById(R.id.user_Name);
        email=findViewById(R.id.user_Email);
        password=findViewById(R.id.password);
        confirmPassword=findViewById(R.id.confirm_password);
        signUp=findViewById(R.id.sign_Up);
        goToSignIn=findViewById(R.id.go_SignIn);

        mAuth = FirebaseAuth.getInstance();


        signUp.setOnClickListener(v -> {
            if(TextUtils.isEmpty(name.getText())||TextUtils.isEmpty(email.getText())||TextUtils.isEmpty(password.getText())||TextUtils.isEmpty(confirmPassword.getText())){
                Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            }else if (!password.getText().toString().equals(confirmPassword.getText().toString())){
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            }else {
                signUp(email.getText().toString(),password.getText().toString());
            }
        });





    }

    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, (com.google.android.gms.tasks.OnCompleteListener<AuthResult>) task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        userx.put("Name",name.getText().toString());
                        userx.put("Uid",mAuth.getCurrentUser().getUid());
                        userx.put("Email",email);
                        addUser(userx,mAuth.getCurrentUser().getUid());
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                    // ...
                });
    }

    private void addUser(Map<String, Object> userx,String docId) {
        dbRef.document(docId).set(userx).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot added with ID: " );
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    private void updateUI(FirebaseUser user) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }


}