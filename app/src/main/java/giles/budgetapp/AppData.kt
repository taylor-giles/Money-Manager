package giles.budgetapp

import android.content.Context
import java.io.*

object AppData: Serializable {
    private var budgets = ArrayList<Budget>()
    var remainingPartition = 100.0

    fun getBudgets(): ArrayList<Budget>{
        return budgets
    }

    fun addBudget(budget: Budget){
        budgets.add(budget)
        remainingPartition -= budget.partition
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