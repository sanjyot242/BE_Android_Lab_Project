package com.example.unamedappproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.unamedappproject.MainActivity.dbRef;
import static com.example.unamedappproject.MainActivity.dbRef1;
import static com.example.unamedappproject.RecyclerViewAdapterHome.CAMERA_REQUEST_CODE;
import static com.example.unamedappproject.RecyclerViewAdapterHome.UplodingDataSetName;
import static com.example.unamedappproject.RecyclerViewAdapterHome.currentPhotoPath;
import static com.example.unamedappproject.SignUpActivity.mAuth;

public class MainHostActivity extends AppCompatActivity
        implements fragmentLeft.OnFragmentInteractionListener, fragmentRight.OnFragmentInteractionListener {
    private static final String TAG = "MainActivity";
    public Fragment mainHostFragment;
    NavHostFragment navHostFragment;
    NavController navController;
    ViewPager mPager;
    PagerAdapter pagerAdapter;
    BottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;
    static String UID;
    private StorageReference mStorage;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    //public static CollectionReference dbRef=db.collection("AllRequests");
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    static String name;
    Compressor compressor;
    String FromWhere ="";
    Map<String, Object> ImageData = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_host);
        mAuth = FirebaseAuth.getInstance();

        compressor = new Compressor();
        // navHostFragment =
        // (NavHostFragment) getSupportFragmentManager()
        // .findFragmentById(R.id.fragNavHost);
        // navController = navHostFragment.getNavController();
        //
        Intent intentReload = getIntent();
        FromWhere=intentReload.getStringExtra("From");


        mStorage = FirebaseStorage.getInstance().getReference();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListner);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mPager.setAdapter(pagerAdapter);

        if (FromWhere != null) {
            if ("MyRequest".equals(FromWhere)) {
                mPager.setCurrentItem(3);
                bottomNavigationView.getMenu().findItem(R.id.nav_right).setChecked(true);
            }
        }








        DocumentReference docRef = db.collection("user").document(mAuth.getUid());
        docRef.get().addOnCompleteListener((com.google.android.gms.tasks.OnCompleteListener<DocumentSnapshot>) task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    name = (String) document.get("Name");
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });



        // NavigationUI.setupWithNavController(bottomNavigationView, navController);



        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.nav_left).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.nav_right).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.CAMERA }, MY_CAMERA_REQUEST_CODE);
        }
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    mPager.setCurrentItem(1);
                    break;
                case R.id.nav_left:
                    mPager.setCurrentItem(0);
                    break;
                case R.id.nav_right:
                    mPager.setCurrentItem(2);
                    break;
            }
            return true;
        }

    };

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0 || mPager.getCurrentItem() == 2) {
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
            mPager.setCurrentItem(1);
        } else {
            Intent sm = new Intent(Intent.ACTION_MAIN);
            sm.addCategory(Intent.CATEGORY_HOME);
            sm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(sm);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            File file = new File(currentPhotoPath);
            if (!compressor.compressfile(file, getApplicationContext())) {
                File cmpfile = new File(Compressor.compressedfile);
                Uri uri = Uri.fromFile(cmpfile);
                StorageReference filePath = mStorage.child("DataSets").child(UplodingDataSetName)
                        .child(uri.getLastPathSegment());
                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();

                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                ImageData.put("Status","Unverified");
                                ImageData.put("url",url);
                                dbRef.document(UplodingDataSetName).update("imageUrls", FieldValue.arrayUnion(url));
                                dbRef1.document(mAuth.getUid()).collection("Request").document(UplodingDataSetName)
                                        .collection("ImageStatus").add(ImageData);
                            }
                        });
                        Toast.makeText(MainHostActivity.this, "Image Upload Successfull", Toast.LENGTH_LONG).show();
                        Upload upload =  new Upload(taskSnapshot.getUploadSessionUri().toString());
                        //dbRef.document(UplodingDataSetName).update("imageUrls", FieldValue.arrayUnion(myUrl));
                        Log.i(TAG, "Bucket: "+ mStorage.child("DataSets").child(UplodingDataSetName));
                       // dbRef.document(UplodingDataSetName).update("imageUrl",mStorage.child("DataSets").child(UplodingDataSetName).toString());
                        // Create file metadata including the content type
                        StorageMetadata metadata = new StorageMetadata.Builder()
                                .setContentType("image/jpg")
                                .setCustomMetadata("Status", "Verified")
                                .build();

                        filePath.updateMetadata(metadata);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(MainHostActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0*snapshot.getBytesTransferred()/snapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });

            } else{

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
