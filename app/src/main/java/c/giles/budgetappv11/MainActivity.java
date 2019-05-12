package c.giles.budgetappv11;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import c.giles.budgetappv11.views.BudgetLayout;
import c.giles.budgetappv11.views.DepositDialog;
import c.giles.budgetappv11.views.WithdrawDialog;

public class MainActivity extends AppCompatActivity implements DepositDialog.DepositDialogListener, WithdrawDialog.WithdrawDialogListener {

    TextView pageTitle;
    List<Budget> budgets = new ArrayList<>();
    List<LinearLayout> budgetLayouts = new ArrayList<>();
    List<TextView> budgetTitles = new ArrayList<>();
    List<TextView> budgetDisplays = new ArrayList<>();
    List<Button> budgetPlusButtons = new ArrayList<>();
    List<Button> budgetMinusButtons = new ArrayList<>();
    List<ImageButton> renameButtons = new ArrayList<>();
    List<ImageButton> deleteButtons = new ArrayList<>();
    int placeholder = -1;

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
        BridgeClass.setBudgetDisplayWindow(verticalLayout);
        budgets = new ArrayList<>(BridgeClass.getBudgetList());
        for(int i = 0; i < budgets.size(); i++){
            addBudget(budgets.get(i), i);
        }

        Button addButton = (Button) findViewById(R.id.addBudgetButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //addBudget(new Budget("Test", 5,false, false, 5));
                //verticalLayout.addView((Button)findViewById(R.id.addBudgetButton));
                makeBudget(view);
                //deposit(view);
                //verticalLayout.addView(budgetDisplay);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                //addBudget(BridgeClass.getNewBudget());
            }
        }
    }

    private void refresh(){
        //budgets = new ArrayList<>(BridgeClass.getBudgetList());
        for(int i = 0; i < budgets.size(); i++){
            budgets.get(i).refresh();
        }
    }

    public void addBudget(Budget newBudget, int index){
        final int i = index;

        LinearLayout budgetLayout = new LinearLayout(this);
        budgetLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        budgetLayout.setOrientation(LinearLayout.HORIZONTAL);
        if(index % 2 != 0){
            budgetLayout.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
        //budgetLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        ImageButton editButton = new ImageButton(this);
        ImageButton deleteButton = new ImageButton(this);
        Button depositButton = new Button(this);
        Button withdrawButton = new Button(this);
        final TextView nameView = new TextView(this);
        final TextView moneyView = new TextView(this);
        final TextView partitionView = new TextView(this);


        editButton.setLayoutParams(new ViewGroup.LayoutParams(75, ViewGroup.LayoutParams.WRAP_CONTENT));
        editButton.setImageResource(android.R.drawable.ic_menu_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budgets.get(i).setName("George");
                nameView.setText("George");
                //TODO replace "George" with something meaningful (i.e. make the Rename dialog and insert that result here)
            }
        });

        deleteButton.setLayoutParams(new ViewGroup.LayoutParams(75, ViewGroup.LayoutParams.WRAP_CONTENT));
        deleteButton.setImageResource(android.R.drawable.ic_menu_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO make some delete action
            }
        });

        depositButton.setLayoutParams(new ViewGroup.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT));
        depositButton.setText("+");
        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeholder = i;
                openDepositDialog();
            }
        });

        withdrawButton.setLayoutParams(new ViewGroup.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT));
        withdrawButton.setText("-");
        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeholder = i;
                openWithdrawDialog();
            }
        });

        nameView.setText(newBudget.getBudgetName());
        nameView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        nameView.setTextColor(getResources().getColor(android.R.color.black));
        nameView.setTextSize(16f);

        moneyView.setText("$" + String.format("%.02f", newBudget.getBudget()));
        moneyView.setTextColor(getResources().getColor(android.R.color.black));
        moneyView.setTextSize(16f);

        if(newBudget.isPartitioned()){
            String temp = "";
            if(newBudget.isAmountBased()){
                temp += "$";
            }
            temp += String.format("%.02f", newBudget.getPartitionValue());
            if(!newBudget.isAmountBased()){
                temp += "%";
            }
            temp += " of P.C.";
            partitionView.setText(temp);
        }

        //The name view and the innerInner layout go inside this layout
        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        innerLayout.setOrientation(LinearLayout.VERTICAL);

        //The money and partition views go inside this layout
        LinearLayout innerInnerLayout = new LinearLayout(this);
        innerInnerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        innerInnerLayout.setOrientation(LinearLayout.HORIZONTAL);

        //The deposit and withdraw buttons go inside this layout
        LinearLayout buttonLayout = new LinearLayout(this);

        //This right-aligns the buttonLayout within the budgetLayout
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = RelativeLayout.ALIGN_PARENT_RIGHT;
        buttonLayout.setLayoutParams(layoutParams);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView fillerText = new TextView(this);
        fillerText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        fillerText.setText("   |   ");

        Space space = new Space(this);
        space.setLayoutParams(new LinearLayout.LayoutParams(0,0,1));

        innerInnerLayout.addView(moneyView);
        if(newBudget.isPartitioned()) {
            innerInnerLayout.addView(fillerText);
            innerInnerLayout.addView(partitionView);
        }
        innerLayout.addView(nameView);
        innerLayout.addView(innerInnerLayout);

        buttonLayout.addView(depositButton);
        buttonLayout.addView(withdrawButton);

        budgetLayout.addView(editButton);
        budgetLayout.addView(deleteButton);
        budgetLayout.addView(innerLayout);
        budgetLayout.addView(space);
        budgetLayout.addView(buttonLayout);

        budgets.get(i).setNameDisplay(nameView);
        budgets.get(i).setAmountDisplay(moneyView);
        budgets.get(i).setPartitionDisplay(partitionView);
        budgets.get(i).setEditButton(editButton);
        budgets.get(i).setDeleteButton(deleteButton);
        budgets.get(i).setDepositButton(depositButton);
        budgets.get(i).setWithdrawButton(withdrawButton);

        budgetLayouts.add(budgetLayout);
        verticalLayout.addView(budgetLayout);
//        LayoutInflater inflater = getLayoutInflater();
//        View budgetDisplay = inflater.inflate(R.layout.budget_layout, verticalLayout);
//        budgetLayouts.add((LinearLayout)budgetDisplay);

//        //TODO figure out how to get these buttons and stuff to work; maybe a separate class for the budgetDisplay?
//        TextView budgetName = (TextView)budgetDisplay.findViewById(R.id.nameDisplay);
//        TextView amountDisplay = (TextView)budgetDisplay.findViewById(R.id.moneyDisplay);
//        TextView partitionDisplay = (TextView)budgetDisplay.findViewById(R.id.partitionDisplay);
//        ImageButton editButton = (ImageButton)budgetDisplay.findViewById(R.id.editButton);
//        ImageButton deleteButton = (ImageButton)budgetDisplay.findViewById(R.id.deleteButton);
//        Button depositButton = (Button)budgetDisplay.findViewById(R.id.addButton);
//        Button withdrawButton = (Button)budgetDisplay.findViewById(R.id.subtractButton);
//
//        budgetName.setText(newBudget.getBudgetName());
//        amountDisplay.setText(String.format("%.2f",newBudget.getBudget()));
//        if(newBudget.isPartitioned()){
//            String temp = "";
//            if(newBudget.isAmountBased()){
//                temp += "$";
//            }
//            temp += String.format("%.2f", newBudget.getPartitionValue());
//            if(!newBudget.isAmountBased()){
//                temp += "%";
//            }
//            temp += " of P.C.";
//            partitionDisplay.setText(temp);
//        }
//
//
//        budgetTitles.add(budgetName);
//        budgetDisplays.add(amountDisplay);
//        renameButtons.add(editButton);
//        deleteButtons.add(deleteButton);
//        budgetPlusButtons.add(depositButton);
//        budgetMinusButtons.add(withdrawButton);
//
//        editButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                //TODO Make "rename" pop-up
//                budgetName.setText("Billy");
//            }
//        });
//
//
//        deleteButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                //TODO Make "delete" pop-up
//            }
//        });
//
//
//        depositButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                deposit(view);
//            }
//        });
//
//
//        withdrawButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                //TODO Make "withdraw" pop-up
//            }
//        });
    }

    public void openDepositDialog(){
        DepositDialog dialog = new DepositDialog();
        dialog.show(getSupportFragmentManager(), "Deposit Dialog");
    }

    public void openWithdrawDialog(){
        WithdrawDialog dialog = new WithdrawDialog();
        dialog.show(getSupportFragmentManager(), "Withdraw Dialog");
    }

    public void makeBudget(View view){
        Intent makeNewBudget = new Intent(this, BudgetCreateActivity.class);
        startActivityForResult(makeNewBudget, 1);
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

    @Override
    public void applyDeposit(String amount) {
        budgets.get(placeholder).deposit(Double.parseDouble(amount));
        refresh();
    }

    @Override
    public void applyWithdraw(String amount){
        budgets.get(placeholder).withdraw(Double.parseDouble(amount));
        refresh();
    }
}
