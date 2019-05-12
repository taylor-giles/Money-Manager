package c.giles.budgetappv11;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import c.giles.budgetappv11.views.BudgetLayout;

public class BridgeClass {
    static double deposit = 0;
    static double withdrawal = 0;
    static Budget newBudget;
    static List<Budget> budgetList = new ArrayList<>();
    static List<LinearLayout> budgetLayouts = new ArrayList<>();
    static ViewGroup budgetDisplayWindow;

    public static void addBudget(Budget newBudget){
        budgetList.add(newBudget);
    }

    public static List<Budget> getBudgetList(){
        return new ArrayList<>(budgetList);
    }

    public static void setBudgetList(List<Budget> newList){
        budgetList = new ArrayList<>(newList);
    }

    public void removeBudget(int index){
        budgetList.remove(index);
    }

    public static void setBudgetDisplayWindow(ViewGroup layout){
        budgetDisplayWindow = layout;
    }


    public static void setNewBudget(Budget budget){
        newBudget = budget;
    }
    public static Budget getNewBudget(){
        return newBudget;
    }

    public static void setDeposit(double value){
        deposit = value;
    }
    public static double getDeposit(){
        return deposit;
    }
}
