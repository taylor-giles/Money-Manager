package c.giles.budgetappv11;

import java.util.GregorianCalendar;

public class HistoryData {
    private Budget budget;
    private Double amount;
    private GregorianCalendar time;
    private boolean paycheck = false;
    private boolean fromPaycheck = false;
    private HistoryData paycheckData = null;

    public HistoryData(Budget budget, Double amount, GregorianCalendar time){
        this.budget = budget;
        this.amount = amount;
        this.time = time;
    }

    public HistoryData(Budget budget, Double amount, GregorianCalendar time, HistoryData paycheckData){
        this(budget, amount, time);
        this.paycheckData = paycheckData;
        fromPaycheck = true;
    }

    public HistoryData(Double paycheckAmount, GregorianCalendar time){
        this.budget = new Budget("Paycheck Logged", paycheckAmount,false,false,0);
        this.amount = paycheckAmount;
        this.time = time;
        paycheck = true;
    }


    public HistoryData getPaycheckData(){
        return paycheckData;
    }

    public boolean isFromPaycheck(){
        return fromPaycheck;
    }
    public boolean isPaycheck(){
        return paycheck;
    }

    public Budget getBudget() {
        return budget;
    }

    public Double getAmount(){
        return amount;

    }

    public GregorianCalendar getTime(){
        return time;
    }

}
