package giles.budgetapp

import android.content.Context
import java.io.*

/**
 * Data manager for the application.
 * Handles all storage and manipulation of app-related/user-generated data.
 */
object AppData: Serializable {
    //ArrayList of all the budget objects stored in the app
    var budgets = ArrayList<Budget>()
        private set

    //The percentage of global deposits that is left available for allocation
    var remainingPartition = 100.0
        private set

    fun addBudget(budget: Budget){
        budgets.add(budget)
        remainingPartition -= budget.partition
    }

    /**
     * Deposits the given amount into the given budget. If no budget is given, distributes the
     * given amount into all budgets based on partitions.
     *
     * @return The index of the budget that was deposited into, or -1 if multiple budgets
     */
    fun deposit(amount: Double, budget: Budget? = null): Int{
        return if(budget != null){
            budget.deposit(amount)
            budgets.indexOf(budget)
        } else {
            //Deposit into budgets depending on partition
            budgets.forEach{ b -> b.deposit((b.partition / 100) * amount) }
            -1
        }
    }

    fun saveToFile(context: Context, filename: String){
        val fos: FileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
        val os = ObjectOutputStream(fos)
        os.writeObject(this)
        os.close()
        fos.close()
    }

    fun loadFromFile(context: Context, filename: String){
        val fis: FileInputStream = context.openFileInput(filename)
        val inStream = ObjectInputStream(fis)
        val fileData = inStream.readObject() as AppData
        inStream.close()
        fis.close()

        this.budgets = fileData.budgets
        this.remainingPartition = fileData.remainingPartition
    }
}