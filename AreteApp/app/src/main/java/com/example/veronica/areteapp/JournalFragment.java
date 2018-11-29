package com.example.veronica.areteapp;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class JournalFragment extends Fragment implements Button.OnClickListener
{
    private TextView viewDate;
    ArrayAdapter<String> mTaskAdapter;
    private Button buttonSubmit;
    private int year, month, dayOfMonth;

    public JournalFragment()
	{
		// Required empty public constructor
        Date date = new java.util.Date();
        this.dayOfMonth = date.getDate();
        this.year = date.getYear();
        this.month = date.getMonth();
	}

    @SuppressLint("ValidFragment")
    public JournalFragment(int year, int month, int dayOfMonth)
	{
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    /**
     *
     * Found ToDo list code here:
     * http://muggingsg.com/university/android-app-tutorial-todo-app-using-fragments/#2_Creating_a_List_of_Tasks
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootview = inflater.inflate(R.layout.fragment_journal, container, false);

        viewDate = rootview.findViewById(R.id.viewDate);
        setDate();

        buttonSubmit = rootview.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(this);

        //Create the fake data
        String[] fakeData = {
                "Finish App",
                "Groceries",
                "Walk Dog",
        };
        List<String> tasks = new ArrayList<String>(Arrays.asList(fakeData));

        //Create the ArrayAdapter by specifying context, "how" (layout),"where" (textview), and " what" (data)
        mTaskAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_task,
                R.id.list_item_task_textview,
                tasks);

        //Still need to bind adapter to the ListView
        ListView listView = (ListView) rootview.findViewById(R.id.listViewTask);
        listView.setAdapter(mTaskAdapter);

        // Inflate the layout for this fragment
        return rootview;

    }

    private void setDate()
    {
        Calendar cal=Calendar.getInstance();
		GregorianCalendar date = new GregorianCalendar(year, month, dayOfMonth);
        viewDate.setText(android.text.format.DateFormat.format("MMMM dd, yyyy", date));
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.buttonSubmit:
                Toast.makeText(getActivity(), "Thanks for submitting your reflection for today!", Toast.LENGTH_LONG).show();
        }
    }
}
