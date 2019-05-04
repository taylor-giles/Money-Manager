package c.giles.budgetappv11;

public class Budget {

    String budgetName = "New Budget";
    double money = 0;
    boolean partition = false;
    boolean amountBased = true;
    double partitionValue = 0;


    public Budget(String budgetName, double initalBudget, boolean partition, boolean amountBased, double partitionValue){
        this.budgetName = budgetName;
        this.amountBased = amountBased;
        this.money = initalBudget;
        this.partition = partition;
        this.partitionValue = partitionValue;
    }

    public void deposit(double deposit){
        money += deposit;
    }

    public void withdraw(double withdrawal){
        money -= withdrawal;
    }

    public double getAmount(){
        return money;
    }

    public void setName(String newName){
        budgetName = newName;
    }

    public void setBudget(double newAmount){
        money = newAmount;
    }

    public String getBudgetName(){
        return budgetName;
    }

    public double getBudget(){
        return money;
    }

    public boolean isPartitioned(){
        return partition;
    }

    public boolean isAmountBased(){
        return amountBased;
    }

    public double getPartitionValue(){
        return partitionValue;
    }



}
