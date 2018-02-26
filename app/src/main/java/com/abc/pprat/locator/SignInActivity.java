package com.abc.pprat.locator;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import thebat.lib.validutil.ValidUtils;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {


    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnSignIn;
    private Button btnSignUp;
    private TextView tvForgot;

    private ValidUtils validUtils;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("users");
    Map<String, String> map;
    String uName;
    String uEmail;
    String uCode;
    String uStatus;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sharedPreferences = getSharedPreferences("RefLocator", MODE_PRIVATE);

        //Initialization of view
        initializeElements();

        //For validation
        validUtils = new ValidUtils();

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    // Elements initialization
    private void initializeElements() {
        etEmail = (TextInputEditText) findViewById(R.id.tietEmail);
        etPassword = (TextInputEditText) findViewById(R.id.tietPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        tvForgot = (TextView) findViewById(R.id.tvForgotPassword);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        tvForgot.setOnClickListener(this);

        progressDialog=new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSignIn:

                signIn();
                break;

            case R.id.btnSignUp:

                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));

                break;

            case R.id.tvForgotPassword:
                startActivity(new Intent(SignInActivity.this, ForgotPassword.class));

                break;

            default:
                Toast.makeText(this, "No Case", Toast.LENGTH_SHORT).show();
                return;
        }
    }

    private void signIn() {


        if (!validUtils.validateEmail(etEmail)) {
            etEmail.setError("Enter valid email address.");
        } else if (!validUtils.validateEditTexts(etPassword)) {
            etPassword.setError("Enter valid password.(At least 6 characters)");
        } else if (!validUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Plase connect your internet firs.", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setTitle("Processing");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            logInUser(etEmail.getText().toString(), etPassword.getText().toString());
        }


    }

    private void logInUser(String s, String s1) {
        final String email = s;
        String password = s1;

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                    String userName = email.split("@")[0];

                    DatabaseReference userReference = databaseReference.child(userName);
                    userReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            map = (Map<String, String>) dataSnapshot.getValue();

                            uName = map.get("Name").toString();
                            uEmail = map.get("Email").toString();
                            uStatus = map.get("Status").toString();
                            uCode = map.get("Code").toString();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Name", uName);
                            editor.putString("Email", uEmail);
                            editor.putString("Code", uCode);
                            editor.putString("Status", uStatus);
                            editor.commit();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    progressDialog.dismiss();
                    Intent dashBoardIntent = new Intent(SignInActivity.this, DashboardActivity.class);
                    startActivity(dashBoardIntent);

                    finish();

                } else {
                    progressDialog.dismiss();
                    AlertDialog.Builder alert=new AlertDialog.Builder(SignInActivity.this);
                    alert.setTitle("Operation Failed")
                            .setMessage("The email or password you have entered is invalid.")
                            .setPositiveButton("Retry",null)
                            .show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
