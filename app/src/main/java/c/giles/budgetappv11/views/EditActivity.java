package c.giles.budgetappv11.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class EditActivity extends AppCompatActivity implements ColorDialog.ColorDialogListener {
    private EditText nameEntry;
    private Switch partitionSwitch;
    private EditText partitionValueEntry;
    private ToggleButton partitionToggle;
    private TextView dollarSign;
    private TextView percentSign;
    private View colorPreview;
    private Button colorButton;
    private Button cancelButton;
    private Button doneButton;

    private List<Budget> budgets = new ArrayList<>(BudgetManager.getBudgetList());

    NumberFormat format = NumberFormat.getNumberInstance();

    Integer colorSelection = budgets.get(BudgetManager.getPlaceholder()).getColor();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_dialog);
        this.setFinishOnTouchOutside(false);
        setTitle("Edit Budget");


//        LayoutInflater inflater = getLayoutInflater();
//        View view = inflater.inflate(R.layout.activity_edit_dialog, null);

        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);

        nameEntry = findViewById(R.id.renameEntryBar);
        partitionSwitch = findViewById(R.id.editPartitionSwitch);
        partitionValueEntry = findViewById(R.id.editPartitionValue);
        partitionToggle = findViewById(R.id.editPartitionToggleButton);
        dollarSign = findViewById(R.id.editDollarSignSmall);
        percentSign = findViewById(R.id.editPercentSignSmall);
        colorButton = findViewById(R.id.edit_color_button);
        colorPreview = findViewById(R.id.edit_color_preview);
        cancelButton = findViewById(R.id.edit_cancel_button);
        doneButton = findViewById(R.id.edit_done_button);

        nameEntry.setText(budgets.get(BudgetManager.getPlaceholder()).getBudgetName());
        partitionSwitch.setChecked(budgets.get(BudgetManager.getPlaceholder()).isPartitioned());
        colorPreview.setBackgroundColor(budgets.get(BudgetManager.getPlaceholder()).getColor());

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

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("newName", nameEntry.getText().toString());
                if(partitionValueEntry.getText().toString().length() > 0) {
                    intent.putExtra("partitionValue", Double.parseDouble(partitionValueEntry.getText().toString()));
                }
                intent.putExtra("isPartitioned", partitionSwitch.isChecked());
                intent.putExtra("isAmountBased", partitionToggle.isChecked());
                intent.putExtra("newColor", colorSelection);
                setResult(RESULT_OK, intent);
                finish();
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

//        builder.setView(view)
//                .setTitle("Edit Budget")
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Nothing happens except dialog close
//                    }
//                })
//                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        listener.applyEdits(nameEntry.getText().toString(), partitionValueEntry.getText().toString(), partitionSwitch.isChecked(), partitionToggle.isChecked(), colorSelection);
//                    }
//                })
//        ;
    }

    public void openColorDialog(View view){
        ColorDialog dialog = new ColorDialog();
        dialog.setSelectedColor(budgets.get(BudgetManager.getPlaceholder()).getColor());
        dialog.show(getSupportFragmentManager(), "Color Dialog");
    }

    @Override
    public void applyColor(Integer color) {
        colorSelection = color;
        colorPreview.setBackgroundColor(color);
    }

//    public interface EditDialogListener{
//        void applyEdits(String newName, String partitionValue, boolean isPartitioned, boolean isAmountBased, Integer newColor);
//    }
}
