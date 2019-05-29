package c.giles.budgetappv11;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Calendar;

import c.giles.budgetappv11.views.DepositDialog;
import c.giles.budgetappv11.views.EditDialog;
import c.giles.budgetappv11.views.PaycheckDialog;
import c.giles.budgetappv11.views.WithdrawDialog;

public class MainActivity extends AppCompatActivity implements DepositDialog.DepositDialogListener, WithdrawDialog.WithdrawDialogListener, EditDialog.EditDialogListener, PaycheckDialog.PaycheckDialogListener {

    List<Budget> budgets = new ArrayList<>();
    List<LinearLayout> budgetLayouts = new ArrayList<>();
    List<HistoryItem> historyItems = new ArrayList<>();
    NumberFormat format = NumberFormat.getNumberInstance();
    int placeholder = -1;
    boolean editModeOn = false;

    final int DEFAULT_BUDGET_PLACEHOLDER = -1;

    Budget defaultBudget;
    TextView defaultBudgetView;
    TextView defaultBudgetNameView;
    Budget totalFunds;
    TextView totalFundsView;

    private LinearLayout budgetsWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        budgetsWindow = (LinearLayout)findViewById(R.id.budgets_window);
        BudgetHandler.setBudgetDisplayWindow(budgetsWindow);

        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);

        initDefaultBudgets();

        loadSharedPreferences();

        refresh();

        //If the edit mode switch in the menu is checked, put the budgets in "edit mode" (show the edit and delete buttons)
        if(editModeOn){
            for(LinearLayout layout : budgetLayouts){
                layout.findViewWithTag(3).setVisibility(View.VISIBLE);
                layout.findViewWithTag(4).setVisibility(View.VISIBLE);
                layout.findViewWithTag(6).setVisibility(View.GONE);
                layout.findViewWithTag(7).setVisibility(View.GONE);
                layout.findViewWithTag(8).setVisibility(View.VISIBLE);
                layout.findViewWithTag(9).setVisibility(View.VISIBLE);
            }
        } else {
            for(LinearLayout layout : budgetLayouts){
                layout.findViewWithTag(3).setVisibility(View.GONE);
                layout.findViewWithTag(4).setVisibility(View.GONE);
                layout.findViewWithTag(6).setVisibility(View.VISIBLE);
                layout.findViewWithTag(7).setVisibility(View.VISIBLE);
                layout.findViewWithTag(8).setVisibility(View.GONE);
                layout.findViewWithTag(9).setVisibility(View.GONE);
            }
        }

        Button paycheckButton = (Button) findViewById(R.id.paycheck_button);
        paycheckButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openPaycheckDialog();
            }
        });

        Button addButton = (Button) findViewById(R.id.addBudgetButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                makeBudget(view);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void saveSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        //Save budgets
        List<Budget> temp = new ArrayList(budgets);
        temp.add(0, defaultBudget);

        String jsonTemp = gson.toJson(temp);
        editor.putString("budgets", jsonTemp);

        //Save quick values
        List<List<Double>> quickValues = new ArrayList<>();
        quickValues.add(0, BudgetHandler.getQuickPayValues());
        quickValues.add(1, BudgetHandler.getQuickDepositValues());
        quickValues.add(2, BudgetHandler.getQuickWithdrawValues());

        String jsonValues = gson.toJson(quickValues);
        editor.putString("quick values", jsonValues);
        editor.apply();


        //Update the history shared preferences
        SharedPreferences historyData = getSharedPreferences("history data", MODE_PRIVATE);
        SharedPreferences.Editor historyEditor = historyData.edit();
        List<HistoryItem> historyTemp = new ArrayList<>(historyItems);
        String historyJson = gson.toJson(historyTemp);
        historyEditor.putString("history list", historyJson);
        historyEditor.apply();
    }

    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences historyPreferences = getSharedPreferences("history data", MODE_PRIVATE);
        Gson gson = new Gson();

        //Get budgets
        String loadedBudgets = sharedPreferences.getString("budgets", null);
        Type budgetsType = new TypeToken<ArrayList<Budget>>() {}.getType();
        ArrayList<Budget> temp = new ArrayList<>();
        temp = gson.fromJson(loadedBudgets, budgetsType);
        if(temp == null){
            budgets = new ArrayList<>();
            BudgetHandler.setDefaultBudgetName(getString(R.string.default_budget_name));
            initDefaultBudgets();
        } else {
            //Take the default budget from the first element in the temp list and then set the budgets list to the remaining budgets
            defaultBudget = temp.get(0);
            BudgetHandler.setDefaultBudgetName(defaultBudget.getBudgetName());
            budgets = temp.subList(1, temp.size());
        }

        //Get values for quick-action buttons
        String loadedQuickValues = sharedPreferences.getString("quick values", null);
        Type quickValType = new TypeToken<ArrayList<ArrayList<Double>>>() {}.getType();
        List<List<Double>> quickTemp = new ArrayList<>();
        quickTemp = gson.fromJson(loadedQuickValues, quickValType);
        if(quickTemp == null){
            List<Double> quickPayAmounts = new ArrayList<>();
            List<Double> quickDepositAmounts = new ArrayList<>();
            List<Double> quickWithdrawAmounts = new ArrayList<>();

            quickPayAmounts.add(0, Double.parseDouble(getString(R.string.quick_pay_text_1).substring(1)));
            quickPayAmounts.add(1, Double.parseDouble(getString(R.string.quick_pay_text_2).substring(1)));
            quickPayAmounts.add(2, Double.parseDouble(getString(R.string.quick_pay_text_3).substring(1)));

            quickDepositAmounts.add(0, Double.parseDouble(getString(R.string.quick_deposit_1).substring(1)));
            quickDepositAmounts.add(1, Double.parseDouble(getString(R.string.quick_deposit_2).substring(1)));
            quickDepositAmounts.add(2, Double.parseDouble(getString(R.string.quick_deposit_3).substring(1)));

            quickWithdrawAmounts.add(0, Double.parseDouble(getString(R.string.quick_withdraw_1).substring(1)));
            quickWithdrawAmounts.add(1, Double.parseDouble(getString(R.string.quick_withdraw_2).substring(1)));
            quickWithdrawAmounts.add(2, Double.parseDouble(getString(R.string.quick_withdraw_3).substring(1)));

            BudgetHandler.setQuickPayAmounts(quickPayAmounts);
            BudgetHandler.setQuickDepositAmounts(quickDepositAmounts);
            BudgetHandler.setQuickWithdrawAmounts(quickWithdrawAmounts);

        } else {
            BudgetHandler.setQuickPayAmounts(quickTemp.get(0));
            BudgetHandler.setQuickDepositAmounts(quickTemp.get(1));
            BudgetHandler.setQuickWithdrawAmounts(quickTemp.get(2));
        }


        /*//Get history data
        String loadedHistoryList = historyPreferences.getString("history list", null);
        Type historyType = new TypeToken<ArrayList<HistoryItem>>() {}.getType();
        List<HistoryItem> historyTemp = new ArrayList<>();
        historyTemp = gson.fromJson(loadedHistoryList, historyType);
        if(historyTemp == null){
            historyItems = new ArrayList<>();
        } else {
            historyItems = new ArrayList<>(historyTemp);
        }*/
    }

    private void refresh(){
        updateLists();

        //If the edit mode switch in the menu is checked, put the budgets in "edit mode" (show the edit and delete buttons)
        if(editModeOn){
            for(LinearLayout layout : budgetLayouts){
                layout.findViewWithTag(3).setVisibility(View.VISIBLE);
                layout.findViewWithTag(4).setVisibility(View.VISIBLE);
                layout.findViewWithTag(6).setVisibility(View.GONE);
                layout.findViewWithTag(7).setVisibility(View.GONE);
                layout.findViewWithTag(8).setVisibility(View.VISIBLE);
                layout.findViewWithTag(9).setVisibility(View.VISIBLE);

            }
        } else {
            for(LinearLayout layout : budgetLayouts){
                layout.findViewWithTag(3).setVisibility(View.GONE);
                layout.findViewWithTag(4).setVisibility(View.GONE);
                layout.findViewWithTag(6).setVisibility(View.VISIBLE);
                layout.findViewWithTag(7).setVisibility(View.VISIBLE);
                layout.findViewWithTag(8).setVisibility(View.GONE);
                layout.findViewWithTag(9).setVisibility(View.GONE);
            }
        }

        //Update each budgetLayout to reflect changes made to budgets
        for(int i = 0; i < budgets.size(); i++) {
            TextView nameDisplay = budgetLayouts.get(i).findViewWithTag(0);
            TextView amountDisplay = budgetLayouts.get(i).findViewWithTag(1);
            TextView partitionDisplay = budgetLayouts.get(i).findViewWithTag(2);
            nameDisplay.setText(budgets.get(i).getBudgetName());
            amountDisplay.setText("$" + String.format("%.02f", budgets.get(i).getBudget()));
            if (budgets.get(i).isPartitioned()) {
                String temp = "";
                if (budgets.get(i).isAmountBased()) {
                    temp += "$";
                }
                temp += String.format("%.02f", budgets.get(i).getPartitionValue());
                if (!budgets.get(i).isAmountBased()) {
                    temp += "%";
                }
                if(!editModeOn) {
                    temp += " of paycheck";
                }
                partitionDisplay.setText(temp);
            }
        }

        defaultBudgetNameView.setText(BudgetHandler.getDefaultBudgetName());
        defaultBudgetView.setText("$" + format.format(defaultBudget.getBudget()));
        totalFundsView.setText("$" + format.format(totalFunds.getBudget()));

        BudgetHandler.setModified(true);
    }
    public void updateLists(){
        //Update the budgets list and the saved list if needed (if the bridge class indicates that a change has been made)
        if(BudgetHandler.isModified()){
            budgets = new ArrayList<>(BudgetHandler.getBudgetList());
            defaultBudget.setName(BudgetHandler.getDefaultBudgetName());
            saveSharedPreferences();
            BudgetHandler.setModified(false);
        } else {
            BudgetHandler.setBudgetList(budgets);
        }

        //Erase all the budget layouts and rebuild them
        budgetLayouts = new ArrayList<>();
        budgetsWindow.removeAllViews();

        for(int i = 0; i < budgets.size(); i++){
            addBudget(budgets.get(i), i);
        }

        //Calc. total budget
        double total = defaultBudget.getBudget();
        for(Budget budget : budgets){
            total += budget.getBudget();
        }
        totalFunds.setBudget(total);
    }

    public void addBudget(Budget newBudget, int index) {
        final int i = index;

        LinearLayout budgetLayout = new LinearLayout(this);
        budgetLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        budgetLayout.setOrientation(LinearLayout.HORIZONTAL);
//        if(index % 2 != 0){
//            budgetLayout.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
//        }

        final TextView nameView = new TextView(this);
        nameView.setTag(0);

        final TextView moneyView = new TextView(this);
        moneyView.setTag(1);

        final TextView partitionView = new TextView(this);
        partitionView.setTag(2);

        ImageButton editButton = new ImageButton(this);
        editButton.setTag(3);

        ImageButton deleteButton = new ImageButton(this);
        deleteButton.setTag(4);

        View colorView = new View(this); //TODO: Add color options to create & edit menus
        colorView.setTag(5);

        Button depositButton = new Button(this);
        depositButton.setTag(6);
        Button withdrawButton = new Button(this);
        withdrawButton.setTag(7);

        Button upButton = new Button(this);
        upButton.setTag(8);
        Button downButton = new Button(this);
        downButton.setTag(9);

        //The name view and the innerInner layout go inside this layout
        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        innerLayout.setOrientation(LinearLayout.VERTICAL);

        //The money and partition views go inside this layout
        LinearLayout innerInnerLayout = new LinearLayout(this);
        innerInnerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        innerInnerLayout.setOrientation(LinearLayout.HORIZONTAL);

        //The deposit and withdraw buttons go inside this layout
        LinearLayout moneyButtonLayout = new LinearLayout(this);
        moneyButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
        moneyButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //The up and down buttons go inside this layout
        LinearLayout reorderButtonLayout = new LinearLayout(this);
        reorderButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
        reorderButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //The moneyButtonLayout, reorderButtonLayout, and some cushions go inside this layout
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.VERTICAL);


        editButton.setLayoutParams(new ViewGroup.LayoutParams(75, ViewGroup.LayoutParams.WRAP_CONTENT));
        editButton.setImageResource(android.R.drawable.ic_menu_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudgetHandler.setBudgetList(budgets);
                placeholder = i;
                BudgetHandler.setPlaceholder(placeholder);
                openEditDialog();
            }
        });

        deleteButton.setLayoutParams(new ViewGroup.LayoutParams(75, ViewGroup.LayoutParams.WRAP_CONTENT));
        deleteButton.setImageResource(android.R.drawable.ic_menu_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                BudgetHandler.removeBudget(i);
                                BudgetHandler.setModified(true);
                                refresh();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Do nothing
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you want to delete " + budgets.get(i).getBudgetName() + "?")
                        .setPositiveButton("Delete", dialogClickListener)
                        .setNegativeButton("Cancel", dialogClickListener)
                        .show()
                ;
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

        //The up and down buttons reorder the budget within the list when edit mode is enabled
        upButton.setLayoutParams(new ViewGroup.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT));
        upButton.setText("↑");
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Budget currentBudget = budgets.get(i);
                if (!(i - 1 < 0)) {
                    BudgetHandler.removeBudget(i);
                    BudgetHandler.addBudget(i - 1, currentBudget);
                    BudgetHandler.setModified(true);
                    refresh();
                }
            }
        });

        downButton.setLayoutParams(new ViewGroup.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT));
        downButton.setText("↓");
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Budget currentBudget = budgets.get(i);
                if (!(budgets.size() <= i + 1)) {
                    BudgetHandler.removeBudget(i);
                    BudgetHandler.addBudget(i + 1, currentBudget);
                    refresh();
                }
            }
        });

        nameView.setText(newBudget.getBudgetName());
        nameView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        nameView.setTextColor(getResources().getColor(android.R.color.black));
        nameView.setTextSize(18f);
        nameView.setTypeface(null, Typeface.BOLD);

        moneyView.setText("$" + format.format(newBudget.getBudget()));
        moneyView.setTextColor(getResources().getColor(android.R.color.black));
        moneyView.setTextSize(18f);

        if (newBudget.isPartitioned()) {
            String temp = "";
            if (newBudget.isAmountBased()) {
                temp += "$";
            }
            temp += format.format(newBudget.getPartitionValue());
            if (!newBudget.isAmountBased()) {
                temp += "%";
            }
            if (!editModeOn) {
                temp += " of paycheck";
            }
            partitionView.setText(temp);
        }

        //The color view displays the chosen color for this budget as a strip on the left side of the budget layout
        colorView.setLayoutParams(new LinearLayout.LayoutParams(10, LinearLayout.LayoutParams.MATCH_PARENT));

        //This cushion will go after the colorView
        Space cushion = new Space(this);
        cushion.setLayoutParams(new LinearLayout.LayoutParams(10, LinearLayout.LayoutParams.MATCH_PARENT));

        //These cushions will go above and below the budget layout, to put space between this layout and the ones around it
        Space topCushion = new Space(this);
        topCushion.setLayoutParams(new LinearLayout.LayoutParams(0, 5)); //This one goes above the BUTTONS ONLY
        Space bottomCushion = new Space(this);
        bottomCushion.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10));


        //Right-align the buttonLayout within the budgetLayout
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = RelativeLayout.ALIGN_PARENT_RIGHT;
        buttonLayout.setLayoutParams(layoutParams);

        TextView fillerText = new TextView(this);
        fillerText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        fillerText.setText("   |   ");

        Space space = new Space(this);
        space.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1));

        //Add all the views to their appropriate layouts
        innerInnerLayout.addView(moneyView);
        if (newBudget.isPartitioned()) {
            innerInnerLayout.addView(fillerText);
            innerInnerLayout.addView(partitionView);
        }
        innerLayout.addView(nameView);
        innerLayout.addView(innerInnerLayout);

        moneyButtonLayout.addView(depositButton);
        moneyButtonLayout.addView(withdrawButton);

        reorderButtonLayout.addView(upButton);
        reorderButtonLayout.addView(downButton);

        buttonLayout.addView(topCushion);
        buttonLayout.addView(moneyButtonLayout);
        buttonLayout.addView(reorderButtonLayout);

        //budgetLayout.addView(topCushion);
        if (!editModeOn) {
            budgetLayout.addView(colorView);
            budgetLayout.addView(cushion);
        }
        budgetLayout.addView(editButton);
        budgetLayout.addView(deleteButton);
        budgetLayout.addView(innerLayout);
        budgetLayout.addView(space);
        budgetLayout.addView(buttonLayout);

        budgetLayouts.add(i, budgetLayout);
        budgetsWindow.addView(budgetLayout);
        budgetsWindow.addView(bottomCushion);

        //If this budget isn't the last one, put a divider line after it
        if (i != budgets.size() - 1) {
            View divider = new View(this);
            divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
            divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            budgetsWindow.addView(divider);
        }

        //Hide the edit/delete buttons and the reorder buttons, as they will be shown only if the Edit Mode menu option is checked.
        editButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        upButton.setVisibility(View.GONE);
        downButton.setVisibility(View.GONE);
    }


    private void initDefaultBudgets(){
        defaultBudget = new Budget(BudgetHandler.getDefaultBudgetName(), 0, false, false, 0);
        defaultBudgetView = (TextView) findViewById(R.id.default_budget_money);
        defaultBudgetNameView = (TextView) findViewById(R.id.default_budget_name_view);
        defaultBudgetNameView.setText(BudgetHandler.getDefaultBudgetName());

        totalFunds = new Budget(getString(R.string.total_funds), 0, false, false, 0);
        totalFundsView = (TextView) findViewById(R.id.total_funds_money);

        //Initialize behavior for default budget
        Button defaultDepositButton = (Button)findViewById(R.id.default_deposit_button);
        //defaultDepositButton.setLayoutParams(new ViewGroup.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT));
        defaultDepositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeholder = DEFAULT_BUDGET_PLACEHOLDER;
                openDepositDialog();
            }
        });
        Button defaultWithdrawButton = (Button)findViewById(R.id.default_withdraw_button);
        //defaultWithdrawButton.setLayoutParams(new ViewGroup.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT));
        defaultWithdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeholder = DEFAULT_BUDGET_PLACEHOLDER;
                openWithdrawDialog();
            }
        });
    }


    public void openDepositDialog(){
        DepositDialog dialog = new DepositDialog();
        dialog.show(getSupportFragmentManager(), "Deposit Dialog");
    }

    public void openWithdrawDialog(){
        WithdrawDialog dialog = new WithdrawDialog();
        dialog.show(getSupportFragmentManager(), "Withdraw Dialog");
    }

    public void openEditDialog(){
        EditDialog dialog = new EditDialog();
        dialog.show(getSupportFragmentManager(), "Edit Dialog");
    }

    public void openPaycheckDialog(){
        PaycheckDialog dialog = new PaycheckDialog();
        dialog.show(getSupportFragmentManager(), "Paycheck Dialog");
    }


    public void makeBudget(View view){
        Intent makeNewBudget = new Intent(this, BudgetCreateActivity.class);
        startActivityForResult(makeNewBudget, 1);
    }

    public void openSettings(View view){
        Intent openSettings = new Intent(this, SettingsActivity.class);
        startActivityForResult(openSettings, 2);
    }

    public void openHistory(View view){
        Intent openHistory = new Intent(this, HistoryActivity.class);
        startActivityForResult(openHistory, 3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(editModeOn){
            menu.findItem(R.id.action_edit).setChecked(true);
        } else {
            menu.findItem(R.id.action_edit).setChecked(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_settings:
                openSettings(item.getActionView());
                refresh();
                return true;

            case R.id.action_edit:
                if(item.isCheckable()){
                    item.setChecked(!item.isChecked());
                }

                if(item.isCheckable() && item.isChecked()){
                    editModeOn = true;
                    for(LinearLayout layout : budgetLayouts){
                        layout.findViewWithTag(3).setVisibility(View.VISIBLE);
                        layout.findViewWithTag(4).setVisibility(View.VISIBLE);
                        layout.findViewWithTag(6).setVisibility(View.GONE);
                        layout.findViewWithTag(7).setVisibility(View.GONE);
                        layout.findViewWithTag(8).setVisibility(View.VISIBLE);
                        layout.findViewWithTag(9).setVisibility(View.VISIBLE);
                    }
                } else {
                    editModeOn = false;
                    for(LinearLayout layout : budgetLayouts){
                        layout.findViewWithTag(3).setVisibility(View.GONE);
                        layout.findViewWithTag(4).setVisibility(View.GONE);
                        layout.findViewWithTag(6).setVisibility(View.VISIBLE);
                        layout.findViewWithTag(7).setVisibility(View.VISIBLE);
                        layout.findViewWithTag(8).setVisibility(View.GONE);
                        layout.findViewWithTag(9).setVisibility(View.GONE);
                    }
                }
                refresh();
                return true;

            case R.id.action_history:
                openHistory(item.getActionView());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void applyDeposit(String amount) {
        if(placeholder == DEFAULT_BUDGET_PLACEHOLDER){
            defaultBudget.deposit(Double.parseDouble(amount));
            addHistoryItem(defaultBudget, Double.parseDouble(amount));
        } else {
            budgets.get(placeholder).deposit(Double.parseDouble(amount));
            addHistoryItem(budgets.get(placeholder), Double.parseDouble(amount));
        }
        BudgetHandler.setModified(true);
        refresh();
    }

    @Override
    public void applyWithdraw(String amount){
        if(placeholder == DEFAULT_BUDGET_PLACEHOLDER){
            defaultBudget.withdraw(Double.parseDouble(amount));
            addHistoryItem(defaultBudget, Double.parseDouble(amount) * -1);
        } else {
            budgets.get(placeholder).withdraw(Double.parseDouble(amount));
            addHistoryItem(budgets.get(placeholder), Double.parseDouble(amount) * -1);
        }
        BudgetHandler.setModified(true);
        refresh();
    }

    @Override
    public void applyEdits(String newName, String partitionValue, boolean isPartitioned, boolean isAmountBased){
        budgets.get(placeholder).setName(newName);
        budgets.get(placeholder).setPartitioned(isPartitioned);
        if(isPartitioned) {
            budgets.get(placeholder).setPartitionValue(Double.parseDouble(partitionValue));
            budgets.get(placeholder).setAmountBased(isAmountBased);
        }
        BudgetHandler.setModified(true);
        refresh();
    }

    @Override
    public void logPaycheck(String amount){
        Double remainingAmount = Double.parseDouble(amount);
        for(Budget budget : budgets){
            if(budget.isPartitioned()){
                if(!budget.isAmountBased()){
                    budget.deposit((budget.getPartitionValue() / 100) * Double.parseDouble(amount));
                    addHistoryItem(budget, Double.parseDouble(amount));
                    remainingAmount = remainingAmount - (budget.getPartitionValue() / 100) * Double.parseDouble(amount);
                } else {
                    budget.deposit(budget.getPartitionValue());
                    addHistoryItem(budget, Double.parseDouble(amount));
                    //TODO MAKE SURE THE AMOUNT-BASED PARTITIONS DON'T EXCEED PAYCHECK AMOUNT
                    remainingAmount = remainingAmount - budget.getPartitionValue();
                }
            }
        }
        defaultBudget.deposit(remainingAmount);
        addHistoryItem(defaultBudget, remainingAmount);
        refresh();
    }

    public void addHistoryItem(Budget budget, Double amount){
        historyItems.add(new HistoryItem(this, budget, amount, (GregorianCalendar) Calendar.getInstance()));
    }
}
