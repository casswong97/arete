package com.example.veronica.areteapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

import static com.example.veronica.areteapp.LoginActivity.EncodeString;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private ListFragment listFragment;
    private CalendarFragment calendarFragment;
    private JournalFragment journalFragment;
    private FirebaseAuth mAuth;

    //strings for push notification example testing
    public static final String CHANNEL_ID = "arete";
    private static final String CHANNEL_NAME = "Arete";
    private static final String CHANNEL_DESC = "Arete Notification";

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainNav = (BottomNavigationView) findViewById(R.id.mainNav);
        mMainFrame = (FrameLayout) findViewById(R.id.mainFrame);

        homeFragment = new HomeFragment();
        listFragment = new ListFragment();
        calendarFragment = new CalendarFragment();
        journalFragment = new JournalFragment();

        setFragment(homeFragment);
        mMainNav.setOnNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();

        //push notification
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        //getting the FCM token for firebase cloud messaging
        id = FirebaseInstanceId.getInstance().getToken();
        saveToken(id);

        //TODO: push notification not working (can't test, version is different apparently?) need an actual device?
    }

    private void saveToken(String token) {
        //saves the token to firebase
        String email = EncodeString(mAuth.getCurrentUser().getEmail());

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(email);
        db.child("token").setValue(token);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.homeNav:
                setFragment(homeFragment);
                return true;

            case R.id.listNav:
                setFragment(listFragment);
                return true;

            case R.id.calendarNav:
                setFragment(calendarFragment);
                return true;
            case R.id.journalNav:
                setFragment(journalFragment);
                return true;

            case R.id.logOutNav:

                FirebaseAuth.getInstance().signOut();

                this.finishAffinity();

                Intent myIntent = new Intent(this, LoginActivity.class);
                this.startActivity(myIntent);

                return true;

            default:
                return false;
        }
    }
    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();
    }

}
