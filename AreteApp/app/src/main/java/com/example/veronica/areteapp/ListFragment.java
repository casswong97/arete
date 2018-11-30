package com.example.veronica.areteapp;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ListFragment extends Fragment implements CompoundButton.OnCheckedChangeListener
{

	CustomListAdapter dataAdapter = null;
	ListView listView;
	ArrayList<ListItem> itemList;
	CompoundButton switchShowCompletedTasks;
	TextView textViewCompleted;
	int totalCompleted = 0;
	private FirebaseAuth mAuth;
	private String TAG = "TAG";
	private String email;
	private String todayDate;

	public ListFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootview = inflater.inflate(R.layout.fragment_list, container, false);
		super.onCreate(savedInstanceState);

		listView = (ListView) rootview.findViewById(R.id.listViewTask);

		// Get Firebase Auth Object
		mAuth = FirebaseAuth.getInstance();
		FirebaseUser user = mAuth.getCurrentUser();
		this.email = LoginActivity.EncodeString(user.getEmail());

		Date c = Calendar.getInstance().getTime();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		this.todayDate = df.format(c);

		totalCompleted = 0;
		switchShowCompletedTasks = (CompoundButton) rootview.findViewById(R.id.switchShowCompletedTasks);
		textViewCompleted = (TextView) rootview.findViewById(R.id.textViewCompleted);

		switchShowCompletedTasks.setOnCheckedChangeListener(this);

		//Generate list View from ArrayList
		displayListView(rootview);
		return rootview;
	}

	/**
	 * Displays the ListItems in the list
	 * @param rootview
	 */
	private void displayListView(View rootview)
	{
		//Array list of to-do items

		// junk data
		itemList = new ArrayList<ListItem>();
		// Assign adapter to ListView
		updateItemList();
		getDBListItems();
	}

	// setOneGoal
	private void setDBListItem(final ListItem listItem) {
		// Retrieve goals from DB
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference rootRef = database.getReference("Users");

		final DatabaseReference goalListItemRef = rootRef.child(email).child("Calendar").child(todayDate).child("Goals").child(listItem.getText());
		goalListItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				goalListItemRef.setValue(listItem);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				// Failed to read value
				Log.w(TAG, "Failed to read value.", databaseError.toException());
			}
		});
	}

	// get entire list
	private void getDBListItems() {
		// Retrieve goals from DB
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference rootRef = database.getReference("Users");

		final DatabaseReference goalListRef = rootRef.child(email).child("Calendar").child(todayDate).child("Goals");
		goalListRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot)
			{
				for (DataSnapshot goalSnapshot: dataSnapshot.getChildren())
				{
					ListItem item = goalSnapshot.getValue(ListItem.class);
					itemList.add(item);
					updateItemList();
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
		for(ListItem _item: itemList)
		{
			if(isChecked)
				_item.setVisible(true);
			else
				if(_item.isSelected())
					_item.setVisible(false);
		}
		dataAdapter.notifyDataSetChanged();
		listView.setAdapter(dataAdapter);
	}

	/**
	 * Custom Adapter to know when check box clicked
	 */
	private class CustomListAdapter extends ArrayAdapter<ListItem>
	{
		private ArrayList<ListItem> itemList;

		public CustomListAdapter(Context context, int textViewResourceId, ArrayList<ListItem> itemList)
		{
			super(context, textViewResourceId, itemList);
			this.itemList = new ArrayList<ListItem>();
			this.itemList.addAll(itemList);
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
						ListItem _item = (ListItem) cb.getTag();

						_item.setSelected(cb.isChecked());
						// gray out text
						if (_item.isSelected())
						{
							buildAlertDiaglogBox("Nice Work!", "Would you like to reflect on completing this task?", "Done");
							_item.setVisible(false);
							completedGoal(finalHolder1, finalConvertView1);

						}
						else
						{
							finalHolder.checkBox.setTextColor(Color.BLACK);
							_item.setVisible(true);
							totalCompleted-=1;
						}
						setDBListItem(_item);
					}
				});

			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}

			ListItem item = itemList.get(position);
			holder.checkBox.setText(item.getText());
			holder.checkBox.setChecked(item.isSelected());
			holder.checkBox.setTag(item);

			if (item.isVisible())
			{
				holder.checkBox.setVisibility(View.VISIBLE);
			}
			else if (!item.isVisible() && !switchShowCompletedTasks.isChecked())
			{
				convertView.setLayoutParams(new AbsListView.LayoutParams(-1,1));
				holder.checkBox.setVisibility(View.INVISIBLE);
			}

			if (item.isSelected())
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

				buildAlertDiaglogBox("Add a task", "What do you want to accomplish?", "Add");

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
	private void buildAlertDiaglogBox(final String title, String message, String positiveButton)
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
					ListItem item = new ListItem(inputField.getText().toString(), false, true);
					itemList.add(item);
					updateItemList();
					setDBListItem(item);
				}
				else if(title == "Nice Work!")
				{
					// add to journal entry
				}
			}
		});
		builder.setNegativeButton("Cancel", null);
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
		totalCompleted+=1;
		String newCount = "Show " + Integer.toString(totalCompleted) + " Completed Tasks";
		textViewCompleted.setText(newCount);
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

	private void updateItemList()
	{
		dataAdapter = new CustomListAdapter(getActivity(), R.layout.list_item_task, itemList);
		listView.setAdapter(dataAdapter);
	}
}