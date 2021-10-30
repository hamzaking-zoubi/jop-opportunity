package com.example.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.myapplication.JobsClass.MyOrder;
import com.example.myapplication.R;
import com.example.myapplication.Recylers.MyAdapter;
import com.example.myapplication.Recylers.OnRecyclerviewItemClickListener;
import com.example.myapplication.Request_view;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class Fragment_requests extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference reference, referenceM;
    RecyclerView recyclerView;
    ProgressBar barr;
    private MyAdapter adapter;
    private View contactView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Advertisement> mJobs;
    private ArrayList<String> orderC;
    public static final String REQUEST_KEY = "request_key";
    public static final String REQUEST_KEYCOMPANY = "wefrequest_key";

    public Fragment_requests() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        setHasOptionsMenu(true);

        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("ClientApplication").child("MyOrder").child(user.getUid());
        referenceM = database.getReference().child("Main");
        contactView = inflater.inflate(R.layout.fragment_requests, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) contactView.findViewById(R.id.swipeRefreshLayout_request);
        recyclerView = (RecyclerView) contactView.findViewById(R.id.request_rv);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) lm).setReverseLayout(true);
        ((LinearLayoutManager) lm).setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);
        mJobs = new ArrayList<>();
        adapter = new MyAdapter(mJobs, getContext(), new OnRecyclerviewItemClickListener() {
            @Override
            public void onItemClick(String carId, String companyId) {
                Intent intent = new Intent(getContext(), Request_view.class);
                intent.putExtra(REQUEST_KEY, carId);
                intent.putExtra(REQUEST_KEYCOMPANY, companyId);
                startActivity(intent);
            }

            @Override
            public void onItemClick(String carId) {

            }
        });


mJobs.clear();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getIdCard(new FirebaseCallback() {
                    @Override
                    public void onCallback(List<String> list) {

                        getIdCard(new FirebaseCallback() {
                            @Override
                            public void onCallback(List<String> list) {



                                orderC = (ArrayList<String>) list;

                                referenceM.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        detach();
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                            Advertisement model = dataSnapshot.getValue(Advertisement.class);

                                            if (Comparison(model.getId()))

                                                mJobs.add(model);

                                        }
                                        adapter.notifyDataSetChanged();
                                        orderC.clear();
                                        barr.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.d("cancel", error.getDetails());
                                    }
                                });

                            }

                        });



                    }

                });

            }
        });

        return contactView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        barr = (ProgressBar) view.findViewById(R.id.progressBar_reqqq);
        getIdCard(new FirebaseCallback() {
            @Override
            public void onCallback(List<String> list) {



                orderC = (ArrayList<String>) list;

                referenceM.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        detach();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Advertisement model = dataSnapshot.getValue(Advertisement.class);

                            if (Comparison(model.getId()))

                                mJobs.add(model);

                        }
                        adapter.notifyDataSetChanged();
                        orderC.clear();
                        barr.setVisibility(View.GONE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("cancel", error.getDetails());
                    }
                });

            }

        });


    }

    public boolean Comparison(String id) {

        for (int i = 0; i < orderC.size(); i++) {
            if (orderC.get(i) == id || orderC.get(i).equals(id))
                return true;
        }
        return false;
    }

    private void getIdCard(FirebaseCallback firebaseCallback) {
        orderC = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    orderC.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        MyOrder order = dataSnapshot.getValue(MyOrder.class);

                        orderC.add(order.getId());

                    }
                firebaseCallback.onCallback(orderC);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("cancelll", error.getDetails());
            }
        });


    }

    private interface FirebaseCallback {
        void onCallback(List<String> list);


    }
    private void detach() {
        if (adapter != null) {
            adapter.clear();
            recyclerView.clearAnimation();
            recyclerView.clearOnScrollListeners();
            recyclerView.clearFocus();
            mJobs.clear();
        }


    }
}
