package c.giles.budgetappv11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class BudgetCreateActivity extends AppCompatActivity {

    String budgetName = "New Budget";
    double initialBudget = 0.0;
    boolean partition = false;
    boolean amountBased = true;
    double partitionValue = 0.0;
    TextView nameView;
    TextView initialBudgetView;
    Switch partitionSwitch;
    TextView partitionValueView;
    ToggleButton partitionTypeButton;
    TextView dollarSignSmall;
    TextView percentSignSmall;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_create);


        nameView = (TextView) findViewById(R.id.budgetName);
        initialBudgetView = (TextView) findViewById(R.id.startingBudget);
        partitionSwitch = (Switch) findViewById(R.id.partitionSwitch);
        partitionValueView = (TextView) findViewById(R.id.partitionValue);
        partitionTypeButton = (ToggleButton)findViewById(R.id.baseToggleButton);
        dollarSignSmall = (TextView)findViewById(R.id.dollarSignSmall);
        percentSignSmall = (TextView)findViewById(R.id.percentSignSmall);

        partitionValueView.setEnabled(partitionSwitch.isChecked());
        partitionSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partitionValueView.setEnabled(partitionSwitch.isChecked());
            }
        });

        partitionTypeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(partitionTypeButton.isChecked()){ //If it's checked, that means it's in amount-based mode
                    dollarSignSmall.setVisibility(View.VISIBLE);
                    percentSignSmall.setVisibility(View.INVISIBLE);
                } else {
                    dollarSignSmall.setVisibility(View.INVISIBLE);
                    percentSignSmall.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void startMainActivity(View view){
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    public void create(View view) {
        budgetName = nameView.getText().toString();
        initialBudget = Double.parseDouble(initialBudgetView.getText().toString());
        partition = partitionSwitch.isChecked();
        partitionValue = Double.parseDouble(partitionValueView.getText().toString());
        amountBased = partitionTypeButton.isChecked();

        if(partition && !amountBased && BudgetHandler.getTotalPercentPartitioned() + partitionValue > 100){
            Toast.makeText(getBaseContext(), "Please keep total partition percentage below 100%", Toast.LENGTH_LONG).show();
        } else {
            Budget newBudget = new Budget(budgetName, initialBudget, partition, amountBased, partitionValue);
            BudgetHandler.addBudget(newBudget);
            BudgetHandler.setModified(true);
            setResult(RESULT_OK);
            startMainActivity(view);
        }
    }

    public void cancel(View view) {
        startMainActivity(view);
    }
}
