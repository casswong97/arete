package com.example.veronica.areteapp;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListFragment extends Fragment
{

	CustomListAdapter dataAdapter = null;
	ListView listView;
	ArrayList<ListItem> itemList;

	public ListFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootview = inflater.inflate(R.layout.fragment_list, container, false);
		super.onCreate(savedInstanceState);
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
		itemList = new ArrayList<ListItem>();
		ListItem _items = new ListItem("Finish App",false);
		itemList.add(_items);
		_items = new ListItem("Groceries",false);
		itemList.add(_items);
		_items = new ListItem("Walk Dog",false);
		itemList.add(_items);
		//create an ArrayAdaptar from the String Array
		dataAdapter = new CustomListAdapter(getActivity(), R.layout.list_item_task, itemList);
		listView = (ListView) rootview.findViewById(R.id.listViewTask);
		// Assign adapter to ListView
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
				holder.checkBox.setOnClickListener(new View.OnClickListener()
				{
					public void onClick(View v)
					{
						CheckBox cb = (CheckBox) v;
						ListItem _item = (ListItem) cb.getTag();

						if (!_item.isSelected())
						{
							buildAlertDiaglogBox("Nice Work!", "Would you like to reflect on completing this task?", "Done");
						}

						_item.setSelected(cb.isChecked());
						// gray out text
						if (_item.isSelected())
						{
							finalHolder.checkBox.setTextColor(Color.rgb(220, 220, 220));
						}
						else
							finalHolder.checkBox.setTextColor(Color.BLACK);
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
					ListItem item = new ListItem(inputField.getText().toString(), false);
					itemList.add(item);
					dataAdapter = new CustomListAdapter(getActivity(), R.layout.list_item_task, itemList);
					listView.setAdapter(dataAdapter);
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

}