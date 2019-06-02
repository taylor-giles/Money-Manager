package c.giles.budgetappv11.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.NumberFormat;

import c.giles.budgetappv11.BudgetManager;
import c.giles.budgetappv11.R;


public class DepositDialog extends AppCompatDialogFragment {

    private DepositDialogListener listener;
    private EditText depositEntry;
    private Button quickButton1;
    private Button quickButton2;
    private Button quickButton3;

    NumberFormat moneyFormat = NumberFormat.getNumberInstance();
    NumberFormat intFormat = NumberFormat.getNumberInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.deposit_dialog, null);

        moneyFormat.setMaximumFractionDigits(2);
        moneyFormat.setMinimumFractionDigits(2);
        intFormat.setMaximumFractionDigits(0);
        intFormat.setMinimumFractionDigits(0);

        depositEntry = (EditText)view.findViewById(R.id.depositEntryBar);
        quickButton1 = (Button)view.findViewById(R.id.quickDeposit1);
        quickButton2 = (Button)view.findViewById(R.id.quickDeposit2);
        quickButton3 = (Button)view.findViewById(R.id.quickDeposit3);

        //If the quickValues can be represented as integers, don't bother formatting them to 2 decimal places
        if(BudgetManager.getQuickDepositValue(0) != Math.rint(BudgetManager.getQuickDepositValue(0))) {
            quickButton1.setText("$" + moneyFormat.format(BudgetManager.getQuickDepositValue(0)));
        } else {
            quickButton1.setText("$" + intFormat.format(BudgetManager.getQuickDepositValue(0)));
        }
        if(BudgetManager.getQuickDepositValue(1) != Math.rint(BudgetManager.getQuickDepositValue(1))) {
            quickButton2.setText("$" + moneyFormat.format(BudgetManager.getQuickDepositValue(1)));
        } else {
            quickButton2.setText("$" + intFormat.format(BudgetManager.getQuickDepositValue(1)));
        }
        if(BudgetManager.getQuickDepositValue(2) != Math.rint(BudgetManager.getQuickDepositValue(2))) {
            quickButton3.setText("$" + moneyFormat.format(BudgetManager.getQuickDepositValue(2)));
        } else {
            quickButton3.setText("$" + intFormat.format(BudgetManager.getQuickDepositValue(2)));
        }


        builder.setView(view)
                .setTitle("")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                })
                .setPositiveButton("Deposit", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        String amount = depositEntry.getText().toString();
                        if(!amount.isEmpty())
                            listener.applyDeposit(amount);
                    }
                })
        ;

        quickButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositEntry.setText(quickButton1.getText().toString().substring(1));
            }
        });

        quickButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositEntry.setText(quickButton2.getText().toString().substring(1));
            }
        });

        quickButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositEntry.setText(quickButton3.getText().toString().substring(1));
            }
        });

        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DepositDialogListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement DepositDialogListener");
        }
    }


    public interface DepositDialogListener {
        void applyDeposit(String amount);
    }
}
