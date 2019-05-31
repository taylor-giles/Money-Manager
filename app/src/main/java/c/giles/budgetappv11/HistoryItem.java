package c.giles.budgetappv11;

import android.content.Context;
import android.graphics.Typeface;
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
import java.util.Objects;

import static android.graphics.Typeface.BOLD;

public class HistoryItem {
    private View view;
    private Budget budget;
    private Double amount;
    private GregorianCalendar time;
    private NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
    private DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

    public HistoryItem(){}

    public HistoryItem(Context context, HistoryData data){
        this(context, data.getBudget(), data.getAmount(), data.getTime(), data.isPaycheck(), data.isFromPaycheck());
    }

    public HistoryItem(Context context, Budget budget, Double amount, GregorianCalendar time){
        this(context, budget, amount, time, false, false);
    }

    public HistoryItem(Context context, Budget budget, Double amount, GregorianCalendar time, boolean isPaycheck, boolean isFromPaycheck){
        this.budget = budget;
        this.amount = amount;
        this.time = time;
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView timeView = new TextView(context);
        TextView nameView = new TextView(context);
        TextView moneyView = new TextView(context);
        Space leftPadding = new Space(context);
        Space timePadding = new Space(context);
        Space rightPadding = new Space(context);
        Space fillerSpace = new Space(context);

        timeView.setText(timeFormat.format(time.getTime()));
        timeView.setTextSize(14);
        timeView.setTextColor(context.getColor(android.R.color.darker_gray));

        nameView.setText(budget.getBudgetName());
        nameView.setTextSize(16);
        nameView.setTextColor(context.getColor(android.R.color.black));
        if(!isPaycheck) {
            if (amount > 0) {
                moneyView.setText("+" + moneyFormat.format(Math.abs(amount)));
                moneyView.setTextColor(context.getColor(android.R.color.holo_green_dark));
            } else if (amount < 0) {
                moneyView.setText("-" + moneyFormat.format(Math.abs(amount)));
                moneyView.setTextColor(context.getColor(android.R.color.holo_red_dark));
            } else if (amount == 0) {
                moneyView.setText(moneyFormat.format(amount));
                moneyView.setTextColor(context.getColor(android.R.color.darker_gray));
            }
            moneyView.setTextSize(16);

        } else {
            //nameView.setTextColor(context.getColor(android.R.color.holo_blue_dark));

            if (amount > 0) {
                moneyView.setText(moneyFormat.format(Math.abs(amount)));
                moneyView.setTextColor(context.getColor(android.R.color.holo_blue_dark));
            } else if (amount < 0) {
                moneyView.setText(moneyFormat.format(Math.abs(amount)));
                moneyView.setTextColor(context.getColor(android.R.color.holo_blue_dark));
            } else if (amount == 0) {
                moneyView.setText(moneyFormat.format(amount));
                moneyView.setTextColor(context.getColor(android.R.color.darker_gray));
            }
            moneyView.setTextSize(16);
            moneyView.setTypeface(Typeface.defaultFromStyle(BOLD));
            nameView.setTypeface(Typeface.defaultFromStyle(BOLD));
        }

        //Indent the views of the history items that WERE the result of a paycheck being logged
        if(!isFromPaycheck) {
            leftPadding.setLayoutParams(new LinearLayout.LayoutParams(20, LinearLayout.LayoutParams.MATCH_PARENT));
            timePadding.setLayoutParams(new LinearLayout.LayoutParams(50, LinearLayout.LayoutParams.MATCH_PARENT));
            rightPadding.setLayoutParams(new LinearLayout.LayoutParams(20, LinearLayout.LayoutParams.MATCH_PARENT));
            fillerSpace.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        } else {
            //nameView.setTextColor(context.getColor(android.R.color.holo_blue_dark));
            moneyView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            nameView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            leftPadding.setLayoutParams(new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.MATCH_PARENT));
            timePadding.setLayoutParams(new LinearLayout.LayoutParams(50, LinearLayout.LayoutParams.MATCH_PARENT));
            rightPadding.setLayoutParams(new LinearLayout.LayoutParams(20, LinearLayout.LayoutParams.MATCH_PARENT));
            fillerSpace.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1));
        }

        layout.addView(leftPadding);
        if(!isFromPaycheck) {
            layout.addView(timeView);
            layout.addView(timePadding);
        }
        layout.addView(nameView);
        layout.addView(fillerSpace);
        layout.addView(moneyView);
        layout.addView(rightPadding);

        view = layout;
    }

    public Calendar getCalendar(){
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
