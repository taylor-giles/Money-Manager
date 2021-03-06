package c.giles.budgetappv11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

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


        defaultBudgetNameBox.setText(BudgetManager.getDefaultBudgetName());

        quickPayBox1.setText(format.format(BudgetManager.getQuickPayValue(0)));
        quickPayBox2.setText(format.format(BudgetManager.getQuickPayValue(1)));
        quickPayBox3.setText(format.format(BudgetManager.getQuickPayValue(2)));

        quickDepositBox1.setText(format.format(BudgetManager.getQuickDepositValue(0)));
        quickDepositBox2.setText(format.format(BudgetManager.getQuickDepositValue(1)));
        quickDepositBox3.setText(format.format(BudgetManager.getQuickDepositValue(2)));

        quickWithdrawBox1.setText(format.format(BudgetManager.getQuickWithdrawValue(0)));
        quickWithdrawBox2.setText(format.format(BudgetManager.getQuickWithdrawValue(1)));
        quickWithdrawBox3.setText(format.format(BudgetManager.getQuickWithdrawValue(2)));


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity(v);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Double> quickPayAmounts = new ArrayList<>();
                List<Double> quickDepositAmounts = new ArrayList<>();
                List<Double> quickWithdrawAmounts = new ArrayList<>();

                quickPayAmounts.add(0, Double.parseDouble(quickPayBox1.getText().toString()));
                quickPayAmounts.add(1, Double.parseDouble(quickPayBox2.getText().toString()));
                quickPayAmounts.add(2, Double.parseDouble(quickPayBox3.getText().toString()));

                quickDepositAmounts.add(0,  Double.parseDouble(quickDepositBox1.getText().toString()));
                quickDepositAmounts.add(1,  Double.parseDouble(quickDepositBox2.getText().toString()));
                quickDepositAmounts.add(2,  Double.parseDouble(quickDepositBox3.getText().toString()));

                quickWithdrawAmounts.add(0,  Double.parseDouble(quickWithdrawBox1.getText().toString()));
                quickWithdrawAmounts.add(1,  Double.parseDouble(quickWithdrawBox2.getText().toString()));
                quickWithdrawAmounts.add(2,  Double.parseDouble(quickWithdrawBox3.getText().toString()));


                BudgetManager.setDefaultBudgetName(defaultBudgetNameBox.getText().toString());
                BudgetManager.setQuickPayAmounts(quickPayAmounts);
                BudgetManager.setQuickDepositAmounts(quickDepositAmounts);
                BudgetManager.setQuickWithdrawAmounts(quickWithdrawAmounts);


                BudgetManager.setModified(true);
                finish();
            }
        });

    }

    public void startMainActivity(View view){
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    public interface SettingsListener {
        void applySettings();
    }
}
