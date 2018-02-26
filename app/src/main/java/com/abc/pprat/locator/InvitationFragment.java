package com.abc.pprat.locator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class InvitationFragment extends Fragment {

    private EditText etInvitation1;
    private EditText etInvitation2;
    private EditText etInvitation3;
    private EditText etInvitation4;
    private Button btnSendInvitation;
    private SharedPreferences sharedPreferences;
    String code;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_invitation, container, false);

        sharedPreferences=getActivity().getSharedPreferences("RefLocator",Context.MODE_PRIVATE);
        code=sharedPreferences.getString("Code","");


        etInvitation1=(EditText) view.findViewById(R.id.etInvi1);
        etInvitation2=(EditText) view.findViewById(R.id.etInvi2);
        etInvitation3=(EditText) view.findViewById(R.id.etInvi3);
        etInvitation4=(EditText) view.findViewById(R.id.etInvi4);
        btnSendInvitation=(Button)view.findViewById(R.id.btnInvite);

        etInvitation1.setText(String.valueOf(code.charAt(0)));
        etInvitation2.setText(String.valueOf(code.charAt(1)));
        etInvitation3.setText(String.valueOf(code.charAt(2)));
        etInvitation4.setText(String.valueOf(code.charAt(3)));

//
//        for (int i = 0; i < code.length(); i++){
//            char c = s.charAt(i);
//            //Process char
//        }
        btnSendInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent=new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"Family Locator");
                sharingIntent.putExtra(Intent.EXTRA_TEXT,"Hey! Just found the top 1 location tracking app available. Download the app from https://play.google.com/apps/testing/com.abc.pprat.locator and then enter the invitation code   "+code +
                        "" +"  to join my group.");
                startActivity(sharingIntent.createChooser(sharingIntent,"Share Via"));
            }
        });

        return view;
    }




}
