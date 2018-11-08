package com.example.veronica.areteapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;
import android.support.annotation.NonNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener
{
    CalendarView calendarView;

    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootview = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = rootview.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
        // Inflate the layout for this fragment
        return rootview;
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
    {
        Toast.makeText(getActivity(), Integer.toString(dayOfMonth), Toast.LENGTH_SHORT).show();
    }
}
