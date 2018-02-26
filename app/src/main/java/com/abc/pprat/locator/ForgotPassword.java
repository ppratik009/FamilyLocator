package com.abc.pprat.locator;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText etForgot;
    private Button btnForgot;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etForgot=(EditText)findViewById(R.id.etForgotEmail);
        btnForgot=(Button)findViewById(R.id.btnForgot);

        auth=FirebaseAuth.getInstance();

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                auth.sendPasswordResetEmail(etForgot.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            AlertDialog.Builder alert=new AlertDialog.Builder(ForgotPassword.this);
                            alert.setMessage("Check you email address to reset you password")
                                    .setTitle("Operation Successful")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(ForgotPassword.this,SignInActivity.class));
                                        }
                                    })
                                    .show();
                        }else {
                            AlertDialog.Builder alert=new AlertDialog.Builder(ForgotPassword.this);
                            alert.setMessage("Your email address is not registered please Sign Up.")
                                    .setTitle("Operation Failed")
                                    .setPositiveButton("Sign Up", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(ForgotPassword.this,SignUpActivity.class));
                                            finish();
                                        }
                                    })
                                    .show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
