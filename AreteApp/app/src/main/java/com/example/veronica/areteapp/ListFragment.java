package com.example.veronica.areteapp;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class ListFragment extends Fragment implements CompoundButton.OnCheckedChangeListener
{
	CustomListAdapter dataAdapter = null;
	ListView listView;
	ArrayList<Goals> goalList;
	CompoundButton switchShowCompletedTasks;
	TextView textViewCompleted;
	private FirebaseAuth mAuth;
	private String TAG = "TAG";
	private String email;
	private String dateKey;

	public ListFragment() {
		// Required empty public constructor
		getKeyFromGCDate(new GregorianCalendar());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootview = inflater.inflate(R.layout.fragment_list, container, false);
		super.onCreate(savedInstanceState);

		// Get Firebase Auth Object
		mAuth = FirebaseAuth.getInstance();
		FirebaseUser user = mAuth.getCurrentUser();
		this.email = LoginActivity.EncodeString(user.getEmail());
		listView = (ListView) rootview.findViewById(R.id.listViewTask);
		switchShowCompletedTasks = (CompoundButton) rootview.findViewById(R.id.switchShowCompletedTasks);
		textViewCompleted = (TextView) rootview.findViewById(R.id.textViewCompleted);
		switchShowCompletedTasks.setOnCheckedChangeListener(this);

		updateGoalsCompleted(0);

		//Generate list View from ArrayList
		displayListView(rootview);
		return rootview;
	}

	private void updateGoalsCompleted(final int update)
	{
		// Retrieve goals from DB
		final FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference rootRef = database.getReference("Users");

		final DatabaseReference goalListRef = rootRef.child(email).child("Calendar").child(dateKey).child("GoalsCompleted");
		goalListRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot)
			{
				if (dataSnapshot.exists())
				{
					int currValue = Integer.valueOf(dataSnapshot.getValue().toString());
					goalListRef.setValue( currValue += update);
					setGoalsCompletedText(currValue);
				}
				else
				{
					goalListRef.setValue(0);
					setGoalsCompletedText(0);
				}

			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				// Failed to read value
				Log.w(TAG, "Failed to read value.", databaseError.toException());
			}
		});
	}

	private void setGoalsCompletedText(int goalsCompleted)
	{
		String newCount = "Show " + Integer.toString(goalsCompleted) + " Completed Tasks";
		textViewCompleted.setText(newCount);
	}

	private void getKeyFromGCDate(GregorianCalendar gcDate)
	{
		Date date = gcDate.getTime();
		SimpleDateFormat sF = new SimpleDateFormat("dd-MM-yyyy");
		this.dateKey = sF.format(date.getTime());
	}

	/**
	 * Displays the Goalss in the list
	 * @param rootview
	 */
	private void displayListView(View rootview)
	{
		//Array list of to-do items

		// junk data
		goalList = new ArrayList<Goals>();
		// Assign adapter to ListView
		updateGoalList();
		getDBGoalList();
	}

	// setOneGoal
	private void setDBGoalList(final Goals Goals) {
		// Retrieve goals from DB
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference rootRef = database.getReference("Users");

		final DatabaseReference goalGoalsRef = rootRef.child(email).child("Calendar").child(dateKey).child("Goals").child(Goals.getGoalName());
		goalGoalsRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				goalGoalsRef.setValue(Goals);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				// Failed to read value
				Log.w(TAG, "Failed to read value.", databaseError.toException());
			}
		});
	}

	// get entire list
	private void getDBGoalList() {
		// Retrieve goals from DB
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference rootRef = database.getReference("Users");

		final DatabaseReference goalListRef = rootRef.child(email).child("Calendar").child(dateKey).child("Goals");
		goalListRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot)
			{
				for (DataSnapshot goalSnapshot: dataSnapshot.getChildren())
				{
					Goals goal = goalSnapshot.getValue(Goals.class);
					goalList.add(goal);
					updateGoalList();
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				// Failed to read value
				Log.w(TAG, "Failed to read value.", databaseError.toException());
			}
		});
	}

	/**
	 * View completed tasks toggle
	 * @param buttonView
	 * @param isChecked
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		// show Completed Tasks when Switch is ON
		// show InCompleted Tasks when Switch if OFF
		for(Goals _goal: goalList)
		{
			if(isChecked)
				_goal.setVisible(true);
			else
				if(_goal.getCompletion())
					_goal.setVisible(false);
		}
		dataAdapter.notifyDataSetChanged();
		listView.setAdapter(dataAdapter);
	}

	/**
	 * Custom Adapter to know when check box clicked
	 */
	private class CustomListAdapter extends ArrayAdapter<Goals>
	{
		private ArrayList<Goals> goalList;

		public CustomListAdapter(Context context, int textViewResourceId, ArrayList<Goals> goalList)
		{
			super(context, textViewResourceId, goalList);
			this.goalList = new ArrayList<Goals>();
			this.goalList.addAll(goalList);
		}

		private class ViewHolder
		{
			TextView text;
			CheckBox checkBox;
		}

		/**
		 *
		 * @param position
		 * @param convertView
		 * @param parent
		 * @return
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder = null;

			if (convertView == null)
			{
				LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = vi.inflate(R.layout.list_item_task, null);

				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.list_item_task_textview);
				holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkTaskButton);
				convertView.setTag(holder);
				final ViewHolder finalHolder = holder;
				final View finalConvertView = convertView;
				final ViewHolder finalHolder1 = holder;
				final View finalConvertView1 = convertView;
				holder.checkBox.setOnClickListener(new View.OnClickListener()
				{
					public void onClick(View v)
					{
						CheckBox cb = (CheckBox) v;
						Goals _goal = (Goals) cb.getTag();

						_goal.setCompletion(cb.isChecked());
						// gray out text
						if (_goal.getCompletion())
						{
							buildAlertDiaglogBox("Nice Work!", "Would you like to reflect on completing this task?", "Done", _goal);
							_goal.setVisible(false);
							completedGoal(finalHolder1, finalConvertView1);
							setDBGoalList(_goal);

						}
						else
						{
							finalHolder.checkBox.setTextColor(Color.BLACK);
							_goal.setVisible(true);
							updateGoalsCompleted(-1);
						}
						setDBGoalList(_goal);
					}
				});


			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}

			Goals goal = goalList.get(position);
			holder.checkBox.setText(goal.getGoalName());
			holder.checkBox.setChecked(goal.getCompletion());
			holder.checkBox.setTag(goal);

			if (goal.getVisible())
			{
				holder.checkBox.setVisibility(View.VISIBLE);
			}
			else if (!goal.getVisible() && !switchShowCompletedTasks.isChecked())
			{
				convertView.setLayoutParams(new AbsListView.LayoutParams(-1,1));
				holder.checkBox.setVisibility(View.INVISIBLE);
			}

			if (goal.getCompletion())
			{
				holder.checkBox.setTextColor(Color.rgb(220, 220, 220));
			}

			return convertView;
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.tasks_fragment, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		switch (id){
			case R.id.action_add_task:

				buildAlertDiaglogBox("Add a task", "What do you want to accomplish?", "Add", null);

				return true;
		}
		return super.onOptionsItemSelected(item);
	}


	/**
	 *
	 * @param title
	 * @param message
	 * @param positiveButton
	 */
	private void buildAlertDiaglogBox(final String title, String message, String positiveButton, final Goals _goal)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(title);
		builder.setMessage(message);
		final EditText inputField = new EditText(getActivity());
		builder.setView(inputField);
		builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface di, int i)
			{
				if (title == "Add a task")
				{
					Goals goal = new Goals(inputField.getText().toString(), false, "", true);
					goalList.add(goal);
					updateGoalList();
					setDBGoalList(goal);
				}
				else if(title == "Nice Work!")
				{
					_goal.setGoalReflectionAnswer(inputField.getText().toString());
					setDBGoalList(_goal);
				}
			}
		});
		builder.setNegativeButton("Cancel", null);

		builder.setIcon(R.drawable.partyhat);
		builder.create().show();


	}

	/**
	 *
	 * @param holder
	 * @param convertView
	 */
	private void completedGoal(final CustomListAdapter.ViewHolder holder, final View convertView)
	{
		holder.checkBox.setTextColor(Color.rgb(220, 220, 220));
		updateGoalsCompleted(1);

		// wait
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run()
			{
				if (!switchShowCompletedTasks.isChecked())
				{
					convertView.setLayoutParams(new AbsListView.LayoutParams(-1, 1));
					holder.checkBox.setVisibility(View.INVISIBLE);
				}
			}
		}, 1000);
	}

	private void updateGoalList()
	{
		dataAdapter = new CustomListAdapter(getActivity(), R.layout.list_item_task, goalList);
		listView.setAdapter(dataAdapter);
	}
}
