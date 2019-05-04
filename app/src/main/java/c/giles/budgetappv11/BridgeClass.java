package c.giles.budgetappv11;

public class BridgeClass {
    static double deposit = 0;
    static double withdrawal = 0;
    static Budget newBudget;

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
