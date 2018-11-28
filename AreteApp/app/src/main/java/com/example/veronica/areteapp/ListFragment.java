package com.example.veronica.areteapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListFragment extends Fragment
{

	CustomListAdapter dataAdapter = null;

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
		ArrayList<ListItem> itemList = new ArrayList<ListItem>();
		ListItem _items = new ListItem("Finish App",false);
		itemList.add(_items);
		_items = new ListItem("Groceries",false);
		itemList.add(_items);
		_items = new ListItem("Walk Dog",false);
		itemList.add(_items);
		//create an ArrayAdaptar from the String Array
		dataAdapter = new CustomListAdapter(getActivity(), R.layout.list_item_task, itemList);
		ListView listView = (ListView) rootview.findViewById(R.id.listViewTask);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);
	}

	/**
	 * Custom Adapter to know when check box clicked
	 */
	private class CustomListAdapter extends ArrayAdapter<ListItem>
	{
		private ArrayList<ListItem> itemList;

		public CustomListAdapter(Context context, int textViewResourceId, ArrayList<ListItem> stateList)
		{
			super(context, textViewResourceId, stateList);
			this.itemList = new ArrayList<ListItem>();
			this.itemList.addAll(stateList);
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

			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null)
			{
				LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = vi.inflate(R.layout.list_item_task, null);

				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.list_item_task_textview);
				holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkTaskButton);

				convertView.setTag(holder);

				holder.checkBox.setOnClickListener( new View.OnClickListener()
				{
					public void onClick(View v)
					{
						CheckBox cb = (CheckBox) v;
						ListItem _item = (ListItem) cb.getTag();

						Toast.makeText(getActivity(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(),
								Toast.LENGTH_LONG).show();

						_item.setSelected(cb.isChecked());
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

}