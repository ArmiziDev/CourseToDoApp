package com.example.coursetodoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private static final String FILE_NAME = "assignments.json";
    private static final String TAG = "MainActivity";
    Toolbar myToolbar;
    private final List<Assignment> assignmentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AssignmentAdapter assignmentAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Toolbar Setup
        myToolbar = findViewById(R.id.toolbar_main);
        myToolbar.setTitle("CourseToDoApp");
        myToolbar.setSubtitle("To Do: ");
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        // Recycler Setup
        recyclerView = findViewById(R.id.recycler);
        assignmentAdapter = new AssignmentAdapter(assignmentList, this);
        recyclerView.setAdapter(assignmentAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Activity Data Transfer
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);

    }

    private void updateToolbarSubtitle()
    {
        myToolbar.setSubtitle("To Do: " + assignmentList.size());
    }

    public void handleResult(ActivityResult result)
    {
        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }
        Intent data = result.getData();
        if (result.getResultCode() == RESULT_OK)
        {
            Assignment assignment = (Assignment) data.getSerializableExtra("SavedAssignment");
            if (data.getBooleanExtra("Edit", false))
            {
                assignmentList.remove(selectedPosition);
                assignmentAdapter.notifyItemRemoved(selectedPosition);
            }
            assignmentList.add(0, assignment);
            assignmentAdapter.notifyItemInserted(0);

            saveAssignmentList();
        }
        updateToolbarSubtitle();
    }
    public void editAssignment(Assignment a)
    {
        Intent intent= new Intent(MainActivity.this, AssignmentActivity.class);
        intent.putExtra("Assignment", a);
        intent.putExtra("Edit", true);

        activityResultLauncher.launch(intent);
    }
    private void showInformation()
    {
        Intent intent = new Intent(this, InformationActivity.class);
        startActivity(intent);
    }
    private void addAssignment()
    {
        Intent intent= new Intent(this, AssignmentActivity.class);
        intent.putExtra("Edit", false);

        activityResultLauncher.launch(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_button) {
            addAssignment();
            return true;
        } else if (item.getItemId() == R.id.info_button) {
            showInformation();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onClick(View v)
    {
        // Get Assignment
        int pos = recyclerView.getChildLayoutPosition(v);
        selectedPosition = pos;
        Assignment assignment = assignmentList.get(pos);
        // Edit Assignment
        editAssignment(assignment);
    }
    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        selectedPosition = pos;
        assignmentList.remove(pos);
        assignmentAdapter.notifyItemRemoved(pos);
        updateToolbarSubtitle();
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        assignmentList.clear();

        ArrayList<Assignment> temp = loadFile();
        if (temp != null) {
            assignmentList.addAll(temp);
            assignmentAdapter.notifyDataSetChanged();
        }
        updateToolbarSubtitle();
    }
    @Override
    protected void onPause() { // Going to be partially or fully hidden
        saveAssignmentList();
        super.onPause();
    }
    // Json File
    private ArrayList<Assignment> loadFile()
    {
        Log.d(TAG, "loadFile: Loading JSON File");
        try {
            InputStream is = getApplicationContext().openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) !=  null)
            {
                sb.append(line);
            }

            if (sb.length() == 0)
            {
                Log.d(TAG, "loadFile: No File Content");
                return null;
            }

            Gson gson = new Gson();
            Assignment[] assignments = gson.fromJson(sb.toString(), Assignment[].class);
            ArrayList<Assignment> tempList = new ArrayList<>();
            Collections.addAll(tempList, assignments);

            return tempList;
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "No JSON File", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "loadFile: " + e.getMessage());
        }
        return null;
    }
    private void saveAssignmentList()
    {
        Log.d(TAG, "saveAssignment: Saving JSON File");

        Gson gson = new Gson();
        String json = gson.toJson(assignmentList);

        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(FILE_NAME, Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(json);
            printWriter.close();
            fos.close();

            Log.d(TAG, "saveAssignmentList: " + json);
            //Toast.makeText(this, "Assignments Saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}