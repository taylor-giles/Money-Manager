package c.giles.budgetappv11;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import c.giles.budgetappv11.views.BudgetLayout;

public class BudgetHandler {

    static double deposit = 0;
    static double withdrawal = 0;
    static List<Budget> budgetList = new ArrayList<>();
    static List<LinearLayout> budgetLayouts = new ArrayList<>();
    static ViewGroup budgetDisplayWindow;
    static int placeholder = -1;
    static boolean modified = false;

    public static void addBudget(Budget newBudget){
        budgetList.add(newBudget);
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

}
