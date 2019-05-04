package c.giles.budgetappv11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DepositActivity extends AppCompatActivity {

    double deposit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        final TextView depositAmount = (TextView) findViewById(R.id.depositAmountBar);
        Button quickDepositButton1 = (Button) findViewById(R.id.quickDepositButton1);
        Button quickDepositButton2 = (Button) findViewById(R.id.quickDepositButton2);
        Button quickDepositButton3 = (Button) findViewById(R.id.quickDepositButton3);
        Button depositCancelButton = (Button) findViewById(R.id.depositCancelButton);
        Button depositButton = (Button) findViewById(R.id.depositButton);

        quickDepositButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                depositAmount.setText(R.string.quick_deposit_1);
            }
        });
        quickDepositButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                depositAmount.setText(R.string.quick_deposit_2);
            }
        });
        quickDepositButton3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                depositAmount.setText(R.string.quick_deposit_3);
            }
        });

        depositCancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startMainActivity();
            }
        });

        depositButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(depositAmount.getText().toString().charAt(0) == '$') {
                    deposit = Double.parseDouble(depositAmount.getText().toString().substring(1)); //Gets the actual number included in the string (removes the $)
                } else {
                    deposit = Double.parseDouble(depositAmount.getText().toString());
                }

            }
        });

    }

    public double getDeposit(){
        return deposit;
    }

    private void startMainActivity(){
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

}
