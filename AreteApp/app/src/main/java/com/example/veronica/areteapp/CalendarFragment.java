package com.example.veronica.areteapp;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener, Button.OnClickListener
{
    private CalendarView calendarView;
	private BottomNavigationView mMainNav;
	private Button buttonJumpToDate;

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
        buttonJumpToDate = (Button) rootview.findViewById(R.id.buttonJumpToDate);

        buttonJumpToDate.setOnClickListener(this);

		// Inflate the layout for this fragment

		return rootview;
    }


	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.buttonJumpToDate:

				GregorianCalendar gcDate = new GregorianCalendar();
				Date date = gcDate.getTime();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				String todayDate = dateFormat.format(date.getTime());

				buildAlertDiaglogBox("Select Date:", "Format dd-MM-yyyy", "Go to date", todayDate);
				break;
		}

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


	private void buildAlertDiaglogBox(final String title, String message, String positiveButton, final String todayDate)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(title);
		builder.setMessage(message);

		final EditText inputField = new EditText(getActivity());
		builder.setView(inputField);
		inputField.setText(todayDate);

		builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface di, int i)
			{
				try
				{
					String enteredDate = inputField.getText().toString();
					int year = Integer.valueOf(enteredDate.substring(6, 10));
					// gregorian date off by 1
					int month = Integer.valueOf(enteredDate.substring(3, 5)) - 1;
					int dayOfMonth = Integer.valueOf(enteredDate.substring(0, 2));

					onSelectedDayChange(calendarView, year, month, dayOfMonth);
				}
				catch (Exception exception)
				{
					Toast toast = Toast.makeText(getActivity(), "Date entered incorrectly", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		});
		builder.setNegativeButton("Cancel", null);

		builder.setIcon(R.drawable.ic_calendar);
		builder.create().show();
	}

}
