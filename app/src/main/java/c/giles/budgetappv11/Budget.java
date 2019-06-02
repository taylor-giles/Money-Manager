package c.giles.budgetappv11;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Budget {

    String budgetName = "New Budget";
    double money = 0.0;
    boolean partition = false;
    boolean amountBased = true;
    double partitionValue = 0;

//    TextView nameDisplay;
//    TextView amountDisplay;
//    TextView partitionDisplay;
//    ImageButton renameButton;
//    ImageButton deleteButton;
//    Button depositButton;
//    Button withdrawButton;


    public Budget(){}

    public Budget(String budgetName, double initalBudget, boolean partition, boolean amountBased, double partitionValue){
        this.budgetName = budgetName;
        this.amountBased = amountBased;
        this.money = initalBudget;
        this.partition = partition;
        this.partitionValue = partitionValue;
    }

//    public Budget(TextView nameDisplay, TextView moneyDisplay, TextView partitionDisplay, ImageButton renameButton, ImageButton deleteButton, Button depositButton, Button withdrawButton){
//        this.nameDisplay = nameDisplay;
//        this.amountDisplay = moneyDisplay;
//        this.partitionDisplay = partitionDisplay;
//        this.renameButton = renameButton;
//        this.deleteButton = deleteButton;
//        this.depositButton = depositButton;
//        this.withdrawButton = withdrawButton;
//    }

    public Budget(Set<String> stringSet){
        List<String> list = new ArrayList<>(stringSet);
        budgetName = list.get(0);
        money = Double.parseDouble(list.get(1));
        partition = Boolean.parseBoolean(list.get(2));
        amountBased = Boolean.parseBoolean(list.get(3));
        partitionValue = Double.parseDouble(list.get(4));
    }

    public Set<String> asStringSet(){
        Double moneyTemp = money;
        Boolean partitionTemp = partition;
        Boolean amtBasedTemp = amountBased;
        Double partitionValTemp = partitionValue;

        List<String> list = new ArrayList<>();
        list.add(0, budgetName);
        list.add(1, moneyTemp.toString());
        list.add(2,partitionTemp.toString());
        list.add(3,amtBasedTemp.toString());
        list.add(4,partitionValTemp.toString());

        Set<String> stringSet = new HashSet<>(list);

        return stringSet;
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

    public Double getAmount(){
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

//    public void setNameDisplay(TextView view){
//        nameDisplay = view;
//    }
//    public TextView getNameDisplay(){
//        return nameDisplay;
//    }
//
//    public void setAmountDisplay(TextView view){
//        amountDisplay = view;
//    }
//    public TextView getAmountDisplay(){
//        return amountDisplay;
//    }
//
//    public void setPartitionDisplay(TextView view){
//        partitionDisplay = view;
//    }
//    public TextView getPartitionDisplay(){
//        return partitionDisplay;
//    }
//
//    public void setEditButton(ImageButton button){
//        renameButton = button;
//    }
//    public ImageButton getEditButton(){
//        return renameButton;
//    }
//
//    public void setDeleteButton(ImageButton button){
//        deleteButton = button;
//    }
//    public ImageButton getDeleteButton(){
//        return deleteButton;
//    }
//
//
//    public void setDepositButton(Button button){
//        depositButton = button;
//    }
//    public Button getDepositButton(){
//        return depositButton;
//    }
//
//    public void setWithdrawButton(Button button){
//        withdrawButton = button;
//    }
//    public Button getWithdrawButton(){
//        return withdrawButton;
//    }
//
//    public void refresh(){
//        nameDisplay.setText(budgetName);
//        amountDisplay.setText("$" + String.format("%.02f", money));
//        if(partition){
//            String temp = "";
//            if(amountBased){
//                temp += "$";
//            }
//            temp += String.format("%.02f", partitionValue);
//            if(!amountBased){
//                temp += "%";
//            }
//            temp += " of P.C.";
//            partitionDisplay.setText(temp);
//        }
//    }


}
