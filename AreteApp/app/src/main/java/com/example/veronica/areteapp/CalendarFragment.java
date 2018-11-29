package com.example.veronica.areteapp;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.support.annotation.NonNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener {
    CalendarView calendarView;
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = rootview.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);

        mMainNav = (BottomNavigationView) rootview.findViewById(R.id.mainNav);
        mMainFrame = (FrameLayout) rootview.findViewById(R.id.mainFrame);

        // Inflate the layout for this fragment
        return rootview;
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
