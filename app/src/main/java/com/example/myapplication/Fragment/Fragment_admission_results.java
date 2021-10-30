package com.example.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.Notification.Data;
import com.example.myapplication.R;
import com.example.myapplication.Recylers.AdapterNotification;
import com.example.myapplication.Recylers.OnRecyclerviewItemClickListener;
import com.example.myapplication.Request_view;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.myapplication.Fragment.Fragment_requests.REQUEST_KEY;
import static com.example.myapplication.Fragment.Fragment_requests.REQUEST_KEYCOMPANY;
import static com.example.myapplication.Notification.MyFireBaseMessagingService.ENAPLE_CHAT;


public class Fragment_admission_results extends Fragment {
    private AdapterNotification adapter;
    private View contactView;
    private ArrayList<Data> data;
    private ProgressBar bar;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private FirebaseAuth auth;
    SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener stateListener;
    private ChildEventListener eventListener;
    public Fragment_admission_results() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        contactView = inflater.inflate(R.layout.fragment_admission_results, container, false);
        setHasOptionsMenu(true);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();
        reference = database.getReference("ClientApplication").child("referenceSaveNotification").child(user.getUid());
        recyclerView = contactView.findViewById(R.id.rv_admission_results);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) lm).setReverseLayout(true);
        ((LinearLayoutManager) lm).setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);
        bar = contactView.findViewById(R.id.progressBar_admission_results);
        swipeRefreshLayout = (SwipeRefreshLayout) contactView.findViewById(R.id.swipeRefreshLayout_admission);
        data = new ArrayList<>();


        adapter = new AdapterNotification(data, getContext(), new OnRecyclerviewItemClickListener() {
            @Override
            public void onItemClick(String carId, String companyId) {
                Intent intent = new Intent(getContext(), Request_view.class);
                intent.putExtra(REQUEST_KEY, carId);
                intent.putExtra(REQUEST_KEYCOMPANY, companyId);
                intent.putExtra( ENAPLE_CHAT ,"1");
                  startActivity(intent);
            }

            @Override
            public void onItemClick(String carId) {

            }
        });
        recyclerView.setAdapter(adapter);

        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    attachDatabaseReadListener();
                    //   Toast.makeText(MainActivity.this, "You're now signed in. Welcome to FriendlyChat.", Toast.LENGTH_SHORT).show();
                } else {

                }

            }
        };





        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                detachDatabaseListener();
                attachDatabaseReadListener();
            }
        });

        return contactView;
    }




    private void attachDatabaseReadListener() {
        if (eventListener == null) {
            adapter.clear();

            eventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Data s = snapshot.getValue(Data.class);


                        adapter.addJob(s);
                        recyclerView.setAdapter(adapter);
                        bar.setVisibility(View.GONE);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            reference.addChildEventListener(eventListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(stateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(stateListener);
        detachDatabaseListener();
        adapter.clear();
        recyclerView.clearAnimation();
        recyclerView.clearOnScrollListeners();
        recyclerView.clearFocus();
    }

    private void detachDatabaseListener() {
        if (eventListener != null) {
            reference.removeEventListener(eventListener);
            eventListener = null;

        }
    }



}