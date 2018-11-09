package com.example.veronica.areteapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class JournalFragment extends Fragment
{
    private EditText editDate;
    ArrayAdapter<String> mTaskAdapter;

    public JournalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootview = inflater.inflate(R.layout.fragment_journal, container, false);

        editDate = rootview.findViewById(R.id.editDate);
        setDate();

        //Create the fake data
        String[] fakeData = {
                "Revise for exam",
                "Buy milk",
                "Do laundry",
                "Call Melissa",
                "Buy stamps",
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
        editDate.setText(android.text.format.DateFormat.format("MMMM dd, yyyy", new java.util.Date()));
    }
}
