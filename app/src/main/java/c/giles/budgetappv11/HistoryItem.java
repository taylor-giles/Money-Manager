package c.giles.budgetappv11;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HistoryItem {
    private View view;
    private Budget budget;
    private Double amount;
    private GregorianCalendar time;
    private NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
    private DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

    public HistoryItem(){}

    public HistoryItem(Context context, Budget budget, Double amount, GregorianCalendar time){
        this.budget = budget;
        this.amount = amount;
        this.time = time;
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView timeView = new TextView(context);
        TextView nameView = new TextView(context);
        TextView moneyView = new TextView(context);
        Space padding = new Space(context);
        Space rightPadding = new Space(context);
        Space fillerSpace = new Space(context);

        timeView.setText(timeFormat.format(time.getTime()));
        timeView.setTextSize(14);
        timeView.setTextColor(context.getColor(android.R.color.darker_gray));

        nameView.setText(budget.getBudgetName());
        nameView.setTextSize(16);
        nameView.setTextColor(context.getColor(android.R.color.black));

        if(amount > 0){
            moneyView.setText("+" + moneyFormat.format(Math.abs(amount)));
            moneyView.setTextColor(context.getColor(android.R.color.holo_green_dark));
        } else if(amount < 0){
            moneyView.setText("-" + moneyFormat.format(Math.abs(amount)));
            moneyView.setTextColor(context.getColor(android.R.color.holo_red_dark));
        } else if(amount == 0){
            moneyView.setText(moneyFormat.format(amount));
            moneyView.setTextColor(context.getColor(android.R.color.darker_gray));
        }
        moneyView.setTextSize(16);

        padding.setLayoutParams(new LinearLayout.LayoutParams(10, LinearLayout.LayoutParams.MATCH_PARENT));
        rightPadding.setLayoutParams(new LinearLayout.LayoutParams(10, LinearLayout.LayoutParams.MATCH_PARENT));
        fillerSpace.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));

        layout.addView(timeView);
        layout.addView(padding);
        layout.addView(nameView);
        layout.addView(fillerSpace);
        layout.addView(moneyView);
        layout.addView(rightPadding);

        view = layout;
    }

    public Calendar getDate(){
        return time;
    }

    public long getTimeInMillis(){
        return time.getTimeInMillis();
    }

    public void setView(View historyView){
        view = historyView;
    }

    public View asView(){
        return view;
    }
}
