package com.example.unamedappproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private EditText inputUsernme, inputPassword;
    private TextView goToSignup;
    private Button signIn;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    public static ArrayList<String> description=new ArrayList<String>(),title=new ArrayList<String>(),owner=new ArrayList<String>();
    public static int c = 0;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static CollectionReference dbRef=db.collection("AllRequests");
    static Boolean FromSignUp=false;

// Initialize Firebase Auth


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();

        inputUsernme = findViewById(R.id.username);
        inputPassword = findViewById(R.id.password);
        signIn = findViewById(R.id.signIn);
        goToSignup = findViewById(R.id.go_Signup);

        signIn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(inputPassword.getText()) || TextUtils.isEmpty(inputPassword.getText())) {
                Toast.makeText(this, "Please Fill all details", Toast.LENGTH_SHORT).show();
            } else {
                signIn(inputUsernme.getText().toString(), inputPassword.getText().toString());
            }
        });

        goToSignup.setOnClickListener(v -> {
            Intent i = new Intent(this,SignUpActivity.class);
            startActivity(i);
        });
    }

    private void signIn(String Username, String Password) {
//        if (Username.equals("Sanjyot") && Password.equals("123456")) {
//            startActivity(new Intent(this,MainHostActivity.class));
//            Toast.makeText(this, inputUsernme.getText() + "\n" + inputPassword.getText(), Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
//        }
        mAuth.signInWithEmailAndPassword(Username, Password)
                .addOnCompleteListener(this, (com.google.android.gms.tasks.OnCompleteListener<AuthResult>) task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        // ...
                    }


                    // ...
                });

    }

    private void updateUI(FirebaseUser user) {
        loadData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),MainHostActivity.class));
                finish();
            }
        }, 5000);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(!FromSignUp) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FromSignUp = false;
                updateUI(FirebaseAuth.getInstance().getCurrentUser());
            }
        }
    }

    public static void loadData() {
        owner.clear();
        description.clear();
        title.clear();
        c=0;

        dbRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot q : queryDocumentSnapshots){
                            AllRequests requests = q.toObject(AllRequests.class);
                            requests.setDocumentId(q.getId());
                            owner.add(requests.getOwner());
                            description.add(requests.getDescription());
                            title.add(requests.getTitle());

                            c++;
                            Log.i("TAG", "onSuccess: "+description);
                        }
                    }
                });
    }
}
