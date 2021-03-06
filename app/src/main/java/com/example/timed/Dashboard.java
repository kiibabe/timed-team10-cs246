package com.example.timed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    /*
    * Dashboard
    * sets the total up time & app list (recycler view) for today
    */

    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // set a tool bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // radio button
        RadioGroup toggle = (RadioGroup) findViewById(R.id.toggle);
        RadioButton rd1 = findViewById(R.id.radioWeek);
        RadioButton rd2 = findViewById(R.id.radioToday);
        toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rd1.isChecked()) {
                    Intent intent = new Intent(getApplication(), WeekView.class);
                    startActivity(intent);
                }
            }
        });

        // gettind data from AppDataManager
        AppDataManager usageMgr = new AppDataManager(this);
        List<AppDataManager.AppUsage> usageList = usageMgr.getUsageForToDay();

        // app name list
        ArrayList<String> appName = new ArrayList<>();
        // app usage list
        ArrayList<String> appTime = new ArrayList<>();
        // app icon list
        ArrayList<Drawable> appIcon = new ArrayList<>();

        long totalTimeMs = 0;

        // adding the data
        for(AppDataManager.AppUsage usage : usageList)
        {
            appName.add(usage.name);
            appTime.add(DateUtils.formatElapsedTime(usage.usageMs / 1000));
            totalTimeMs = totalTimeMs + usage.usageMs;
            appIcon.add(usage.icon);
        }

        // set total up time
        TextView totalTime = (TextView) findViewById(R.id.total_usage);
        totalTime.setText("Total Up Time: " + DateUtils.formatElapsedTime(totalTimeMs / 1000));

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvToday);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, appName, appTime, appIcon);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


    }

    public void onItemClick(final View view, int position) {
        // setContentView(R.layout.recyclerview_row);
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), PerAppView.class);
        view.getContext().startActivity(intent);
    }

}