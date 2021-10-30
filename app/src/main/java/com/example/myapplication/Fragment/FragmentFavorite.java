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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.JobsClass.Advertisement;
import com.example.myapplication.JobsClass.Favorite;
import com.example.myapplication.ViewJobActivity;
import com.example.myapplication.R;
import com.example.myapplication.Recylers.MyAdapter;
import com.example.myapplication.Recylers.OnRecyclerviewItemClickListener;
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

import static com.example.myapplication.MainActivity.JOBs_KEY;


public class FragmentFavorite extends Fragment {


    private FirebaseDatabase database;
    private DatabaseReference reference, referenceM;
    ProgressBar barr;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private RecyclerView rv;
    private MyAdapter adapter;
    private Toolbar toolbar;
    ArrayList<Advertisement> mJobs = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    String currentId;
    private View contactView;
    private ArrayList<String> favoriteId;
    // private ArrayList<Advertisement> mJobs;

    public FragmentFavorite() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contactView = inflater.inflate(R.layout.fragment_favorite, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        currentId = user.getUid();

        database = FirebaseDatabase.getInstance();
        referenceM = database.getReference("Main");
        rv = (RecyclerView) contactView.findViewById(R.id.recycler2);
        swipeRefreshLayout = (SwipeRefreshLayout) contactView.findViewById(R.id.swipeRefreshLayout);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) lm).setReverseLayout(true);
        ((LinearLayoutManager) lm).setStackFromEnd(true);
        rv.setLayoutManager(lm);
        rv.setHasFixedSize(true);
        //toolbar.setNavigationIcon(R.drawable.ic_orward);
//        toolbar=getActivity().findViewById(R.id.toolbar);


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
        mJobs.clear();
        rv.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getIdCard(new FirebaseCallback() {
                    @Override
                    public void onCallback(List<String> list) {


                        favoriteId = (ArrayList<String>) list;


                        referenceM.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                detach();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    Advertisement model = dataSnapshot.getValue(Advertisement.class);

                                    if (Comparison(model.getId()))

                                        mJobs.add(model);
                                    //adapter.addJob(model);
                                }
                                //  Collections.shuffle(mJobs, new Random(System.currentTimeMillis()));
                                adapter.notifyDataSetChanged();


                                favoriteId.clear();


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

        return contactView;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        barr = (ProgressBar) contactView.findViewById(R.id.progressBar_favorite);

        barr.setVisibility(View.VISIBLE);

        getIdCard(new FirebaseCallback() {
            @Override
            public void onCallback(List<String> list) {

                favoriteId = (ArrayList<String>) list;

                referenceM.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        detach();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Advertisement model = dataSnapshot.getValue(Advertisement.class);

                            if (Comparison(model.getId()))

                                mJobs.add(model);
                            //adapter.addJob(model);
                        }
                        adapter.notifyDataSetChanged();
                        favoriteId.clear();
                        Log.d("hamzasss", adapter.getItemCount() + "");


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

        for (int i = 0; i < favoriteId.size(); i++) {
            if (favoriteId.get(i) == id || favoriteId.get(i).equals(id))
                return true;
        }
        return false;
    }

    private void getIdCard(FirebaseCallback firebaseCallback) {
        favoriteId = new ArrayList<>();

        reference = database.getReference("ClientApplication").child("Favorite").child(currentId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    favoriteId.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Favorite favorite = dataSnapshot.getValue(Favorite.class);

                    favoriteId.add(favorite.getId());

                }
                firebaseCallback.onCallback(favoriteId);
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
            rv.clearAnimation();
            rv.clearOnScrollListeners();
            rv.clearFocus();
            mJobs.clear();
        }


    }
}