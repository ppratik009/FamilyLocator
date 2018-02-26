package com.abc.pprat.locator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class JoinFragment extends Fragment {


    private EditText etCode1;
    private EditText etCode2;
    private EditText etCode3;
    private EditText etCode4;
    private Button btnJoin;
    public final int size=1;

    private String partnerJoiningCode;
    private String partnerKey="Empty";
    private String userKey;
    private String userCode;

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference groupReference=firebaseDatabase.getReference("groups");
  //  private DatabaseReference codesReference=firebaseDatabase.getReference("Codes");

    private SharedPreferences sharedPreferences;
    private DatabaseReference userCodeReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_join, container, false);

        etCode1=(EditText) view.findViewById(R.id.etCode1);
        etCode2=(EditText) view.findViewById(R.id.etCode2);
        etCode3=(EditText) view.findViewById(R.id.etCode3);
        etCode4=(EditText) view.findViewById(R.id.etCode4);
        btnJoin=(Button)view.findViewById(R.id.btnJoin);

        sharedPreferences=getActivity().getSharedPreferences("RefLocator",Context.MODE_PRIVATE);
        String email=sharedPreferences.getString("Email","RefMail");
        userKey=email.split("@")[0];
        userCode=sharedPreferences.getString("Code","RefCode");

       // Toast.makeText(getContext(), "" + userKey+ " "+userCode, Toast.LENGTH_SHORT).show();


        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                partnerJoiningCode=etCode1.getText().toString()+etCode2.getText().toString()+etCode3.getText().toString()+etCode4.getText().toString();

                DatabaseReference codesReference=firebaseDatabase.getReference("Codes");
                codesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(partnerJoiningCode))
                        {
                            final DatabaseReference partnerCodeReference=firebaseDatabase.getReference("Codes").child(partnerJoiningCode).child("Key");
                            partnerCodeReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    partnerKey=dataSnapshot.getValue(String.class);
                                   // Toast.makeText(getContext(), ""+partnerKey, Toast.LENGTH_SHORT).show();

                                    DatabaseReference partnerReference=firebaseDatabase.getReference("groups").child(partnerKey);
                                    DatabaseReference userReference=firebaseDatabase.getReference("groups").child(userKey);

                                    userReference.child(partnerKey).setValue(partnerJoiningCode);
                                    partnerReference.child(userKey).setValue(userCode);


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                            alert.setTitle("Successful")
                                    .setMessage("You have successfully joined the group start tracking your partner location.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            etCode1.setText("");
                                            etCode2.setText("");
                                            etCode3.setText("");
                                            etCode4.setText("");
                                            btnJoin.setEnabled(false);

                                        }
                                    })
                                    .show();

                        }
                        else {
                            AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                            alert.setTitle("Operation Failed")
                                    .setMessage("You have entered the invalid joining code please try again.")
                                    .setNegativeButton("Retry",null)
                                    .show();                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });


        etCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etCode1.getText().toString().length()==size)     //size as per your requirement
                {
                    etCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count==0)
                {
                    etCode1.requestFocus();
                }else if(etCode2.getText().toString().length()==size)     //size as per your requirement
                {
                    etCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count==0)
                {
                    etCode2.requestFocus();
                }else if(etCode3.getText().toString().length()==size)     //size as per your requirement
                {
                    etCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count==0)
                {
                    btnJoin.setEnabled(false);
                    etCode3.requestFocus();
                    //joinCode=null;
                }else if(etCode4.getText().toString().length()==size)     //size as per your requirement
                {
                    btnJoin.setEnabled(true);
                    //joinCode=etCode1.getText().toString()+etCode2.getText().toString()+etCode3.getText().toString()+etCode4.getText().toString();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

//        view.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if( keyCode == KeyEvent.KEYCODE_BACK )
//                {
//
//                    etCode1.setText(null);
//                    etCode2.setText(null);
//                    etCode3.setText(null);
//                    etCode4.setText(null);
//                }
//                return false;
//            }
//        });

        return view;
    }


}
