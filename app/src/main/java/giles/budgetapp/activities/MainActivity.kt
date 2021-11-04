package giles.budgetapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import giles.budgetapp.AppData
import giles.budgetapp.R
import giles.budgetapp.views.BudgetViewAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: BudgetViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar_main))

        //Set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_budgets_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BudgetViewAdapter(AppData.budgets)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // TODO: Open settings
            true
        }

        R.id.action_add_budget -> {
            // Open activity to add new budget
            val intent = Intent(this, EditBudgetActivity::class.java)
            startActivity(intent)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onResume(){
        super.onResume()
        adapter.notifyDataSetChanged()
        Log.d("LOGLOGLOGLOGLOG", "Here")
        for (budget in AppData.budgets) {
            Log.d("LOGLOGLOGLOGLOG", budget.name)
        }
    }
}