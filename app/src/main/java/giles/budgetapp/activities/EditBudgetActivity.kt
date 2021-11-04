package giles.budgetapp.activities

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import giles.budgetapp.R
import giles.util.BasicExpandableView
import giles.util.Expandable
import android.graphics.PorterDuff
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener
import com.madrapps.pikolo.ColorPicker
import giles.budgetapp.AppData
import giles.budgetapp.Budget
import giles.util.ColorUtils


class EditBudgetActivity : AppCompatActivity() {
    private lateinit var partitionExpandable : Expandable
    private lateinit var colorPicker: ColorPicker
    private lateinit var colorText: EditText
    private lateinit var colorPreviewText: TextView
    private lateinit var nameText: EditText
    private lateinit var amountText: EditText
    private lateinit var partitionText: EditText
    private var budgetColor = Color.BLACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_budget)
        nameText = findViewById(R.id.text_edit_budget_name)
        amountText = findViewById(R.id.text_edit_initial_amount)
        partitionText = findViewById(R.id.text_edit_budget_partition)

        //TODO: Get budget being edited and handle it accordingly

        //Cancel button action
        findViewById<Button>(R.id.btn_cancel_edit_budget).setOnClickListener { finish() }

        //Save button action
        findViewById<Button>(R.id.btn_save_edit_budget).setOnClickListener {
            var valid = true

            //Get name
            var name = nameText.text.toString()
            if(name.isEmpty() || name == ""){
                name = "Unnamed Budget"
            }

            //Get amount
            var amt = amountText.text.toString().toDoubleOrNull()
            if(amt == null){
                amt = 0.0
            }

            //Get partition
            val partition = partitionText.text.toString().toDoubleOrNull()
            if(partition == null || partition < 0){
                valid = false
                Toast.makeText(applicationContext,
                    "Partition must be between 0-100", Toast.LENGTH_SHORT).show()
            } else if(partition > AppData.remainingPartition){
                valid = false
                Toast.makeText(applicationContext,
                    "Total partitioning across all budgets may not exceed 100%. " +
                            "${AppData.remainingPartition}% is available.", Toast.LENGTH_LONG).show()
            }

            if(valid){
                AppData.addBudget(Budget(name, amt, partition!!, budgetColor))
                finish()
            }
        }

        //Make the partition help expandable
        partitionExpandable = BasicExpandableView(
            content = findViewById<TextView>(R.id.text_partition_help),
            button = findViewById(R.id.btn_partition_help)
        )

        //Color selector behavior
        colorText = findViewById(R.id.text_edit_budget_color)
        colorPicker = findViewById(R.id.colorpicker_edit_budget)
        colorPreviewText = findViewById(R.id.text_color_preview)
        colorPicker.setColorSelectionListener(object : SimpleColorSelectionListener() {
            override fun onColorSelected(color: Int) { setColor(color, true) }
        })
        colorText.setOnEditorActionListener { textView, _, _ ->
            //Extract color from string and set the color
            //Append zeroes (in case less than 6 chars were provided) and take only 1st 7 chars ("#")
            setColor(Color.parseColor(("#" + textView.text.toString().trim() + "000000").substring(0, 7)))

            //Consume action
            true
        }
        setColor(budgetColor)
    }


    private fun setColor(color: Int, ignoreColorPicker: Boolean = false){
        budgetColor = color
        if(!ignoreColorPicker){
            colorPicker.setColor(color)
        }
        colorText.setText(ColorUtils.getHexString(color))
        colorPreviewText.setBackgroundColor(color)

        //Set text color of preview to contrast background
        colorPreviewText.setTextColor(ColorUtils.getContrastingTextColor(color))
    }
}