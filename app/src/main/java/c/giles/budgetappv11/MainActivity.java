package c.giles.budgetappv11;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import c.giles.budgetappv11.views.DepositDialog;
import c.giles.budgetappv11.views.EditDialog;
import c.giles.budgetappv11.views.WithdrawDialog;

public class MainActivity extends AppCompatActivity implements DepositDialog.DepositDialogListener, WithdrawDialog.WithdrawDialogListener, EditDialog.EditDialogListener {

    List<Budget> budgets = new ArrayList<>();
    List<LinearLayout> budgetLayouts = new ArrayList<>();
    NumberFormat format = NumberFormat.getNumberInstance();
    int placeholder = -1;

    private LinearLayout verticalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadSharedPreferences();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        verticalLayout = (LinearLayout)findViewById(R.id.verticalLayout);
        BudgetHandler.setBudgetDisplayWindow(verticalLayout);

        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);

        updateLists();

        Button addButton = (Button) findViewById(R.id.addBudgetButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                makeBudget(view);
            }
        });
    }

    private void saveSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);;
        SharedPreferences.Editor editor= sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(budgets);
        editor.putString("budgets", json);
        editor.apply();
    }

    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String loadedJson = sharedPreferences.getString("budgets", null);
        Type type = new TypeToken<ArrayList<Budget>>() {}.getType();
        budgets = gson.fromJson(loadedJson, type);

        if(budgets == null){
            budgets = new ArrayList<>();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                //addBudget(BudgetHandler.getNewBudget());
            }
        }
    }

    private void refresh(){
        updateLists();
        //Updates each budgetLayout to reflect changes made to budgets
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
                temp += " of P.C.";
                partitionDisplay.setText(temp);
            }
        }
        BudgetHandler.setModified(true);
            //budgets.get(i).refresh();
    }

    public void addBudget(Budget newBudget, int index){
        final int i = index;

        LinearLayout budgetLayout = new LinearLayout(this);
        budgetLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        budgetLayout.setOrientation(LinearLayout.HORIZONTAL);
        if(index % 2 != 0){
            budgetLayout.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }

        ImageButton editButton = new ImageButton(this);
        ImageButton deleteButton = new ImageButton(this);
        Button depositButton = new Button(this);
        Button withdrawButton = new Button(this);
        final TextView nameView = new TextView(this);
            nameView.setTag(0);
        final TextView moneyView = new TextView(this);
            moneyView.setTag(1);
        final TextView partitionView = new TextView(this);
            partitionView.setTag(2);

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
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                BudgetHandler.removeBudget(i);
                                BudgetHandler.setModified(true);
                                updateLists();
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

        nameView.setText(newBudget.getBudgetName());
        nameView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        nameView.setTextColor(getResources().getColor(android.R.color.black));
        nameView.setTextSize(16f);

        moneyView.setText("$" + format.format(newBudget.getBudget()));
        moneyView.setTextColor(getResources().getColor(android.R.color.black));
        moneyView.setTextSize(16f);

        if(newBudget.isPartitioned()){
            String temp = "";
            if(newBudget.isAmountBased()){
                temp += "$";
            }
            temp += format.format(newBudget.getPartitionValue());
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

        budgetLayouts.add(i,budgetLayout);
        verticalLayout.addView(budgetLayout);
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


    public void makeBudget(View view){
        Intent makeNewBudget = new Intent(this, BudgetCreateActivity.class);
        startActivityForResult(makeNewBudget, 1);
    }


    public void updateLists(){
        //Update the budgets list and the saved list if needed (if the bridge class indicates that a change has been made)
        if(BudgetHandler.isModified()){
            budgets = new ArrayList<>(BudgetHandler.getBudgetList());
            saveSharedPreferences();
            BudgetHandler.setModified(false);
        } else {
            BudgetHandler.setBudgetList(budgets);
        }

        budgetLayouts = new ArrayList<>();
        verticalLayout.removeAllViews();
        for(int i = 0; i < budgets.size(); i++){
            addBudget(budgets.get(i), i);
        }
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
        BudgetHandler.setModified(true);
        refresh();
    }

    @Override
    public void applyWithdraw(String amount){
        budgets.get(placeholder).withdraw(Double.parseDouble(amount));
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


}
