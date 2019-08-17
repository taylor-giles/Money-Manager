package c.giles.budgetappv11.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import c.giles.budgetappv11.BudgetManager;
import c.giles.budgetappv11.Budget;
import c.giles.budgetappv11.R;

public class EditDialog extends AppCompatDialogFragment implements ColorDialog.ColorDialogListener {
    private EditText nameEntry;
    private Switch partitionSwitch;
    private EditText partitionValueEntry;
    private ToggleButton partitionToggle;
    private TextView dollarSign;
    private TextView percentSign;
    private View colorPreview;
    private Button colorButton;

    private EditDialogListener listener;
    private List<Budget> budgets = new ArrayList<>(BudgetManager.getBudgetList());

    NumberFormat format = NumberFormat.getNumberInstance();

    Integer colorSelection = budgets.get(BudgetManager.getPlaceholder()).getColor();


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
        colorButton = view.findViewById(R.id.edit_color_button);

        nameEntry.setText(budgets.get(BudgetManager.getPlaceholder()).getBudgetName());
        partitionSwitch.setChecked(budgets.get(BudgetManager.getPlaceholder()).isPartitioned());

        partitionValueEntry.setEnabled(partitionSwitch.isChecked());
        partitionSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partitionValueEntry.setEnabled(partitionSwitch.isChecked());
            }
        });

        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorDialog(v);
            }
        });

        if(budgets.get(BudgetManager.getPlaceholder()).isPartitioned()) {
            partitionToggle.setChecked(budgets.get(BudgetManager.getPlaceholder()).isAmountBased());
            partitionValueEntry.setText(format.format(budgets.get(BudgetManager.getPlaceholder()).getPartitionValue()));
        }

        if(partitionToggle.isChecked()){
            dollarSign.setVisibility(View.VISIBLE);
            percentSign.setVisibility(View.INVISIBLE);
        }
        if(!partitionToggle.isChecked()){
            dollarSign.setVisibility(View.INVISIBLE);
            percentSign.setVisibility(View.VISIBLE);
        }

        partitionToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(partitionToggle.isChecked()){
                    dollarSign.setVisibility(View.VISIBLE);
                    percentSign.setVisibility(View.INVISIBLE);
                }
                if(!partitionToggle.isChecked()){
                    dollarSign.setVisibility(View.INVISIBLE);
                    percentSign.setVisibility(View.VISIBLE);
                }
            }
        });

        builder.setView(view)
                .setTitle("Edit Budget")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Nothing happens except dialog close
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyEdits(nameEntry.getText().toString(), partitionValueEntry.getText().toString(), partitionSwitch.isChecked(), partitionToggle.isChecked(), colorSelection);
                    }
                })
        ;
        return builder.create();
    }

    public void openColorDialog(View view){
        ColorDialog dialog = new ColorDialog();
        dialog.setSelectedColor(budgets.get(BudgetManager.getPlaceholder()).getColor());
        dialog.show(getActivity().getSupportFragmentManager(), "Color Dialog");
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

    @Override
    public void applyColor(Integer color) {
        colorSelection = color;
    }

    public interface EditDialogListener{
        void applyEdits(String newName, String partitionValue, boolean isPartitioned, boolean isAmountBased, Integer newColor);
    }
}
