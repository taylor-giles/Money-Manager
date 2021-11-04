package giles.budgetapp

import android.graphics.Color
import java.io.Serializable

class Budget(
    var name: String,
    var amount: Double = 0.0,
    var partition: Double = 0.0,
    var color: Int = Color.BLACK
) : Serializable {

    fun deposit(amount: Double){
        this.amount += amount
    }
}