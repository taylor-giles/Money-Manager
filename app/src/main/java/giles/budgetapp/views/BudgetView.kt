package giles.budgetapp.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import giles.budgetapp.Budget
import giles.budgetapp.R
import giles.util.ColorUtils

class BudgetView @JvmOverloads constructor(
    context : Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    budget: Budget? = null
) : ConstraintLayout(context, attrs, defStyle) {
    var budget: Budget? = null
        private set

    //Components
    private val depositButton: Button
    private val withdrawButton: Button
    private val partitionText: TextView
    private val nameText: TextView
    private val amountText: TextView
    private val colorView: View

    init {
        inflate(context, R.layout.layout_budget_view, this)
        this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        //Get components
        depositButton = this.findViewById(R.id.btn_budget_item_deposit)
        withdrawButton = this.findViewById(R.id.btn_budget_item_withdraw)
        partitionText = this.findViewById(R.id.text_budget_item_partition)
        nameText = this.findViewById(R.id.text_budget_item_name)
        amountText = this.findViewById(R.id.text_budget_item_amount)
        colorView = this.findViewById(R.id.view_budget_color)

        //Set budget if it is given
        if(budget != null){
            setBudget(budget)
        }
    }

    fun setBudget(budget: Budget){
        this.budget = budget
        nameText.text = budget.name
        partitionText.text = budget.partition.toString()
        amountText.text = String.format("%.2f", budget.amount)
        colorView.setBackgroundColor(budget.color)
        depositButton.backgroundTintList = ColorStateList.valueOf(budget.color)
        withdrawButton.backgroundTintList = ColorStateList.valueOf(budget.color)
        depositButton.setTextColor(ColorUtils.getContrastingTextColor(budget.color))
        withdrawButton.setTextColor(ColorUtils.getContrastingTextColor(budget.color))

        //Button behaviors
        depositButton.setOnClickListener {
            //TODO: Deposit into budget
        }

        withdrawButton.setOnClickListener {
            //TODO: Withdraw from budget
        }
    }
}


/**
 * A RecyclerView adapter for displaying budgets
 */
class BudgetViewAdapter(
    var dataSet: List<Budget>
): RecyclerView.Adapter<BudgetViewAdapter.BudgetViewHolder>(){

    /**
     * A ViewHolder for views displaying budgets
     */
    class BudgetViewHolder constructor(
        private val view: BudgetView
    ) : RecyclerView.ViewHolder(view){

        fun setBudget(budget: Budget){
            view.setBudget(budget)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        //Create a new ViewHolder for a BudgetView
        return BudgetViewHolder(BudgetView(parent.context))
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        //Set budget of the view
        holder.setBudget(dataSet[position])
    }

    //There is an item for every value in the data set
    override fun getItemCount() = dataSet.size
}