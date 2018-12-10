package com.example.veronica.areteapp;


import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements Button.OnClickListener
{
    private TextView viewGreeting;
    private EditText editTextAnswer;
    private ImageButton buttonEdit;
    private FirebaseAuth mAuth;
    private String email;
    private String dateKey;

    private ImageView imgMorning, imgAfternoon, imgEvening, imgNight;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        email = LoginActivity.EncodeString(user.getEmail());

        Date date = new GregorianCalendar().getTime();
        SimpleDateFormat sF = new SimpleDateFormat("dd-MM-yyyy");
        dateKey = sF.format(date.getTime());

        viewGreeting = (TextView) rootview.findViewById(R.id.viewGreeting);
        editTextAnswer = (EditText) rootview.findViewById(R.id.editTextAnswer);
        buttonEdit = (ImageButton) rootview.findViewById(R.id.buttonEdit);

        imgMorning = (ImageView) rootview.findViewById(R.id.morning_icon);
        imgAfternoon = (ImageView) rootview.findViewById(R.id.afternoon_icon);
        imgEvening = (ImageView) rootview.findViewById(R.id.evening_icon);
        imgNight = (ImageView) rootview.findViewById(R.id.night_icon);

        buttonEdit.setOnClickListener(this);

        setTodayIf();
        setGreeting();

        // Inflate the layout for this fragment
        return rootview;
    }

    private void setTodayIf()
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference("Users");

        final DatabaseReference todayIf = rootRef.child(email).child("Calendar").child(dateKey).child("todayIf");
        todayIf.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                    editTextAnswer.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }

    private void setGreeting()
    {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String greeting = "";

        if(timeOfDay >= 6 && timeOfDay < 12)
        {
            greeting = "Good Morning!";
            viewGreeting.setText(greeting);
            imgMorning.setVisibility(View.VISIBLE);
        }
        else if(timeOfDay >= 12 && timeOfDay < 17)
        {
            greeting = "Good Afternoon!";
            viewGreeting.setText(greeting);
            imgAfternoon.setVisibility(View.VISIBLE);
        }
        else if(timeOfDay >= 17 && timeOfDay < 20)
        {
            greeting = "Good Evening!";
            viewGreeting.setText(greeting);
            imgEvening.setVisibility(View.VISIBLE);
        }
        else if((timeOfDay >= 20 && timeOfDay < 24) || (timeOfDay >= 0 && timeOfDay < 6))
        {
            greeting = "Good Night!";
            viewGreeting.setText(greeting);
            imgNight.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // saves "Today if" goal in DB
            case R.id.buttonEdit:
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference rootRef = database.getReference("Users");

                final DatabaseReference todayIf = rootRef.child(email).child("Calendar").child(dateKey).child("todayIf");
                todayIf.addListenerForSingleValueEvent(new ValueEventListener()
                   {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                       {
                            todayIf.setValue(editTextAnswer.getText().toString());
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError)
                       {
                       }
                   });

                Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
