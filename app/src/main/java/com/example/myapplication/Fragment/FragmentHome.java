package com.example.myapplication.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.JobsClass.Advertisement;
import com.example.myapplication.ViewJobActivity;
import com.example.myapplication.R;
import com.example.myapplication.Recylers.OnRecyclerviewItemClickListener;
import com.example.myapplication.Recylers.RecyclerAdapterFirebase;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.myapplication.MainActivity.JOBs_KEY;
import static com.example.myapplication.MainActivity.JOBs_KEY1;


public class FragmentHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseDatabase database;

    private DatabaseReference reference;
    public static final int RC_SIGN_IN = 16;
    private String mUsername;


    private FirebaseAuth.AuthStateListener stateListener;
    private ChildEventListener eventListener;
    ProgressBar mDataProgressBar;
    private FirebaseAuth firebaseAuth;
    public static final int EDIT_CAR_REQUSTCOD = 1;
    RecyclerAdapterFirebase adapter;
    RecyclerView rv;
    private View contactView;
    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.search, menu);
        menu.findItem(R.menu.search);
       SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.main_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                search(query);
                return false;

            }


        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
               // adapter.stopListening();


               // adapter.startListening();

                return false;
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Main");
        firebaseAuth = FirebaseAuth.getInstance();
        mDataProgressBar = contactView.findViewById(R.id.progressBar_hom);
        rv = (RecyclerView) contactView.findViewById(R.id.recycler1);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) lm).setReverseLayout(true);
        ((LinearLayoutManager) lm).setStackFromEnd(true);
        rv.setLayoutManager(lm);

        return contactView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataProgressBar.setVisibility(View.VISIBLE);

        FirebaseRecyclerOptions<Advertisement> options= new FirebaseRecyclerOptions.Builder<Advertisement>()
                .setQuery(reference, Advertisement.class)
                .build();
        adapter = new RecyclerAdapterFirebase(options, mDataProgressBar, getContext(), new OnRecyclerviewItemClickListener() {
            @Override
            public void onItemClick(String carId,String companyId) {
                Intent intent = new Intent(getContext(), ViewJobActivity.class);
                intent.putExtra(JOBs_KEY, carId);
                intent.putExtra(JOBs_KEY1, companyId);

                startActivity(intent);
            }

            @Override
            public void onItemClick(String carId) {

            }
        });
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);

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


    public void search(String searchText) {

       // String pquery = searchText.toLowerCase();
      //  Query sQuery = reference.orderByChild("title").startAt(pquery).endAt(pquery + "\uf8ff");
        FirebaseRecyclerOptions<Advertisement> options =
                new FirebaseRecyclerOptions.Builder<Advertisement>()
                .setQuery(reference.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff"), Advertisement.class).setLifecycleOwner(this)
                .build();
        adapter = new RecyclerAdapterFirebase(options, mDataProgressBar, getContext(), new OnRecyclerviewItemClickListener() {
            @Override
            public void onItemClick(String carId,String companyId) {
                Intent intent = new Intent(getContext(), ViewJobActivity.class);
                intent.putExtra(JOBs_KEY, carId);
                startActivity(intent);
            }

            @Override
            public void onItemClick(String carId) {

            }
        });
        adapter.startListening();
        rv.setAdapter(adapter);
    }


}