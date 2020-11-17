package com.example.unamedappproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.media.MediaRecorder.VideoSource.CAMERA;
import static com.example.unamedappproject.RecyclerViewAdapterHome.CAMERA_REQUEST_CODE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentHome.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {
    View v;
    private static RecyclerView recyclerViewHome;
    public static RecyclerViewAdapterHome recyclerViewAdapter;
    private SwipeRefreshLayout refreshLayout;
    static LinearLayoutManager mLayoutManager;
    Toolbar toolbar;
    static TextView textView;
    public static String description[], title[], owner[];

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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
        v = inflater.inflate(R.layout.fragment_fragment_home, container, false);
        toolbar = (Toolbar)v.findViewById(R.id.toolbar_global);
         textView = v.findViewById(R.id.textEmpty);
     //   refreshLayout = v.findViewById(R.id.refreshrequest);
        recyclerViewHome=(RecyclerView) v.findViewById(R.id.global_Requests);
        //logic will come to hide recyler view when no requests are there !
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewHome.setLayoutManager(mLayoutManager);

       // AppCompatActivity activity = (AppCompatActivity) getActivity();


//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshLayout.setRefreshing(true);
//                recyclerViewAdapter = new RecyclerViewAdapterHome(getContext(),description,title,owner);
//                recyclerViewHome.setAdapter(recyclerViewAdapter);
//                refreshLayout.setRefreshing(false);
//            }
//        });

        setupRecyclerView(getContext());

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
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static void setupRecyclerView(Context context){


//        MainActivity.loadData();
        Log.i(TAG, "setupRecyclerView: ");
        description = new String[MainActivity.c];
        title = new String[MainActivity.c];
        owner = new String[MainActivity.c];


        for(int i = 0,x=0 ; i <= MainActivity.c -1 ; i++){
            description[x]=MainActivity.description.get(i);
            title[x]=MainActivity.title.get(i);
            owner[x]=MainActivity.owner.get(i);
            x++;
        }

        if(MainActivity.c!=0) {
            textView.setVisibility(View.GONE);
            recyclerViewAdapter = new RecyclerViewAdapterHome(context, description, title, owner);
            recyclerViewHome.setAdapter(recyclerViewAdapter);
        }else{
            recyclerViewHome.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
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