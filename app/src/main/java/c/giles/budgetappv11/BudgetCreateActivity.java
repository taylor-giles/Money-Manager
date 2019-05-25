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

import java.util.List;

public class BudgetCreateActivity extends AppCompatActivity {

    String budgetName = "New Budget";
    double initalBudget = 0.0;
    boolean partition = false;
    boolean amountBased = true;
    double partitionValue = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_create);

        final TextView nameView = (TextView) findViewById(R.id.budgetName);
        final TextView initialBudgetView = (TextView) findViewById(R.id.startingBudget);
        final Switch partitionSwitch = (Switch) findViewById(R.id.partitionSwitch);
        final TextView partitionValueView = (TextView) findViewById(R.id.partitionValue);
        final ToggleButton partitionTypeButton = (ToggleButton)findViewById(R.id.baseToggleButton);

        final TextView dollarSignSmall = (TextView)findViewById(R.id.dollarSignSmall);
        final TextView percentSignSmall = (TextView)findViewById(R.id.percentSignSmall);

        Button createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                budgetName = nameView.getText().toString();
                initalBudget = Double.parseDouble(initialBudgetView.getText().toString());
                partition = partitionSwitch.isChecked();
                partitionValue = Double.parseDouble(partitionValueView.getText().toString());
                amountBased = partitionTypeButton.isChecked();

                if(partition && !amountBased && BudgetHandler.getTotalPercentPartitioned() + partitionValue > 100){
                    Toast.makeText(getBaseContext(), "Please keep total partition percentage below 100%", Toast.LENGTH_LONG).show();
                } else {
                    Budget newBudget = new Budget(budgetName, initalBudget, partition, amountBased, partitionValue);
                    BudgetHandler.addBudget(newBudget);
                    BudgetHandler.setModified(true);
                    setResult(RESULT_OK);
                    startMainActivity(view);
                }
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startMainActivity(view);
            }
        });


        partitionTypeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(partitionTypeButton.isChecked()){ //If it's activated, that means it's in amount-based mode
                    dollarSignSmall.setText("$");
                    percentSignSmall.setText(" ");
                } else {
                    dollarSignSmall.setText(" ");
                    percentSignSmall.setText("%");
                }
            }
        });



    }

    public void startMainActivity(View view){
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

}
