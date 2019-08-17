package c.giles.budgetappv11;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import c.giles.budgetappv11.ui.main.SectionsPagerAdapter;
import c.giles.budgetappv11.views.HistoryFragment;
import c.giles.budgetappv11.views.SectionsPageAdapter;
import c.giles.budgetappv11.views.TrendsFragment;

public class HistoryActivity extends AppCompatActivity {

    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    private HistoryFragment historyFragment;
    private TrendsFragment trendsFragment;
    SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("History");

        Toolbar toolbar = (Toolbar) findViewById(R.id.history_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        setupViewPager(viewPager);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());



    }

    private void setupViewPager(ViewPager viewPager){
        historyFragment = new HistoryFragment();
        trendsFragment = new TrendsFragment();
        adapter.addFragment(historyFragment, "History");
        adapter.addFragment(trendsFragment, "Trends");

        viewPager.setAdapter(adapter);
    }

    public void clearHistory(View v){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        HistoryManager.setHistoryDataList(new ArrayList<HistoryData>());
                        HistoryManager.setHistoryDeleted(true);
                        saveSharedPreferences();
                        historyFragment.refresh();
                        trendsFragment.refresh();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //Do nothing
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setMessage("Are you sure you want to clear all history items?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show()
        ;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void saveSharedPreferences(){
        SharedPreferences historyData = getSharedPreferences("history data", MODE_PRIVATE);
        SharedPreferences.Editor historyEditor = historyData.edit();
        Gson gson = new Gson();

        List<HistoryData> historyTemp = new ArrayList<>(HistoryManager.getHistoryDataList());
        List<HistoryData> totalHistoryTemp = new ArrayList<>(HistoryManager.getTotalDataList());
        String historyJson = gson.toJson(historyTemp);
        String totalJson = gson.toJson(totalHistoryTemp);
        historyEditor.putString("history list", historyJson);
        historyEditor.putString("total history list", totalJson);
        historyEditor.apply();
    }

}