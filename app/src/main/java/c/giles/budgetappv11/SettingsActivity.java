package c.giles.budgetappv11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class SettingsActivity extends AppCompatActivity {

    NumberFormat format = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText defaultBudgetNameBox;
        final EditText quickPayBox1;
        final EditText quickPayBox2;
        final EditText quickPayBox3;
        final EditText quickDepositBox1;
        final EditText quickDepositBox2;
        final EditText quickDepositBox3;
        final EditText quickWithdrawBox1;
        final EditText quickWithdrawBox2;
        final EditText quickWithdrawBox3;
        final Button cancelButton;
        final Button saveButton;

        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);

        defaultBudgetNameBox = (EditText)findViewById(R.id.default_budget_name_box);
        quickPayBox1 = (EditText)findViewById(R.id.quick_pay_box_1);
        quickPayBox2 = (EditText)findViewById(R.id.quick_pay_box_2);
        quickPayBox3 = (EditText)findViewById(R.id.quick_pay_box_3);
        quickDepositBox1 = (EditText)findViewById(R.id.quick_deposit_box_1);
        quickDepositBox2 = (EditText)findViewById(R.id.quick_deposit_box_2);
        quickDepositBox3 = (EditText)findViewById(R.id.quick_deposit_box_3);
        quickWithdrawBox1 = (EditText)findViewById(R.id.quick_withdraw_box_1);
        quickWithdrawBox2 = (EditText)findViewById(R.id.quick_withdraw_box_2);
        quickWithdrawBox3 = (EditText)findViewById(R.id.quick_withdraw_box_3);
        cancelButton = (Button)findViewById(R.id.settings_cancel_button);
        saveButton = (Button)findViewById(R.id.settings_save_button);


        defaultBudgetNameBox.setText(BudgetHandler.getDefaultBudgetName());

        quickPayBox1.setText(format.format(BudgetHandler.getQuickPayValue(0)));
        quickPayBox2.setText(format.format(BudgetHandler.getQuickPayValue(1)));
        quickPayBox3.setText(format.format(BudgetHandler.getQuickPayValue(2)));

        quickDepositBox1.setText(format.format(BudgetHandler.getQuickDepositValue(0)));
        quickDepositBox2.setText(format.format(BudgetHandler.getQuickDepositValue(1)));
        quickDepositBox3.setText(format.format(BudgetHandler.getQuickDepositValue(2)));

        quickWithdrawBox1.setText(format.format(BudgetHandler.getQuickWithdrawValue(0)));
        quickWithdrawBox2.setText(format.format(BudgetHandler.getQuickWithdrawValue(1)));
        quickWithdrawBox3.setText(format.format(BudgetHandler.getQuickWithdrawValue(2)));


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity(v);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudgetHandler.setDefaultBudgetName(defaultBudgetNameBox.getText().toString());
                BudgetHandler.setQuickPayAmounts(new Double[]{
                        Double.parseDouble(quickPayBox1.getText().toString()),
                        Double.parseDouble(quickPayBox2.getText().toString()),
                        Double.parseDouble(quickPayBox3.getText().toString()),
                });
                BudgetHandler.setQuickDepositAmounts(new Double[]{
                        Double.parseDouble(quickDepositBox1.getText().toString()),
                        Double.parseDouble(quickDepositBox2.getText().toString()),
                        Double.parseDouble(quickDepositBox3.getText().toString()),
                });
                BudgetHandler.setQuickWithdrawAmounts(new Double[]{
                        Double.parseDouble(quickWithdrawBox1.getText().toString()),
                        Double.parseDouble(quickWithdrawBox2.getText().toString()),
                        Double.parseDouble(quickWithdrawBox3.getText().toString()),
                });

                BudgetHandler.setModified(true);

                startMainActivity(v);
            }
        });

    }

    public void startMainActivity(View view){
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }
}
