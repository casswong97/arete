package com.example.veronica.areteapp;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.annotation.NonNull;

<<<<<<< Updated upstream
/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener {
    CalendarView calendarView;
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
=======
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.example.veronica.areteapp.LoginActivity.EncodeString;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener, Button.OnClickListener {
    private CalendarView calendarView;
    private BottomNavigationView mMainNav;
    private Spinner spinnerDate;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    final private List<String> inputs = new ArrayList<String>();
    private Button buttonSubmit;

    String spinnerInput;
    int year, month, dayOfMonth;
>>>>>>> Stashed changes

    public CalendarFragment() {
        // Required empty public constructor
//        this.gcDate = new GregorianCalendar();
//        getKeyFromGCDate(this.gcDate);
    }

<<<<<<< Updated upstream
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_calendar, container, false);
=======
//    private void getKeyFromGCDate(GregorianCalendar gcDate) {
//        Date date = gcDate.getTime();
//        SimpleDateFormat sF = new SimpleDateFormat("dd-MM-yyyy");
//        this.dateKey = sF.format(date.getTime());
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_calendar, container, false);
>>>>>>> Stashed changes

        calendarView = rootview.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
        spinnerDate = (Spinner) rootview.findViewById(R.id.spinnerDate);
        buttonSubmit = rootview.findViewById(R.id.buttonSubmit);

<<<<<<< Updated upstream
        mMainNav = (BottomNavigationView) rootview.findViewById(R.id.mainNav);
        mMainFrame = (FrameLayout) rootview.findViewById(R.id.mainFrame);

        // Inflate the layout for this fragment
        return rootview;
=======
        buttonSubmit.setOnClickListener(this);

        // Get Firebase Auth Object
        mAuth = FirebaseAuth.getInstance();

        //populate spinner with firebase dates for the user
        final DatabaseReference rootRef = database.getReference("Users");

        //foundKey has to be user
        FirebaseUser user = mAuth.getCurrentUser();
        String email = EncodeString(user.getEmail());

        String foundKey = email;

        rootRef.child(foundKey).child("Calendar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //adds dateName to the list inputs
                //for use in putting items into spinnerDate
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String dateName = String.valueOf(ds.getKey());
                    inputs.add(dateName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //creates the spinner with dates
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootview.getContext(), android.R.layout.simple_spinner_item, inputs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(adapter);

        //this is supposed to set it so that when user selects an item from spinner, it goes to the journal entry upon clicking buttonSubmit
//        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                int pos = spinnerDate.getSelectedItemPosition();
//                adapter.notifyDataSetChanged();
//                spinnerInput = parent.getItemAtPosition(pos).toString();
//
//                year = Integer.valueOf(spinnerInput.substring(6,10));
//                month = Integer.valueOf(spinnerInput.substring(3,5));
//                dayOfMonth = Integer.valueOf(spinnerInput.substring(0,3));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        inputs.clear();
//
		// Inflate the layout for this fragment
		return rootview;
>>>>>>> Stashed changes
    }


    //this doesn't actually go to the day's activity... don't know how to make it do that
    @Override
    public void onClick(View v) {
        if(v == buttonSubmit) {
            AppCompatActivity activity = (AppCompatActivity) getView().getContext();

            GregorianCalendar gcDate = new GregorianCalendar(year, month, dayOfMonth);
            Fragment myFragment = new JournalFragment(gcDate);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, myFragment).addToBackStack(null).commit();
            mMainNav.getMenu().findItem(R.id.journalNav).setChecked(true);
        }
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Fragment myFragment = new JournalFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, myFragment).addToBackStack(null).commit();

        //TODO: onSelectedDayChange, change bottom navigation bar icon/color to journalNav
        //mMainNav.setSelectedItemId(R.id.journalNav);
    }

}
