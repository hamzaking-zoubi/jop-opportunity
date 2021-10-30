package com.example.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.Cv.Cv;
import com.example.myapplication.JobsClass.Advertisement;
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


public class recommended extends Fragment {

    private DatabaseReference referenceM, referenceCv;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private ProgressBar bar;
    private MyAdapter adapter;
    private View contactView;
    SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<Advertisement> recom;
//    private String[] WorkTime = {"Full time", "Contract", "part time", "Free lancer", "Work from home"};
//    private String[] Salary = {"from 25000 to 50000", "from 50000 to 75000", "from 75000 to 100000", "mor than million"};
//    private String[] educationLevel = {"There is no", "secondary certificate", "College degree", "Institute Certificate", "Diploma", "Master degree", "Doctor's certificate"};
//    private String[] experienceYears = {"There is no", "1 years", "2 years", "3 years", "4 years", "5 years", "6 years", "More than 10 years"};

    public recommended() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contactView = inflater.inflate(R.layout.fragment_recommended, container, false);
        setHasOptionsMenu(true);
        bar = contactView.findViewById(R.id.progressBar_reco);
        recyclerView = contactView.findViewById(R.id.recommended_rvvv);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        referenceM = database.getReference("Main");
        referenceCv = database.getReference("CV-user").child(user.getUid());

        swipeRefreshLayout = (SwipeRefreshLayout) contactView.findViewById(R.id.swipeRefreshLayout_recomended);

        recom = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MyAdapter(recom, getContext(), new OnRecyclerviewItemClickListener() {
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getIdCard(new FirebaseCallback() {
                    @Override
                    public void onCallback(List<Advertisement> filterL) {
                        Log.d("filterL", filterL.size() + "");
                        recom = (ArrayList<Advertisement>) filterL;
                 //       filterL.clear();
                        adapter.setJobs(recom);


                        adapter.notifyDataSetChanged();

                        bar.setVisibility(ProgressBar.GONE);


                    }
                });
            }
        });


        return contactView;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {


        getIdCard(new FirebaseCallback() {
            @Override
            public void onCallback(List<Advertisement> filterL) {
                Log.d("filterL", filterL.size() + "");
                recom = (ArrayList<Advertisement>) filterL;
                adapter.setJobs(recom);
                adapter.notifyDataSetChanged();
                bar.setVisibility(ProgressBar.GONE);
            }
        });
    }


    boolean check(String names, String x) {
        if ( names.equals(x) ) {
            return true;
        }
        return false;
    }


    private void getIdCard(FirebaseCallback firebaseCallback) {

        List<Advertisement> filter = new ArrayList<>();

        referenceCv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Cv cv = snapshot.getValue(Cv.class);
                    referenceM.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                filter.clear();
                                for (DataSnapshot s : snapshot.getChildren()) {
                                    Advertisement jobs = s.getValue(Advertisement.class);
                                    Log.d("recomjob", jobs.getTitle());

                                    if (check(cv.getSsJobType(), jobs.getWorkTime()) && check(cv.getSsExperienceSalary(), jobs.getSalary()) &&
                                            check(cv.getSsEducationLevel(), jobs.getEducationLevel()) && check(cv.getSsExperience(), jobs.getExperienceYears())) {
                                        filter.add(jobs);

                                    }
                                }
                                firebaseCallback.onCallback(filter);


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "enter Cv", Toast.LENGTH_LONG);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("cancelll", error.getDetails());
            }
        });


    }

    private interface FirebaseCallback {
        void onCallback(List<Advertisement> filterL);


    }


}