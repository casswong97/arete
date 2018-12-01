package com.example.veronica.areteapp;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.example.veronica.areteapp.LoginActivity.EncodeString;

/**
 * A simple {@link Fragment} subclass.
 */
public class JournalFragment extends Fragment implements Button.OnClickListener {
	private TextView eT_Date;
	private Button bT_Submit;
	private ArrayList<Goals> goals;
	private EditText eT_Day_Reflection_answer;
	private RatingBar ratingBar_Status;
	private FirebaseAuth mAuth;
	private String TAG = "TAG";
	private int year, month, dayOfMonth;
	private GregorianCalendar date;

    public JournalFragment() {
		// Required empty public constructor
		Date date = new java.util.Date();
		this.dayOfMonth = date.getDate();
		this.year = date.getYear();
		this.month = date.getMonth();
	}

	@SuppressLint("ValidFragment")
	public JournalFragment(int year, int month, int dayOfMonth) {
		this.year = year;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
	}
    // TODO: Instance where there's no goals, display a message, currently empty page

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_journal, container, false);

        // Retrieve UI Elements
        eT_Date = rootview.findViewById(R.id.tV_Date);
        eT_Day_Reflection_answer = rootview.findViewById(R.id.eT_Day_Reflection_Answer);
        bT_Submit = rootview.findViewById(R.id.bt_Submit);
        bT_Submit.setOnClickListener(this);
        ratingBar_Status = rootview.findViewById(R.id.ratingBar_Status);
        ratingBar_Status.setOnClickListener(this);

        // Get Firebase Auth Object
        mAuth = FirebaseAuth.getInstance();

        //TODO: Error HERE with Gregorian Calendar Object -> Can't format like in ListFragment
        // Get date from Goal List
        date = new GregorianCalendar(year, month, dayOfMonth);

        // Set date of page
        setDate();

        // Set up Goals UI
        setGoalView(rootview);

        // Inflate the layout for this fragment
        return rootview;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Submit:
                Toast.makeText(getActivity(), "Thanks for submitting your reflection for today!", Toast.LENGTH_LONG).show();
        }
    }

    private void updateDayReflectionDB() {
        // Get email of User
        String email = getEmail();
        if (!TextUtils.isEmpty(email)) {
            int stars = ratingBar_Status.getNumStars();
            String dayReflection = eT_Day_Reflection_answer.getText().toString();
            setDBDayReflection(email, new DayReflection(stars, dayReflection));
        } else {
            Log.w(TAG, email + " didn't exist, unable to update Reflection Answer");
        }
    }

    private void setDBDayReflection(String email, final DayReflection reflection) {
        // Retrieve goals from DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference("Users");
        final DatabaseReference dayReflectionRef = rootRef.child(email).child("Calendar").child(stringDateKey()).child("Reflection");
        dayReflectionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dayReflectionRef.setValue(reflection);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void setDate() {
        eT_Date.setText(android.text.format.DateFormat.format("MMMM dd, yyyy", date));
    }

    private void setGoalView(View view) {
        // Set up Recycler Adapter/View for Goals List
        // Retrieve Goals
        initGoals();
        // Inflate Recycler View
        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(goals, getActivity());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initGoals() {
        String email = getEmail();
        if (!TextUtils.isEmpty(email)) {
            retrieveGoals(email);
        } else {
            Log.w(TAG, email + " didn't exist, unable to populate goal list");
        }
    }

    private String getEmail() {
        String email;
        // get user email
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            email = EncodeString(user.getEmail());
        } else {
            email = null;
        }
        return email;
    }

    private void retrieveGoals(String email) {
        // Initialize goals list
        goals = new ArrayList<>();

        // Retrieve goals from DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference("Users");
        DatabaseReference goalTableRef = rootRef.child(email).child("Calendar").child(stringDateKey()).child("Goals");
        goalTableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Add goals to goals list
                for (DataSnapshot goalSnapshot : dataSnapshot.getChildren()) {
                    Goals goal = goalSnapshot.getValue(Goals.class);
                    goals.add(goal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private String stringDateKey() {
        Date dateDate = date.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy")
        String dateKey = df.format(dateDate.getTime());
        return dateKey;
    }
}