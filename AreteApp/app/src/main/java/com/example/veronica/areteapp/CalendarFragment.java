package com.example.veronica.areteapp;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener, Button.OnClickListener
{
    private CalendarView calendarView;
	private BottomNavigationView mMainNav;
	private Button buttonJumpToDate;
	private CaldroidFragment caldroidFragment;
	private FirebaseAuth mAuth;
	private String email;
	private ColorDrawable fiveStars, fourStars, threeStars, twoStars, oneStars;

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
        buttonJumpToDate = (Button) rootview.findViewById(R.id.buttonJumpToDate);
		mAuth = FirebaseAuth.getInstance();
		FirebaseUser user = mAuth.getCurrentUser();
		this.email = LoginActivity.EncodeString(user.getEmail());
        buttonJumpToDate.setOnClickListener(this);

        // set star color
		fiveStars = new ColorDrawable(getResources().getColor(R.color.colorFiveStars));
		fourStars = new ColorDrawable(getResources().getColor(R.color.colorFourStars));
		threeStars = new ColorDrawable(getResources().getColor(R.color.colorThreeStars));
		twoStars = new ColorDrawable(getResources().getColor(R.color.colorTwoStars));
		oneStars = new ColorDrawable(getResources().getColor(R.color.colorOneStar));

		setCalendroidFrag();
        setCalendarColors();

		// Inflate the layout for this fragment

		return rootview;
    }

    private void setCalendroidFrag()
	{
		caldroidFragment = new CaldroidFragment();
		Bundle args = new Bundle();
		Calendar cal = Calendar.getInstance();
		args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
		args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
		caldroidFragment.setArguments(args);

		FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendarView, caldroidFragment);
		t.commit();


		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view)
			{
				GregorianCalendar gcDate = new GregorianCalendar(date.getYear()+1900, date.getMonth(), date.getDate());

				// if date is in future, no reflection entry yet
				if (gcDate.compareTo(new GregorianCalendar()) > 0)
				{
					Toast toast = Toast.makeText(getActivity(), "This date is in the future. No Reflection entered for this date yet.", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}

				Fragment myFragment = new JournalFragment(gcDate);
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, myFragment).addToBackStack(null).commit();
				mMainNav.getMenu().findItem(R.id.journalNav).setChecked(true);
			}

			@Override
			public void onChangeMonth(int month, int year)
			{

			}

			@Override
			public void onLongClickDate(Date date, View view)
			{
			}

			@Override
			public void onCaldroidViewCreated()
			{
			}

		};

		caldroidFragment.setCaldroidListener(listener);
	}


    private void setCalendarColors()
	{

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		final DatabaseReference rootRef = database.getReference("Users");
		final DatabaseReference dayRef = rootRef.child(email).child("Calendar");
		dayRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot)
			{
				// get list of all dates
				ArrayList<String> dateList = new ArrayList<String>();
				for (DataSnapshot snapshot: dataSnapshot.getChildren())
				{
					String dateKey = snapshot.getKey();
					dateList.add(dateKey);
				}

				// get rating for each date if exists
				for (final String key: dateList)
				{
					DatabaseReference dayReflectionRef = rootRef.child(email).child("Calendar").child(key).child("Reflection");
					dayReflectionRef.addListenerForSingleValueEvent(new ValueEventListener()
					{
						@Override
						public void onDataChange(@NonNull DataSnapshot dataSnapshot)
						{
							if (dataSnapshot.exists())
							{
								DayReflection reflection = dataSnapshot.getValue(DayReflection.class);
								Calendar cal = setCalDate(key);
								setDateColor((int)reflection.getRating(), cal.getTime());
							}
						}

						@Override
						public void onCancelled(@NonNull DatabaseError databaseError)
						{
						}
					});

				}

			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				// Failed to read value
			}
		});

	}

	private Calendar setCalDate(String dateKey)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.valueOf(dateKey.substring(6, 10)));
		cal.set(Calendar.MONTH, Integer.valueOf(dateKey.substring(3, 5)) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dateKey.substring(0, 2)));
		return cal;
	}

	private void setDateColor(int stars, Date date)
	{
		if (stars == 5)
			caldroidFragment.setBackgroundDrawableForDate(fiveStars, date);
		else if (stars == 4)
			caldroidFragment.setBackgroundDrawableForDate(fourStars, date);
		else if (stars == 3)
			caldroidFragment.setBackgroundDrawableForDate(threeStars, date);
		else if (stars == 2)
			caldroidFragment.setBackgroundDrawableForDate(twoStars, date);
		else if (stars == 1)
			caldroidFragment.setBackgroundDrawableForDate(oneStars, date);
		caldroidFragment.refreshView();
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
