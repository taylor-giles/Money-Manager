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

import c.giles.budgetappv11.BudgetHandler;
import c.giles.budgetappv11.R;


public class PaycheckDialog extends AppCompatDialogFragment {

    private PaycheckDialogListener listener;
    private EditText paycheckEntry;
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

        View view = inflater.inflate(R.layout.paycheck_dialog, null);

        moneyFormat.setMaximumFractionDigits(2);
        moneyFormat.setMinimumFractionDigits(2);
        intFormat.setMaximumFractionDigits(0);
        intFormat.setMinimumFractionDigits(0);

        paycheckEntry = (EditText)view.findViewById(R.id.paycheck_entry_bar);
        quickButton1 = (Button)view.findViewById(R.id.quick_pay_1);
        quickButton2 = (Button)view.findViewById(R.id.quick_pay_2);
        quickButton3 = (Button)view.findViewById(R.id.quick_pay_3);
        builder.setView(view)
                .setTitle("Log Paycheck")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                })
                .setPositiveButton("Log", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        String amount = paycheckEntry.getText().toString();
                        if(!amount.isEmpty())
                            listener.logPaycheck(amount);
                    }
                })
        ;

        //If the quickValues can be represented as integers, don't bother formatting them to 2 decimal places
        if(BudgetHandler.getQuickPayValue(0) != Math.rint(BudgetHandler.getQuickPayValue(0))) {
            quickButton1.setText("$" + moneyFormat.format(BudgetHandler.getQuickPayValue(0)));
        } else {
            quickButton1.setText("$" + intFormat.format(BudgetHandler.getQuickPayValue(0)));
        }
        if(BudgetHandler.getQuickPayValue(1) != Math.rint(BudgetHandler.getQuickPayValue(1))) {
            quickButton2.setText("$" + moneyFormat.format(BudgetHandler.getQuickPayValue(1)));
        } else {
            quickButton2.setText("$" + intFormat.format(BudgetHandler.getQuickPayValue(1)));
        }
        if(BudgetHandler.getQuickPayValue(2) != Math.rint(BudgetHandler.getQuickPayValue(2))) {
            quickButton3.setText("$" + moneyFormat.format(BudgetHandler.getQuickPayValue(2)));
        } else {
            quickButton3.setText("$" + intFormat.format(BudgetHandler.getQuickPayValue(2)));
        }

        quickButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paycheckEntry.setText(quickButton1.getText().toString().substring(1));
            }
        });

        quickButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paycheckEntry.setText(quickButton2.getText().toString().substring(1));
            }
        });

        quickButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paycheckEntry.setText(quickButton3.getText().toString().substring(1));
            }
        });

        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PaycheckDialogListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement PaycheckDialogListener");
        }
    }


    public interface PaycheckDialogListener {
        void logPaycheck(String amount);
    }
}
