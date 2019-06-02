package c.giles.budgetappv11.views;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import c.giles.budgetappv11.Budget;
import c.giles.budgetappv11.R;

public class BudgetLayout extends LinearLayout {

    TextView budgetName = (TextView)findViewById(R.id.nameDisplay);
    TextView currentBudget = (TextView)findViewById(R.id.moneyDisplay);
    TextView partitionDisplay = (TextView)findViewById(R.id.partitionDisplay);
    ImageButton renameButton = (ImageButton)findViewById(R.id.renameButton);
    ImageButton deleteButton = (ImageButton)findViewById(R.id.deleteButton);
    Button depositButton = (Button)findViewById(R.id.addButton);
    Button withdrawButton = (Button)findViewById(R.id.subtractButton);

    Budget budget;

    public BudgetLayout(Context context) {
        super(context);
    }

    public BudgetLayout(Context context, Budget budget){
        super(context);
        this.budget = budget;

        budgetName.setText(budget.getBudgetName());
        currentBudget.setText(budget.getAmount().toString());
        partitionDisplay.setText("");

        renameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //TODO open rename window
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //TODO open delete window
            }
        });

        depositButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //TODO open deposit window
            }
        });

        withdrawButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //TODO open withdraw window
            }
        });

    }
}
