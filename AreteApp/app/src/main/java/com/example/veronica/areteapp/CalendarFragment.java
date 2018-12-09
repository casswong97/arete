package com.example.veronica.areteapp;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener
{
    private CalendarView calendarView;
	private BottomNavigationView mMainNav;

    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootview = inflater.inflate(R.layout.fragment_calendar, container, false);

        mMainNav = getActivity().findViewById(R.id.mainNav);
        calendarView = rootview.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);

		// Inflate the layout for this fragment

		return rootview;
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
    {
		AppCompatActivity activity = (AppCompatActivity) view.getContext();

		GregorianCalendar gcDate = new GregorianCalendar(year, month, dayOfMonth);

		// if date is in future, no relfection entry yet
		if (gcDate.compareTo(new GregorianCalendar()) > 0)
		{
			Toast toast = Toast.makeText(getActivity(), "This date is in the future. No Reflection entered for this date yet.", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return;
		}

		Fragment myFragment = new JournalFragment(gcDate);
		activity.getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, myFragment).addToBackStack(null).commit();
		mMainNav.getMenu().findItem(R.id.journalNav).setChecked(true);
    }
}
