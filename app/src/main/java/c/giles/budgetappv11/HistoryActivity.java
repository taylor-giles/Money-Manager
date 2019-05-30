package c.giles.budgetappv11;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import c.giles.budgetappv11.ui.main.SectionsPagerAdapter;
import c.giles.budgetappv11.views.HistoryFragment;
import c.giles.budgetappv11.views.SectionsPageAdapter;

public class HistoryActivity extends AppCompatActivity {

    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        setupViewPager(viewPager);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());



    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new HistoryFragment(), "History");
        //adapter.addFragment(new TrendsFragment(), "Trends");
        //TODO uncomment this line^ when Trends fragment is finished

        viewPager.setAdapter(adapter);
    }
}