package com.example.veronica.areteapp;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
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

import org.joda.time.DateTimeComparator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.veronica.areteapp.LoginActivity.EncodeString;

/**
 * A simple {@link Fragment} subclass.
 */

public class JournalFragment extends Fragment implements Button.OnClickListener, View.OnFocusChangeListener {
    private TextView eT_Date, tV_Exercise_Entry;
    private Button bT_Submit;
    private ArrayList<Goals> goals;
    private EditText eT_Day_Reflection_Answer;
    private EditText eT_Daily_Exercise_Answer;
    private RatingBar ratingBar_Status;
    private FirebaseAuth mAuth;
    private String TAG = "TAG";
	private GregorianCalendar gcDate;
	private String dateKey;

	public JournalFragment() {
		// Required empty public constructor
		this.gcDate = new GregorianCalendar();
		getKeyFromGCDate(this.gcDate);
	}

	@SuppressLint("ValidFragment")
	public JournalFragment(GregorianCalendar gcDate) {
		this.gcDate = gcDate;
		getKeyFromGCDate(this.gcDate);
	}

	private void getKeyFromGCDate(GregorianCalendar gcDate) {
		Date date = gcDate.getTime();
		SimpleDateFormat sF = new SimpleDateFormat("dd-MM-yyyy");
		this.dateKey = sF.format(date.getTime());
	}
    // TODO: Instance where there's no goals, display a message, currently empty page

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_journal, container, false);

        // Retrieve UI Elements
        eT_Date = rootview.findViewById(R.id.tV_Date);
        eT_Day_Reflection_Answer = rootview.findViewById(R.id.eT_Day_Reflection_Answer);
        eT_Daily_Exercise_Answer = rootview.findViewById(R.id.eT_DE);
        eT_Daily_Exercise_Answer.setOnFocusChangeListener(this);
        bT_Submit = rootview.findViewById(R.id.bt_Submit);
        bT_Submit.setOnClickListener(this);
        ratingBar_Status = rootview.findViewById(R.id.ratingBar_Status);
        ratingBar_Status.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
			{
				setStarColor(ratingBar, rating, fromUser);
			}
		});

		tV_Exercise_Entry = (TextView) rootview.findViewById(R.id.tV_Exercise_Entry);

        // Get Firebase Auth Object
        mAuth = FirebaseAuth.getInstance();

        // Set up Initial UI
        setUI(rootview);

        // Inflate the layout for this fragment
        return rootview;
    }

    private void setStarColor(RatingBar ratingBar, float rating, boolean fromUser)
	{
		int colorZeroStars = getResources().getColor(R.color.colorAccent);
		int colorOneStar = getResources().getColor(R.color.colorOneStar);
		int colorTwoStars = getResources().getColor(R.color.colorTwoStars);
		int colorThreeStars = getResources().getColor(R.color.colorThreeStars);
		int colorFourStars = getResources().getColor(R.color.colorFourStars);
		int colorFiveStars = getResources().getColor(R.color.colorFiveStars);

		LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
		stars.getDrawable(0).setColorFilter(colorZeroStars, PorterDuff.Mode.SRC_ATOP);

		//change color of 5 stars
		if (ratingBar.getRating() == 5f)
		{
			stars.getDrawable(2).setColorFilter(colorFiveStars, PorterDuff.Mode.SRC_ATOP);
			stars.getDrawable(1).setColorFilter(colorFiveStars, PorterDuff.Mode.SRC_ATOP);
		}
		//change color of 4 stars
		else if (ratingBar.getRating() == 4f)
		{
			stars.getDrawable(2).setColorFilter(colorFourStars, PorterDuff.Mode.SRC_ATOP);
			stars.getDrawable(1).setColorFilter(colorFourStars, PorterDuff.Mode.SRC_ATOP);
		}
		//change color of 3 stars
		else if (ratingBar.getRating() == 3f)
		{
			stars.getDrawable(2).setColorFilter(colorThreeStars, PorterDuff.Mode.SRC_ATOP);
			stars.getDrawable(1).setColorFilter(colorThreeStars, PorterDuff.Mode.SRC_ATOP);
		}
		//change color of 2 stars
		else if (ratingBar.getRating() == 2f)
		{
			stars.getDrawable(2).setColorFilter(colorTwoStars, PorterDuff.Mode.SRC_ATOP);
			stars.getDrawable(1).setColorFilter(colorTwoStars, PorterDuff.Mode.SRC_ATOP);
		}
		//change color of 1 stars
		else if (ratingBar.getRating() == 1f)
		{
			stars.getDrawable(2).setColorFilter(colorOneStar, PorterDuff.Mode.SRC_ATOP);
			stars.getDrawable(1).setColorFilter(colorOneStar, PorterDuff.Mode.SRC_ATOP);
		}
	}

    private void setUI(View rootview) {
        // Set date of page
        setDate();
        // Set up Goals UI
        initGoals(rootview);
		// Set Exercise Question
        setExercise(getEmail());
		// Set up Daily Reflection UI
        setDayReflection(getEmail());
        // If not today, hide some functionality for UI
        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        Date date = gcDate.getTime();
        Date today = new Date();
        if (dateTimeComparator.compare(date, today) < 0) {
            setPastDateUI();
        }
    }

    private void setPastDateUI() {
        eT_Daily_Exercise_Answer.setFocusable(false);
        eT_Daily_Exercise_Answer.setClickable(false);
        eT_Daily_Exercise_Answer.setCursorVisible(false);
        eT_Day_Reflection_Answer.setFocusable(false);
        eT_Day_Reflection_Answer.setClickable(false);
        eT_Day_Reflection_Answer.setCursorVisible(false);
        ratingBar_Status.setIsIndicator(true);
        ratingBar_Status.setFocusable(false);
        ratingBar_Status.setClickable(false);
        bT_Submit.setEnabled(false);
        bT_Submit.setVisibility(View.INVISIBLE);
    }

    // Set Date textview of the page
    private void setDate() {
        eT_Date.setText(android.text.format.DateFormat.format("MMMM dd, yyyy", gcDate));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Submit:
                updateDayReflectionDB();
                Toast.makeText(getActivity(), "Thanks for submitting your reflection for today!", Toast.LENGTH_LONG).show();
                showCongratsFragment(v);
                break;
		}
    }

    private void showCongratsFragment(View v) {
		View popupView = getLayoutInflater().inflate(R.layout.layout_congrats, null);

		final PopupWindow popupWindow = new PopupWindow(popupView,
				FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

		// If the PopupWindow should be focusable
		popupWindow.setFocusable(false);

		// If you need the PopupWindow to dismiss when when touched outside
		popupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});

		// Using location, the PopupWindow will be displayed right under anchorView
		popupWindow.showAtLocation(v, Gravity.CENTER,0,0);
	}

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            // code to execute when EditText loses focus
            updateExerciseAnswerDB();
        }
    }

    private void updateDayReflectionDB() {
        // Get email of User
        String email = getEmail();
        if (!TextUtils.isEmpty(email)) {
            float stars = ratingBar_Status.getRating();
            String dayReflection = eT_Day_Reflection_Answer.getText().toString();
            setDBDayReflection(email, new DayReflection(stars, dayReflection));
        } else {
            Log.w(TAG, email + " didn't exist, unable to update Reflection Answer");
        }
    }

    private void setDBDayReflection(String email, final DayReflection reflection) {
        // Retrieve goals from DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference("Users");
        final DatabaseReference dayReflectionRef = rootRef.child(email).child("Calendar").child(dateKey).child("Reflection");
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

    private void setDayReflection(String email) {
        // Retrieve goals from DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference("Users");
        DatabaseReference goalTableRef = rootRef.child(email).child("Calendar").child(dateKey).child("Reflection");
        goalTableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DayReflection dailyReflection = dataSnapshot.getValue(DayReflection.class);
                    eT_Day_Reflection_Answer.setText(dailyReflection.getDayReflection());
                    ratingBar_Status.setRating(dailyReflection.getRating());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void updateExerciseAnswerDB() {
        String email = getEmail();
        if (!TextUtils.isEmpty(email)) {
			String exerciseQuestion = tV_Exercise_Entry.getText().toString();
			String exerciseAnswer = eT_Daily_Exercise_Answer.getText().toString();
            setDBExerciseAnswer(email, new DailyExercise(exerciseQuestion, exerciseAnswer));
        } else {
            Log.w(TAG, email + " didn't exist, unable to update Exercise Answer");
        }
    }

    private void setDBExerciseAnswer(String email, final DailyExercise exerciseAnswer) {
        // Retrieve goals from DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference("Users");
        final DatabaseReference exerciseAnswerRef = rootRef.child(email).child("Calendar").child(dateKey).child("ExerciseAnswer");
        exerciseAnswerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
			{
                exerciseAnswerRef.setValue(exerciseAnswer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void setExercise(final String email)
    {
        // Retrieve question # from DB
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference rootRef = database.getReference("Users");
        // if already populated question for today
		final DatabaseReference deQuestionRef = rootRef.child(email).child("Calendar").child(dateKey).child("ExerciseAnswer");
		deQuestionRef.addListenerForSingleValueEvent(new ValueEventListener()
		{
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot)
			{
				if (dataSnapshot.exists())
				{
					// set UI
					tV_Exercise_Entry.setText(dataSnapshot.child("dailyExerciseQuestion").getValue().toString());
					eT_Daily_Exercise_Answer.setText(dataSnapshot.child("dailyExerciseAnswer").getValue().toString());
				}
				else
				{
					// get question from DB
					final DatabaseReference questionRef = rootRef.child(email).child("LastQuestionSeen");
					questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
							if (dataSnapshot.exists())
							{
								int index = dataSnapshot.getValue(int.class);
								Questions question = new Questions();
								String nextQuestion = question.getNextQuestion(index);
								tV_Exercise_Entry.setText(nextQuestion);
								questionRef.setValue(index+1);
							}
							else
							{
								questionRef.setValue(0);
								Questions question = new Questions();
								String nextQuestion = question.getNextQuestion(0);
								tV_Exercise_Entry.setText(nextQuestion);
							}

							updateExerciseAnswerDB();
						}
						@Override
						public void onCancelled(@NonNull DatabaseError databaseError) {
							// Failed to read value
							Log.w(TAG, "Failed to read value.", databaseError.toException());
						}
					});


				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError)
			{
			}
		});
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(goals, getActivity());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initGoals(View view) {
        String email = getEmail();
        if (!TextUtils.isEmpty(email)) {
            setGoalsUI(email, view);
        } else {
            Log.w(TAG, email + " didn't exist, unable to populate goal list");
        }
    }

    private void setGoalsUI(String email, final View view) {
        // Initialize goals list
        goals = new ArrayList<>();
        // Retrieve goals from DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference("Users");
        DatabaseReference goalTableRef = rootRef.child(email).child("Calendar").child(dateKey).child("Goals");
        goalTableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Add goals to goals list
                for (DataSnapshot goalSnapshot : dataSnapshot.getChildren()) {
                    Goals goal = goalSnapshot.getValue(Goals.class);
                    goals.add(goal);
                }
                initRecyclerView(view);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
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
}