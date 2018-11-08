package com.example.veronica.areteapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment
{
    ArrayAdapter<String> mTaskAdapter;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootview = inflater.inflate(R.layout.fragment_list, container, false);
        //Create the fake data
        String[] fakeData = {
                "Revise for exam",
                "Buy milk",
                "Do laundry",
                "Call Melissa",
                "Buy stamps",
        };
        List<String> tasks = new ArrayList<String>(Arrays.asList(fakeData));

        //Create the ArrayAdapter by specifying context, "how" (layout),"where" (textview), and " what" (data)
        mTaskAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_task,
                R.id.list_item_task_textview,
                tasks);

        //Still need to bind adapter to the ListView
        ListView listView = (ListView) rootview.findViewById(R.id.listViewTask);
        listView.setAdapter(mTaskAdapter);

        // Inflate the layout for this fragment
        return rootview;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add a task");
                builder.setMessage("What do you want to do?");
                final EditText inputField = new EditText(getActivity());
                builder.setView(inputField);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface di, int i) {
                        Toast.makeText(getActivity(), inputField.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.create().show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
