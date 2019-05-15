package c.giles.budgetappv11.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import c.giles.budgetappv11.BudgetHandler;
import c.giles.budgetappv11.Budget;
import c.giles.budgetappv11.R;

public class EditDialog extends AppCompatDialogFragment {
    private EditText nameEntry;
    private Switch partitionSwitch;
    private EditText partitionValueEntry;
    private ToggleButton partitionToggle;
    private TextView dollarSign;
    private TextView percentSign;

    private EditDialogListener listener;
    private List<Budget> budgets = new ArrayList<>(BudgetHandler.getBudgetList());

    NumberFormat format = NumberFormat.getNumberInstance();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_dialog, null);


        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        nameEntry = view.findViewById(R.id.renameEntryBar);
        partitionSwitch = view.findViewById(R.id.editPartitionSwitch);
        partitionValueEntry = view.findViewById(R.id.editPartitionValue);
        partitionToggle = view.findViewById(R.id.editPartitionToggleButton);
        dollarSign = view.findViewById(R.id.editDollarSignSmall);
        percentSign = view.findViewById(R.id.editPercentSignSmall);

        nameEntry.setText(budgets.get(BudgetHandler.getPlaceholder()).getBudgetName());
        partitionSwitch.setChecked(budgets.get(BudgetHandler.getPlaceholder()).isPartitioned());
        if(budgets.get(BudgetHandler.getPlaceholder()).isPartitioned()) {
            partitionToggle.setChecked(budgets.get(BudgetHandler.getPlaceholder()).isAmountBased());
            partitionValueEntry.setText(format.format(budgets.get(BudgetHandler.getPlaceholder()).getPartitionValue()));
        }

        if(partitionToggle.isChecked()){
            dollarSign.setText("$");
            percentSign.setText("");
        }
        if(!partitionToggle.isChecked()){
            dollarSign.setText("");
            percentSign.setText("%");
        }

        partitionToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(partitionToggle.isChecked()){
                    dollarSign.setText("$");
                    percentSign.setText("");
                }
                if(!partitionToggle.isChecked()){
                    dollarSign.setText("");
                    percentSign.setText("%");
                }
            }
        });

        builder.setView(view)
                .setTitle("Edit Budget")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Leave empty: nothing happens except dialog close
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyEdits(nameEntry.getText().toString(), partitionValueEntry.getText().toString(), partitionSwitch.isChecked(), partitionToggle.isChecked());
                    }
                })
        ;
        return builder.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            listener = (EditDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement EditDialogListener");
        }
    }
    public interface EditDialogListener{
        void applyEdits(String newName, String partitionValue, boolean isPartitioned, boolean isAmountBased);
    }
}
