package com.example.myapplication.JobsClass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Recylers.OnRecyclerviewItemClickListener;
import com.example.myapplication.Recylers.RecyclerAdapterFirebase;
import com.example.myapplication.ViewJobActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.example.myapplication.JobsClass.ProfileCompanyActivity.SHOW_ALL_KEY;
import static com.example.myapplication.MainActivity.JOBs_KEY;

public class ShowAllJobs extends AppCompatActivity {
    private RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference reference;
    RecyclerAdapterFirebase adapter;
    ProgressBar bar;
    String companyId;
private Advertisement d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showall_jobs);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Main");
        bar = findViewById(R.id.prog_all);
        recyclerView = findViewById(R.id.ShowallJobs);
        bar.setVisibility(View.VISIBLE);

        Intent startingIntent = getIntent();
        companyId = startingIntent.getStringExtra(SHOW_ALL_KEY);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (companyId != null) {
            Query sQuery = reference.orderByChild("idCompany").equalTo(companyId);
            FirebaseRecyclerOptions<Advertisement> options =
                    new FirebaseRecyclerOptions.Builder<Advertisement>()
                            .setQuery(sQuery, Advertisement.class).setLifecycleOwner(this)
                            .build();
            adapter = new RecyclerAdapterFirebase(options, bar, getBaseContext(), new OnRecyclerviewItemClickListener() {
                @Override
                public void onItemClick(String carId, String companyId) {

                }

                @Override
                public void onItemClick(String carId) {
                    Intent intent = new Intent(getBaseContext(), ViewJobActivity.class);
                    intent.putExtra(JOBs_KEY, carId);
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(adapter);

        }


    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}