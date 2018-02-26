package com.abc.pprat.locator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GroupFragment extends Fragment {


    ListView listViewGroup;
    private SharedPreferences sharedPreferences;
    String userKey;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String[] name;
    String[] status;
    String[] code;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressDialog progress;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_group, container, false);

        sharedPreferences = getActivity().getSharedPreferences("RefLocator", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "RefEmail");
        userKey = email.split("@")[0];

        progress=new ProgressDialog(getContext());
        progress.show();
        progress.setMessage("Loading please wait...");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(getDetail(),getContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);
                progress.dismiss();
            }
        },4000);



        return rootView;
    }


    private ArrayList<PartnerDetail> getDetail() {

        final ArrayList<PartnerDetail> arrayList=new ArrayList<>();

        DatabaseReference myReference = firebaseDatabase.getReference("groups").child(userKey);
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String partnerCode = snapshot.getValue(String.class);

                    DatabaseReference partnerCodeReference = firebaseDatabase.getReference("Codes").child(partnerCode);
                    partnerCodeReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            PartnerDetail partner = dataSnapshot.getValue(PartnerDetail.class);
                            arrayList.add(partner);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return arrayList;

    }


}
