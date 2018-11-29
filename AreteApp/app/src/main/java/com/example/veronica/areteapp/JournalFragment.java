package com.example.veronica.areteapp;


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
    private FirebaseAuth mAuth;
    private String TAG = "TAG";

    public JournalFragment() {
        // Required empty public constructor
    }

    /**
     *
     * Found ToDo list code here:
     * http://muggingsg.com/university/android-app-tutorial-todo-app-using-fragments/#2_Creating_a_List_of_Tasks
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_journal, container, false);
        // Retrieve UI Elements
        eT_Date = rootview.findViewById(R.id.tV_Date);
        bT_Submit = rootview.findViewById(R.id.bt_Submit);
        bT_Submit.setOnClickListener(this);
        // Get Firebase Auth Object
        mAuth = FirebaseAuth.getInstance();

        // Set today's date
        setDate();

        // Set up Recycler Adapter/View


//
//        //Create the fake data
//        String[] fakeData = {
//                "Finish App",
//                "Groceries",
//                "Walk Dog",
//        };
//        List<String> tasks = new ArrayList<String>(Arrays.asList(fakeData));
//
//        //Create the ArrayAdapter by specifying context, "how" (layout),"where" (textview), and " what" (data)
//        mTaskAdapter = new ArrayAdapter<String>(
//                getActivity(),
//                R.layout.list_item_task,
//                R.id.list_item_task_textview,
//                tasks);
//
//        //Still need to bind adapter to the ListView
//        ListView listView = (ListView) rootview.findViewById(R.id.listViewTask);
//        listView.setAdapter(mTaskAdapter);

        // Inflate the layout for this fragment
        return rootview;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSubmit:
                Toast.makeText(getActivity(), "Thanks for submitting your reflection for today!", Toast.LENGTH_LONG).show();
        }
    }

    private void setDate() {
        Calendar cal=Calendar.getInstance();
        eT_Date.setText(android.text.format.DateFormat.format("MMMM dd, yyyy", new java.util.Date()));
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(goals, getActivity());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

//    private void initGoals() {
//        String email = getEmail();
//        if (!TextUtils.isEmpty(email)) {
//            String calendarKey = getCKey(email);
//        }
//    }
//
//    private String getEmail() {
//        String email;
//        // get user email
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            email = EncodeString(user.getEmail());
//        } else {
//            email = null;
//        }
//        return email;
//    }
//
//    private String getCKey(String email) {
//        getDBVal("Users", email, "calendarKey");
//        goals = new ArrayList<>();
//        return null;
//    }
//
//    private void getDBVal(String table, String ref, String key) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference tableRef = database.getReference(table);
//        DatabaseReference keyRef = tableRef.child(ref).child(key);
//        keyRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    String cKey = dataSnapshot.getValue(String.class);
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", databaseError.toException());
//            }
//        });
//    }
}
