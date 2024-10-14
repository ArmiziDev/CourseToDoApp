package com.example.coursetodoapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.os.BuildCompat;

public class AssignmentActivity extends AppCompatActivity
{
    private static final String TAG = "AssignmentActivity";
    Toolbar myToolbar;
    EditText course_name;
    EditText assignment_name;
    EditText message_box;
    boolean Edit;
    Assignment originalAssignment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d(TAG, "onCreate: Registering OnBackInvokedCallback");
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    this::backInvoked
            );
        } else {
            getOnBackPressedDispatcher().addCallback(
                    new OnBackPressedCallback(true) {
                        @Override
                        public void handleOnBackPressed() {
                            backInvoked();
                        }
                    }
            );
        }

        // Toolbar
        myToolbar = findViewById(R.id.toolbar_assignment);
        myToolbar.setTitle("CourseToDoApp");
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        // Grab IDS
        course_name = findViewById(R.id.course_name_editText);
        assignment_name = findViewById(R.id.assignment_title_editText);
        message_box = findViewById(R.id.editTextTextMultiLine);

        //Intent
        Intent intent = getIntent();
        if (intent.hasExtra("Assignment"))
        {
            Assignment assignment;
            assignment = (Assignment) intent.getSerializableExtra("Assignment");
            assert assignment != null;
            course_name.setText(assignment.getClassName());
            assignment_name.setText(assignment.getTitle());
            message_box.setText(assignment.getBoxText());
            originalAssignment = assignment;
        }
        Edit = intent.getBooleanExtra("Edit", false);
    }

    private void goBack(boolean save_assignment)
    {
        if (save_assignment)
        {
            Assignment assignment = new Assignment(
                    String.valueOf(course_name.getText()),
                    String.valueOf(assignment_name.getText()),
                    String.valueOf(message_box.getText()));
            Intent data = new Intent();

            data.putExtra("SavedAssignment", assignment);
            data.putExtra("Edit", Edit);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    private void backInvoked() {
        if (originalAssignment != null &&
        originalAssignment.getTitle().equals(String.valueOf(assignment_name.getText())) &&
        originalAssignment.getClassName().equals(String.valueOf(course_name.getText())) &&
        originalAssignment.getBoxText().equals(String.valueOf(message_box.getText())))
        {
            Toast.makeText(this, "No Changes Made", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else if (
                String.valueOf(assignment_name.getText()).isEmpty() &&
                String.valueOf(course_name.getText()).isEmpty() &&
                String.valueOf(message_box.getText()).isEmpty())
        {
            Toast.makeText(this, "Empty item not saved", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your Data is Not Saved!");
        builder.setMessage("Do you want to save this data?");
        builder.setPositiveButton("Yes", (dialog, id) -> goBack(true));
        builder.setNegativeButton("No", (dialog, id) -> finish());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void noTitleDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("OK", (dialog, id) -> finish());
        builder.setNegativeButton("CANCEL", (dialog, id) -> dialog.dismiss());

        builder.setMessage("Do you want to exit without saving?");
        builder.setTitle("Title is Empty");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Toobar
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_assignment, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.info_button) {
            if (String.valueOf(assignment_name.getText()).isEmpty()) // title is empty
            {
                noTitleDialog();
                return true;
            }
            goBack(true);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
