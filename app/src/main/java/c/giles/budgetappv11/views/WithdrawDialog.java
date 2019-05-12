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

import c.giles.budgetappv11.R;


public class WithdrawDialog extends AppCompatDialogFragment {

    private WithdrawDialogListener listener;
    private EditText withdrawalEntry;
    private Button quickButton1;
    private Button quickButton2;
    private Button quickButton3;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.withdraw_dialog, null);

        withdrawalEntry = (EditText)view.findViewById(R.id.withdrawalEntryBar);
        quickButton1 = (Button)view.findViewById(R.id.quickWithdraw1);
        quickButton2 = (Button)view.findViewById(R.id.quickWithdraw2);
        quickButton3 = (Button)view.findViewById(R.id.quickWithdraw3);
        builder.setView(view)
                .setTitle("Withdraw")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                })
                .setPositiveButton("Withdraw", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        String amount = withdrawalEntry.getText().toString();
                        listener.applyWithdraw(amount);
                    }
                })
        ;


        quickButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawalEntry.setText(quickButton1.getText().toString().substring(1));
            }
        });

        quickButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawalEntry.setText(quickButton2.getText().toString().substring(1));
            }
        });

        quickButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawalEntry.setText(quickButton3.getText().toString().substring(1));
            }
        });

        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (WithdrawDialogListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement WithdrawDialogListener");
        }
    }


    public interface WithdrawDialogListener {
        void applyWithdraw(String amount);
    }
}
