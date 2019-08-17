package c.giles.budgetappv11;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Space;

import java.util.ArrayList;
import java.util.List;

import c.giles.budgetappv11.views.ColorDialog;
import c.giles.budgetappv11.views.DepositDialog;

public class BudgetCreateActivity extends AppCompatActivity implements ColorDialog.ColorDialogListener {

    String budgetName = "New Budget";
    double initialBudget = 0.0;
    boolean partition = false;
    boolean amountBased = true;
    double partitionValue = 0.0;
    String colorName = "";
    TextView nameView;
    TextView initialBudgetView;
    Switch partitionSwitch;
    TextView partitionValueView;
    ToggleButton partitionTypeButton;
    TextView dollarSignSmall;
    TextView percentSignSmall;
    LinearLayout colorLayout;
    Button colorButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_create);


        nameView = (TextView) findViewById(R.id.budget_name_entry);
        initialBudgetView = (TextView) findViewById(R.id.initial_budget_entry);
        partitionSwitch = (Switch) findViewById(R.id.partition_switch);
        partitionValueView = (TextView) findViewById(R.id.partition_value_entry);
        partitionTypeButton = (ToggleButton)findViewById(R.id.partition_basis_toggle_button);
        dollarSignSmall = (TextView)findViewById(R.id.dollar_sign_small);
        percentSignSmall = (TextView)findViewById(R.id.percent_sign_small);
        colorButton = (Button)findViewById(R.id.color_button);

        partitionValueView.setEnabled(partitionSwitch.isChecked());

    }

    public void startMainActivity(View view){
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    public void toggleBasis(View view){
        if(partitionTypeButton.isChecked()){ //If it's checked, that means it's in amount-based mode
            dollarSignSmall.setVisibility(View.VISIBLE);
            percentSignSmall.setVisibility(View.INVISIBLE);
        } else {
            dollarSignSmall.setVisibility(View.INVISIBLE);
            percentSignSmall.setVisibility(View.VISIBLE);
        }
    }

    public void openColorDialog(View view){
        ColorDialog dialog = new ColorDialog();
        dialog.show(getSupportFragmentManager(), "Color Dialog");
    }

    public void flipSwitch(View view){
        partitionValueView.setEnabled(partitionSwitch.isChecked());
    }

    public void create(View view) {
        budgetName = nameView.getText().toString();
        initialBudget = Double.parseDouble(initialBudgetView.getText().toString());
        partition = partitionSwitch.isChecked();
        partitionValue = Double.parseDouble(partitionValueView.getText().toString());
        amountBased = partitionTypeButton.isChecked();

        if(partition && !amountBased && BudgetManager.getTotalPercentPartitioned() + partitionValue > 100){
            Toast.makeText(getBaseContext(), "Please keep total partition percentage below 100%", Toast.LENGTH_LONG).show();
        } else {
            Budget newBudget = new Budget(budgetName, initialBudget, partition, amountBased, partitionValue);
            BudgetManager.addBudget(newBudget);
            BudgetManager.setModified(true);
            setResult(RESULT_OK);
            startMainActivity(view);
        }
    }

    public void cancel(View view) {
        startMainActivity(view);
    }

    @Override
    public void applyColor(String color) {

    }
}
