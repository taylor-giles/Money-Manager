package c.giles.budgetappv11;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Budget {

    String budgetName = "New Budget";
    double money = 0;
    boolean partition = false;
    boolean amountBased = true;
    double partitionValue = 0;

    TextView nameDisplay;
    TextView amountDisplay;
    TextView partitionDisplay;
    ImageButton renameButton;
    ImageButton deleteButton;
    Button depositButton;
    Button withdrawButton;



    public Budget(String budgetName, double initalBudget, boolean partition, boolean amountBased, double partitionValue){
        this.budgetName = budgetName;
        this.amountBased = amountBased;
        this.money = initalBudget;
        this.partition = partition;
        this.partitionValue = partitionValue;
    }

    public Budget(TextView nameDisplay, TextView moneyDisplay, TextView partitionDisplay, ImageButton renameButton, ImageButton deleteButton, Button depositButton, Button withdrawButton){
        this.nameDisplay = nameDisplay;
        this.amountDisplay = moneyDisplay;
        this.partitionDisplay = partitionDisplay;
        this.renameButton = renameButton;
        this.deleteButton = deleteButton;
        this.depositButton = depositButton;
        this.withdrawButton = withdrawButton;
    }

    public void deposit(double deposit){
        money += deposit;
    }

    public void withdraw(double withdrawal){
        money -= withdrawal;
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

    public Double getBudget(){
        return money;
    }

    public boolean isPartitioned(){
        return partition;
    }
    public void setPartitioned(boolean partitioned){
        partition = partitioned;
    }

    public boolean isAmountBased(){
        return amountBased;
    }
    public void setAmountBased(boolean isAmountBased){
        amountBased = isAmountBased;
    }

    public Double getPartitionValue(){
        return partitionValue;
    }
    public void setPartitionValue(Double value){
        partitionValue = value;
    }

    public void setNameDisplay(TextView view){
        nameDisplay = view;
    }
    public TextView getNameDisplay(){
        return nameDisplay;
    }

    public void setAmountDisplay(TextView view){
        amountDisplay = view;
    }
    public TextView getAmountDisplay(){
        return amountDisplay;
    }

    public void setPartitionDisplay(TextView view){
        partitionDisplay = view;
    }
    public TextView getPartitionDisplay(){
        return partitionDisplay;
    }

    public void setEditButton(ImageButton button){
        renameButton = button;
    }
    public ImageButton getEditButton(){
        return renameButton;
    }

    public void setDeleteButton(ImageButton button){
        deleteButton = button;
    }
    public ImageButton getDeleteButton(){
        return deleteButton;
    }


    public void setDepositButton(Button button){
        depositButton = button;
    }
    public Button getDepositButton(){
        return depositButton;
    }

    public void setWithdrawButton(Button button){
        withdrawButton = button;
    }
    public Button getWithdrawButton(){
        return withdrawButton;
    }

    public void refresh(){
        nameDisplay.setText(budgetName);
        amountDisplay.setText("$" + String.format("%.02f", money));
        if(partition){
            String temp = "";
            if(amountBased){
                temp += "$";
            }
            temp += String.format("%.02f", partitionValue);
            if(!amountBased){
                temp += "%";
            }
            temp += " of P.C.";
            partitionDisplay.setText(temp);
        }
    }


}
