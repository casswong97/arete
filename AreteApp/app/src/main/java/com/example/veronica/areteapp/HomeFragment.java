package com.example.veronica.areteapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements Button.OnClickListener
{
    private EditText editGreeting;
    private EditText editTextAnswer;
    private ImageButton buttonEdit;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);
        editGreeting = (EditText) rootview.findViewById(R.id.editGreeting);
        editTextAnswer = (EditText) rootview.findViewById(R.id.editTextAnswer);
        buttonEdit = (ImageButton) rootview.findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(this);

        setGreeting();

        // Inflate the layout for this fragment
        return rootview;
    }

    private void setGreeting()
    {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String greeting = "";

        if(timeOfDay >= 0 && timeOfDay < 12)
        {
            greeting = "Good Morning!";
            editGreeting.setText(greeting);
        }
        else if(timeOfDay >= 12 && timeOfDay < 16)
        {
            greeting = "Good Afternoon!";
            editGreeting.setText(greeting);
        }
        else if(timeOfDay >= 16 && timeOfDay < 21)
        {
            greeting = "Good Evening!";
            editGreeting.setText(greeting);
        }
        else if(timeOfDay >= 21 && timeOfDay < 24)
        {
            greeting = "Good Night!";
            editGreeting.setText(greeting);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.buttonEdit:
                Toast.makeText(getActivity(), "You've submitted your daily goal!", Toast.LENGTH_SHORT).show();
        }
    }
}
