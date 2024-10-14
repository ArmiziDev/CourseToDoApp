package com.example.coursetodoapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentViewHolder>{

    private static final String TAG = "AssignmentAdapter";

    private final List<Assignment> assignmentList;
    private final MainActivity mainAct;

    AssignmentAdapter(List<Assignment> empList, MainActivity ma)
    {
        this.assignmentList = empList;
        this.mainAct = ma;
    }


    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Making New MyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assignment_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new AssignmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position)
    {
        Log.d(TAG, "onBindViewHolder: Filling New View Holder Assignment " + position);

        Assignment assignment = assignmentList.get(position);

        holder.class_name.setText(assignment.getClassName());
        holder.assignment_title.setText(assignment.getTitle());
        if (assignment.getBoxText().length() > 80)
        {
            String newTextBox = assignment.getBoxText().substring(0, 80) + "...";
            holder.assignment_box.setText(newTextBox);
        }
        else {
            holder.assignment_box.setText(assignment.getBoxText());
        }
        holder.date_time.setText(assignment.getDateTime());
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }
}
