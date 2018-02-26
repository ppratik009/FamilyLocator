package com.abc.pprat.locator;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import thebat.lib.validutil.ValidUtils;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,ChildEventListener{


    private TextInputEditText etEmail;
    private TextInputEditText etFullName;
    private TextInputEditText etPassword;
    private Button btnSignUp;
    private CheckBox cbTC;

    private ValidUtils validUtils;
    private FirebaseAuth mAuth;
    private int counter;

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference counterReference;
    private DatabaseReference databaseReference=firebaseDatabase.getReference("users");
    Map<String,String > userData;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeElements(); //Initialization



        mAuth=FirebaseAuth.getInstance();

        //For validation
        validUtils = new ValidUtils();

        cbTC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked())
                {
                    btnSignUp.setEnabled(true);
                }else {
                    btnSignUp.setEnabled(false);

                }
            }
        });

        counterReference=firebaseDatabase.getReference("Counter").child("count");
        counterReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String count=dataSnapshot.getValue(String.class);
                counter= Integer.parseInt(count);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // Elements initialization
    private void initializeElements() {
        etFullName = (TextInputEditText) findViewById(R.id.tietFullNameS);
        etEmail = (TextInputEditText) findViewById(R.id.tietEmails);
        etPassword = (TextInputEditText) findViewById(R.id.tietPasswords);
        btnSignUp = (Button) findViewById(R.id.btnSignUps);
        cbTC=(CheckBox)findViewById(R.id.cbTC);

        btnSignUp.setOnClickListener(this);
        databaseReference.addChildEventListener(this);
        progressDialog=new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnSignUps:
                signUp();
                break;

            default:
                Toast.makeText(this, "No Case", Toast.LENGTH_SHORT).show();
                return;
        }
    }

    private void signUp() {



        if (!validUtils.validateEditTexts(etFullName)) {
            etFullName.setError("Enter valid name.");
        }
        else if (!validUtils.validateEmail(etEmail)) {
            etEmail.setError("Enter valid email address.");
        } else if (!validUtils.validateEditTexts(etPassword)) {
            etPassword.setError("Enter valid password.(At least 6 characters)");
        } else if (!validUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Please connect your internet firs.", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setTitle("Processing");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            createNewUser(etEmail.getText().toString(),etPassword.getText().toString());
        }



    }

    private void createNewUser(String s, String s1) {
        final String email=s;
        String password=s1;

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {

                    Log.d("Create new user", "createUserWithEmail:success");

                    String userName=email.split("@")[0];
                    userData=new HashMap<String, String>();
                    userData.put("Name",etFullName.getText().toString());
                    userData.put("Email",etEmail.getText().toString());
                    userData.put("Status","1");
                    userData.put("Code", String.valueOf(counter));
                    //Adding value in db
                    databaseReference.child(userName).setValue(userData);
                    //Adding 1 in counter after assigning it
                    counterReference.setValue(String.valueOf(counter+1));
                    progressDialog.dismiss();

                    AlertDialog.Builder alert=new AlertDialog.Builder(SignUpActivity.this);
                    alert.setTitle("Successful")
                            .setMessage("Your account is created successfully you can login.Thanks!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseAuth.getInstance().signOut();
                                    finish();
                                }
                            })
                            .show();

                }else {
                    progressDialog.dismiss();
                    AlertDialog.Builder alert=new AlertDialog.Builder(SignUpActivity.this);
                    alert.setTitle("Operation Failed")
                            .setMessage("The email address you have entered is already registered please login.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("Retry",null)
                            .show();                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
