package com.example.veronica.areteapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment
{

    private TextView textGreeting;
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
        textGreeting = (TextView)rootview.findViewById(R.id.textGreeting);
        editTextAnswer = (EditText) rootview.findViewById(R.id.editTextAnswer);
        buttonEdit = (ImageButton) rootview.findViewById(R.id.buttonEdit);

        // Inflate the layout for this fragment
        return rootview;
    }

}


