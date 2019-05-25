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


public class DepositDialog extends AppCompatDialogFragment {

    private DepositDialogListener listener;
    private EditText depositEntry;
    private Button quickButton1;
    private Button quickButton2;
    private Button quickButton3;

    NumberFormat format = NumberFormat.getNumberInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.deposit_dialog, null);

        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);

        depositEntry = (EditText)view.findViewById(R.id.depositEntryBar);
        quickButton1 = (Button)view.findViewById(R.id.quickDeposit1);
        quickButton2 = (Button)view.findViewById(R.id.quickDeposit2);
        quickButton3 = (Button)view.findViewById(R.id.quickDeposit3);

        quickButton1.setText("$" + format.format(BudgetHandler.getQuickDepositValue(0)));
        quickButton2.setText("$" + format.format(BudgetHandler.getQuickDepositValue(1)));
        quickButton3.setText("$" + format.format(BudgetHandler.getQuickDepositValue(2)));

        builder.setView(view)
                .setTitle("Deposit")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                })
                .setPositiveButton("Deposit", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        String amount = depositEntry.getText().toString();
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
