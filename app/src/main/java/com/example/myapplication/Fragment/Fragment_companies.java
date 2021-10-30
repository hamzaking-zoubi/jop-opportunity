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

import com.example.myapplication.JobsClass.Advertisement;
import com.example.myapplication.ViewJobActivity;
import com.example.myapplication.R;
import com.example.myapplication.Recylers.MyAdapter;
import com.example.myapplication.Recylers.OnRecyclerviewItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.myapplication.MainActivity.JOBs_KEY;


public class Fragment_companies extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private View contactView;
    private ArrayList<Advertisement> mJobs;
    private ProgressBar bar;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener stateListener;
    private ChildEventListener eventListener;
    SwipeRefreshLayout swipeRefreshLayout;

    public Fragment_companies() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactView = inflater.inflate(R.layout.fragment_companies, container, false);
        setHasOptionsMenu(true);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Main");
        recyclerView = contactView.findViewById(R.id.rv_copanies);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) lm).setReverseLayout(true);
        ((LinearLayoutManager) lm).setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);
        bar = contactView.findViewById(R.id.progressBar_companyes);
        firebaseAuth = FirebaseAuth.getInstance();
        mJobs = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) contactView.findViewById(R.id.swipeRefreshLayout_company);
      //  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
       /// recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyAdapter(mJobs, getContext(), new OnRecyclerviewItemClickListener() {
            @Override
            public void onItemClick(String carId, String companyId) {
                Intent intent = new Intent(getContext(), ViewJobActivity.class);
                intent.putExtra(JOBs_KEY, carId);

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
                    Advertisement s = snapshot.getValue(Advertisement.class);

                    if (s.getCategory() == "companies" || s.getCategory().equals("companies")) {
                        adapter.addJob(s);
                        recyclerView.setAdapter(adapter);
                        bar.setVisibility(View.GONE);
                    }
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