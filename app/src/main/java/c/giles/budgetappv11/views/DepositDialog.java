package c.giles.budgetappv11.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.Button;

import c.giles.budgetappv11.R;

/*
public class DepositDialog extends AppCompat {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.deposit_layout, null))
                .setPositiveButton("Deposit", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        //TODO send information to main activity
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        DepositDialog.this.getDialog().cancel();
                    }
                })
        ;

        Button quickDeposit1 = (Button) findViewById(R.id.quickDepositButton1);
        builder.setTitle("Add Money");


        return builder.create();
    }
}
*/