package c.giles.budgetappv11;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import c.giles.budgetappv11.views.BudgetLayout;

public class BudgetManager {

    private static List<Budget> budgetList = new ArrayList<>();
    static ViewGroup budgetDisplayWindow;
    private static int placeholder = -1;
    static boolean modified = false;
    private static List<Double> quickPayAmounts = new ArrayList<>();
    private static List<Double> quickDepositAmounts = new ArrayList<>();
    private static List<Double> quickWithdrawAmounts = new ArrayList<>();
    private static String defaultBudgetName;
    private static Double totalFunds;

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
        return quickPayAmounts.get(index);
    }

    public static Double getQuickDepositValue(int index){
        return quickDepositAmounts.get(index);
    }

    public static List<Double> getQuickWithdrawValues(){
        return new ArrayList<>(quickWithdrawAmounts);
    }

    public static List<Double> getQuickPayValues(){
        return new ArrayList<>(quickPayAmounts);
    }

    public static List<Double> getQuickDepositValues(){
        return new ArrayList<>(quickDepositAmounts);
    }

    public static Double getQuickWithdrawValue(int index){
        return quickWithdrawAmounts.get(index);
    }

    public static void setQuickPayAmounts(List<Double> amounts){
        quickPayAmounts = new ArrayList<>(amounts);
    }

    public static void setQuickDepositAmounts(List<Double> amounts){
        quickDepositAmounts = new ArrayList<>(amounts);
    }

    public static void setQuickWithdrawAmounts(List<Double> amounts){
        quickWithdrawAmounts = new ArrayList<>(amounts);
    }

    public static String getDefaultBudgetName(){
        return defaultBudgetName;
    }

    public static void setDefaultBudgetName(String name){
        defaultBudgetName = name;
    }

    public static void setTotalFunds(Double total){
        totalFunds = total;
    }

    public static Double getTotalFunds(){
        return totalFunds;
    }

}