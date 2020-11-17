package com.example.unamedappproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.example.unamedappproject.MainActivity.Mdescription;
import static com.example.unamedappproject.MainActivity.Mtitle;
import static com.example.unamedappproject.MainActivity.mAuth;
import static com.example.unamedappproject.RecyclerViewAdapterHome.currentVisitedDatasetName;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragmentLeft.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragmentLeft#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentLeft extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static RecyclerView recyclerView;
    public static RecyclerViewAdapterLeft recyclerViewAdapterLeft;
    static LinearLayoutManager mLayoutManager;
    Toolbar toolbar;
    private StorageReference mStorage;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
   //Toolbar toolbar;

    private OnFragmentInteractionListener mListener;

    public fragmentLeft() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentLeft.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentLeft newInstance(String param1, String param2) {
        fragmentLeft fragment = new fragmentLeft();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_left, container, false);
        toolbar = (Toolbar)v.findViewById(R.id.toolbar_myRequests);
        recyclerView=v.findViewById(R.id.moderateRecyclerView);
        mStorage = FirebaseStorage.getInstance().getReference();
        TextView textView = v.findViewById(R.id.textEmpty);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        if(Mdescription.size()<1){
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }else{
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerViewAdapterLeft = new RecyclerViewAdapterLeft(getContext(),Mdescription,Mtitle);
            recyclerView.setAdapter(recyclerViewAdapterLeft);
        }


        Log.i("TAG", "onCreateView: "+mAuth.getUid());




//        StorageReference listRef = mStorage.child("/DataSets/"+"New Request");
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
//                            item.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
//                                @Override
//                                public void onSuccess(StorageMetadata storageMetadata) {
//                                    Log.i("TAG", "onSuccess: "+storageMetadata.getCustomMetadata("Status"));
//                                    if(storageMetadata.getCustomMetadata("Status")=="Unverified"){
//                                       item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                           @Override
//                                           public void onSuccess(Uri uri) {
//                                               String Url = uri.toString();
//                                               forModeration.add(Url);
//                                               Log.i("TAG", "ArrayList"+forModeration);
//                                           }
//                                       });
//                                    }
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception exception) {
//                                    // Handle any errors
//                                }
//                            });
//                        }
////                        imageAdapter = new ExploreRequestsAdapter(exploreRequest.this,downloadUrl);
////                        recyclerViewExplore.setAdapter(imageAdapter);
////                        Log.i(TAG, "onCreate: "+downloadUrl);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
