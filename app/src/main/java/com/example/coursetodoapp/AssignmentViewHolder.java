package com.example.coursetodoapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AssignmentViewHolder extends RecyclerView.ViewHolder {

    public TextView class_name;
    TextView assignment_title;
    TextView assignment_box;
    TextView date_time;

    public AssignmentViewHolder(@NonNull View itemView) {
        super(itemView);

        this.class_name = itemView.findViewById(R.id.className);
        this.assignment_title = itemView.findViewById(R.id.assignment_title);
        this.assignment_box = itemView.findViewById(R.id.assignment_text);
        this.date_time = itemView.findViewById(R.id.dateTime);

    }
}
