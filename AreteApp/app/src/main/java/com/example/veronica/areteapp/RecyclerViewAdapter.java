package com.example.veronica.areteapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Goals> goals = new ArrayList<>();
    private Context mContext;

    RecyclerViewAdapter(ArrayList<Goals> goals, Context mContext) {
        this.goals = goals;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_journal_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.goalName.setText(goals.get(i).getGoalName());
        viewHolder.reflection.setText(goals.get(i).getGoalReflectionAnswer());
        if (goals.get(i).getCompletion()) {
            viewHolder.finishGoal.setChecked(true);
        } else {
            viewHolder.finishGoal.setChecked(false);
        }
//        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, goals.get(i).goalName, Toast.LENGTH_SHORT);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView goalName;
        EditText reflection;
        CheckBox finishGoal;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            goalName = itemView.findViewById(R.id.tV_Goal_Name);
            reflection = itemView.findViewById(R.id.eT_Goal_Reflection);
            finishGoal = itemView.findViewById(R.id.checkbox_Goal);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
