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
       // TODO needs to be dynamic
//       textGreeting = (TextView) getActivity().findViewById(R.id.textGreeting);
//       editTextAnswer = (EditText) getView().findViewById(R.id.editTextAnswer);
//       buttonEdit = (ImageButton) getView().findViewById(R.id.buttonEdit);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

}


