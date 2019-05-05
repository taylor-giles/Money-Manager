package c.giles.budgetappv11;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView pageTitle;
    List<Budget> budgets = new ArrayList<>();
    List<LinearLayout> budgetLayouts = new ArrayList<>();
    List<TextView> budgetTitles = new ArrayList<>();
    List<TextView> budgetDisplays = new ArrayList<>();
    List<Button> budgetPlusButtons = new ArrayList<>();
    List<Button> budgetMinusButtons = new ArrayList<>();
    List<ImageButton> renameButtons = new ArrayList<>();
    List<ImageButton> deleteButtons = new ArrayList<>();

    private LinearLayout verticalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        verticalLayout = (LinearLayout)findViewById(R.id.verticalLayout);

        Button addButton = (Button) findViewById(R.id.addBudgetButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //verticalLayout.addView((Button)findViewById(R.id.addBudgetButton));
                //makeBudget(view);
                //deposit(view);
                LayoutInflater inflater = getLayoutInflater();
                View budgetDisplay = inflater.inflate(R.layout.budget_layout, verticalLayout);
                //verticalLayout.addView(budgetDisplay);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                addBudget(BridgeClass.getNewBudget());
            }
        }
    }

    public void addBudget(Budget newBudget){
        LayoutInflater inflater = getLayoutInflater();
        View budgetDisplay = inflater.inflate(R.layout.budget_layout, null);

        //TODO figure out how to get these buttons and stuff to work; maybe a separate class for the budgetDisplay?
        final TextView budgetName = (TextView)budgetDisplay.findViewById(R.id.nameDisplay);
        TextView currentBudget = (TextView)budgetDisplay.findViewById(R.id.moneyDisplay);
        TextView partitionDisplay = (TextView)budgetDisplay.findViewById(R.id.partitionDisplay);
        ImageButton renameButton = (ImageButton)budgetDisplay.findViewById(R.id.renameButton);
        ImageButton deleteButton = (ImageButton)budgetDisplay.findViewById(R.id.deleteButton);
        Button depositButton = (Button)budgetDisplay.findViewById(R.id.addButton);
        Button withdrawButton = (Button)budgetDisplay.findViewById(R.id.subtractButton);

        budgetName.setText(newBudget.getBudgetName());
        currentBudget.setText(String.format("%.2f",newBudget.getBudget()));
        if(newBudget.isPartitioned()){
            String temp = "";
            if(newBudget.isAmountBased()){
                temp += "$";
            }
            temp += String.format("%.2f", newBudget.getPartitionValue());
            if(!newBudget.isAmountBased()){
                temp += "%";
            }
            temp += " of P.C.";
            partitionDisplay.setText(temp);
        }


        budgetTitles.add(budgetName);
        budgetDisplays.add(currentBudget);
        renameButtons.add(renameButton);
        deleteButtons.add(deleteButton);
        budgetPlusButtons.add(depositButton);
        budgetMinusButtons.add(withdrawButton);

        renameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //TODO Make "rename" pop-up
                budgetName.setText("Billy");
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //TODO Make "delete" pop-up
            }
        });


        depositButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                deposit(view);
            }
        });


        withdrawButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //TODO Make "withdraw" pop-up
            }
        });

        verticalLayout.addView(budgetDisplay);
    }

    public void makeBudget(View view){
        Intent makeNewBudget = new Intent(this, BudgetCreateActivity.class);
        startActivityForResult(makeNewBudget, 1);
    }

    public void deposit(View view){
        Intent deposit = new Intent(this, DepositActivity.class);
        startActivityForResult(deposit, 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
