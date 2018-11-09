package com.example.veronica.areteapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity implements Button.OnClickListener {

    private Button buttonLogin, buttonCreateAccount;
    private EditText editTextLogin, editTextPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);

        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonLogin.setOnClickListener(this);
        buttonCreateAccount.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null)
                {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_id:" + user.getUid());
                }
                else
                {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out:");
                }
            }
        };
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            //create account
            case R.id.buttonCreateAccount:
                createAccount(editTextLogin.getText().toString(), editTextPassword.getText().toString());
                break;

            case R.id.buttonLogin:
                signIn(editTextLogin.getText().toString(), editTextPassword.getText().toString());
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;

        }
    }
    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void createAccount(String email, String password)
    {
        Toast toast;
        if (password.length() < 6)
        {
            toast = Toast.makeText(LoginActivity.this, "Authentication Failed.\n Password must be at least 6 characters", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // if signin fails, display a msg to user
                        Toast toast;
                        if (!task.isSuccessful())
                        {
                            toast = Toast.makeText(LoginActivity.this, "Authentication Failed.\n Do you already have an account?", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        else
                        {
                            toast = Toast.makeText(LoginActivity.this, "Success!\n Account Created!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        }
                    }
                });

    }

    public void signIn(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // if sign in fails display msg
                        Toast toast;

                        if (!task.isSuccessful())
                        {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            toast = Toast.makeText(LoginActivity.this, "Authentication Failed.\n Did you create an Account?", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        else
                        {
                            toast = Toast.makeText(LoginActivity.this, "Success!\n You are logged in!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                });

    }
}

