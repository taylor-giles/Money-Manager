package c.giles.budgetappv11;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import c.giles.budgetappv11.views.BudgetLayout;

public class BudgetHandler {

    private static List<Budget> budgetList = new ArrayList<>();
    static ViewGroup budgetDisplayWindow;
    private static int placeholder = -1;
    static boolean modified = false;
    private static Double[] quickPayAmounts = new Double[3];
    private static Double[] quickDepositAmounts = new Double[3];
    private static Double[] quickWithdrawAmounts = new Double[3];
    private static String defaultBudgetName;

    public static void addBudget(Budget newBudget){
        budgetList.add(newBudget);
    }

    public static void addBudget(int index, Budget newBudget){
        budgetList.add(index, newBudget);
    }

    public static boolean isModified(){
        return modified;
    }

    public static void setModified(boolean bool){
        modified = bool;
    }

    public static List<Budget> getBudgetList(){
        return new ArrayList<>(budgetList);
    }

    public static void setBudgetList(List<Budget> newList){
        budgetList = new ArrayList<>(newList);
    }

    public static void removeBudget(int index){
        budgetList.remove(index);
        modified = true;
    }

    public static void setBudgetDisplayWindow(ViewGroup layout){
        budgetDisplayWindow = layout;
    }

    public static void setPlaceholder(int i){
        placeholder = i;
    }

    public static int getPlaceholder(){
        return placeholder;
    }

    public static double getTotalPercentPartitioned(){
        double totalPercentPartitioned = 0;
        for(Budget budget : budgetList){
            if(budget.isPartitioned() && !budget.isAmountBased()){
                totalPercentPartitioned += budget.getPartitionValue();
            }
        }
        return totalPercentPartitioned;
    }

    public static Double getQuickPayValue(int index){
        return quickPayAmounts[index];
    }

    public static Double getQuickDepositValue(int index){
        return quickDepositAmounts[index];
    }

    public static Double[] getQuickWithdrawValues(){
        return quickWithdrawAmounts;
    }

    public static Double[] getQuickPayValues(){
        return quickPayAmounts;
    }

    public static Double[] getQuickDepositValues(){
        return quickDepositAmounts;
    }

    public static Double getQuickWithdrawValue(int index){
        return quickWithdrawAmounts[index];
    }

    public static void setQuickPayAmounts(Double[] amounts){
        quickPayAmounts = amounts;
    }

    public static void setQuickDepositAmounts(Double[] amounts){
        quickDepositAmounts = amounts;
    }

    public static void setQuickWithdrawAmounts(Double[] amounts){
        quickWithdrawAmounts = amounts;
    }

    public static String getDefaultBudgetName(){
        return defaultBudgetName;
    }

    public static void setDefaultBudgetName(String name){
        defaultBudgetName = name;
    }

}