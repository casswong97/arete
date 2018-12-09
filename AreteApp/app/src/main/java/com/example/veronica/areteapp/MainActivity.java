package com.example.veronica.areteapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private ListFragment listFragment;
    private CalendarFragment calendarFragment;
    private JournalFragment journalFragment;
    private FirebaseAuth mAuth;

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
