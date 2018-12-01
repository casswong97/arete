package com.example.veronica.areteapp;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.support.constraint.Constraints.TAG;

public class LoginActivity extends Activity implements Button.OnClickListener {

    private Button buttonLogin, buttonCreateAccount;
    private EditText editTextLogin, editTextPassword;
    private FirebaseAuth mAuth;
    private String TAG = "TAG";
    private GifImageView mGifImageView;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mGifImageView = (GifImageView) findViewById(R.id.gifLogo);

		GifDrawable gifDrawable = null;
		try {
			gifDrawable = new GifDrawable(getResources(), R.drawable.logo_gif);
			gifDrawable.setLoopCount(3);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mGifImageView.setImageDrawable(gifDrawable);

        // Get UI objects
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);

        // Set listeners for buttons
        buttonLogin.setOnClickListener(this);
        buttonCreateAccount.setOnClickListener(this);
        // Get Firebase Auth Object
        mAuth = FirebaseAuth.getInstance();

        //create a new progress dialog
        progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //create account
            case R.id.buttonCreateAccount:
                createAccount(editTextLogin.getText().toString(), editTextPassword.getText().toString());
                break;
            case R.id.buttonLogin:
                signIn(editTextLogin.getText().toString(), editTextPassword.getText().toString());
                break;

        }
    }

    public void createAccount(final String email, String password) {

        progressDialog.setMessage("Registering, please wait...");
        progressDialog.show();

        Toast toast;
        if (password.length() < 6) {
            progressDialog.hide();
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
                        // if sign in fails, display a msg to user
                        Toast toast;
                        if (!task.isSuccessful()) {
                            progressDialog.hide();
                            toast = Toast.makeText(LoginActivity.this, "Account Creation Failed.\n Do you already have an account?", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        else {
                            progressDialog.hide();
                            toast = Toast.makeText(LoginActivity.this, "Success!\n Account Created!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            createUserIdEntry(new User(EncodeString(email)));
                        }
                    }
                });
    }

    public void signIn(String email, String password) {

        progressDialog.setMessage("Logging in, please wait...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // if sign in fails display msg
                        Toast toast;

                        if (!task.isSuccessful()) {
                            progressDialog.hide();

                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            toast = Toast.makeText(LoginActivity.this, "Authentication Failed.\n Did you create an Account?", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        else {
                            progressDialog.hide();
                            toast = Toast.makeText(LoginActivity.this, "Success!\n You are logged in!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    }
                });
    }

    public void createUserIdEntry(final User user) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userTableRef = database.getReference("Users");
            final DatabaseReference userRef = userTableRef.child(user.getUserName());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(LoginActivity.this, user.getUserName() + " already exists!", Toast.LENGTH_SHORT).show();
                    } else {
                        userRef.setValue(user);
                        Toast.makeText(LoginActivity.this, user.getUserName() + " added to DB", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                }
            });
    }
    // Helper functions to work around Firebase Datapath rules
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
    }
}

