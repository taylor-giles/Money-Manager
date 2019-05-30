package c.giles.budgetappv11.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import c.giles.budgetappv11.BudgetHandler;
import c.giles.budgetappv11.HistoryData;
import c.giles.budgetappv11.HistoryItem;
import c.giles.budgetappv11.R;

import static android.content.Context.MODE_PRIVATE;

public class HistoryFragment extends Fragment {

    private List<HistoryItem> historyList = new ArrayList<>();
    private List<HistoryData> historyDataList = new ArrayList<>();
    private LinearLayout historyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        historyView = (LinearLayout) view.findViewById(R.id.history_list_layout);

        loadSharedPreferences();

        loadHistory();


        return view;
    }


    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("history data", MODE_PRIVATE);
        Gson gson = new Gson();

        //Get history data
        String loadedHistoryList = sharedPreferences.getString("history list", null);
        Type historyType = new TypeToken<List<HistoryData>>() {}.getType();
        List<HistoryData> temp = new ArrayList<>();
        temp = gson.fromJson(loadedHistoryList, historyType);
        if(temp == null){
            historyDataList = new ArrayList<>();
            historyList = new ArrayList<>();
        } else {
            historyDataList = new ArrayList<>(temp);
            for(HistoryData data : historyDataList){
                historyList.add(new HistoryItem(getActivity(), data));
            }
        }
    }

    private void saveSharedPreferences(){
        //Update the history shared preferences
        Gson gson = new Gson();
        SharedPreferences historyData = Objects.requireNonNull(getActivity()).getSharedPreferences("history data", MODE_PRIVATE);
        SharedPreferences.Editor historyEditor = historyData.edit();
        List<HistoryData> historyTemp = new ArrayList<>(historyDataList);
        String historyJson = gson.toJson(historyTemp);
        historyEditor.putString("history list", historyJson);
        historyEditor.apply();
    }

    //Fills the historyView with all of the information contained in the historyList
    private void loadHistory(){
        for (int i = 0; i < historyList.size(); i++) {
            HistoryItem item = historyList.get(i);

            //Add a header for each new day
            if(i == 0 ||
                    item.getCalendar().get(Calendar.YEAR) != historyList.get(i-1).getCalendar().get(Calendar.YEAR) ||
                    item.getCalendar().get(Calendar.MONTH) != historyList.get(i-1).getCalendar().get(Calendar.MONTH) ||
                    item.getCalendar().get(Calendar.DAY_OF_MONTH) != historyList.get(i-1).getCalendar().get(Calendar.DAY_OF_MONTH)){


                LinearLayout dateLayout = new LinearLayout(getActivity());
                View leftLine = new View(getActivity());
                View rightLine = new View(getActivity());
                TextView dateView = new TextView(getActivity());
                Space leftSpace = new Space(getActivity());
                Space rightSpace = new Space(getActivity());

                leftLine.setLayoutParams(new LinearLayout.LayoutParams(0,2,1));
                rightLine.setLayoutParams(new LinearLayout.LayoutParams(0,2,1));

                leftLine.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                rightLine.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

                dateView.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(item.getCalendar().getTime()));
                dateView.setTextSize(14);
                dateView.setTextColor(getResources().getColor(android.R.color.darker_gray));

                leftSpace.setLayoutParams(new LinearLayout.LayoutParams(50, LinearLayout.LayoutParams.MATCH_PARENT));
                rightSpace.setLayoutParams(new LinearLayout.LayoutParams(50, LinearLayout.LayoutParams.MATCH_PARENT));


                dateLayout.setGravity(Gravity.CENTER_VERTICAL);
                dateLayout.addView(leftLine);
                dateLayout.addView(leftSpace);
                dateLayout.addView(dateView);
                dateLayout.addView(rightSpace);
                dateLayout.addView(rightLine);

                historyView.addView(dateLayout);
            }

            //Add the item
            View topSpace = new View(getActivity());
            topSpace.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 25));

            View bottomSpace = new View(getActivity());
            bottomSpace.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 25));

            View divider = new View(getActivity());
            divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
            divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

            Space lastItemOfDaySpace = new Space(getActivity());
            lastItemOfDaySpace.setLayoutParams(new LinearLayout.LayoutParams(1,75));

            LinearLayout itemLayout = (LinearLayout)item.asView();

            //Make every other layout gray (for visual separation)
//            if(i%2 == 1){
//                topSpace.setBackgroundColor(getResources().getColor(R.color.light_gray));
//                bottomSpace.setBackgroundColor(getResources().getColor(R.color.light_gray));
//                itemLayout.setBackgroundColor(getResources().getColor(R.color.light_gray));
//            }

            historyView.addView(topSpace);
            historyView.addView(itemLayout);
            historyView.addView(bottomSpace);
            if(i != historyDataList.size() -1){
                if(item.getCalendar().get(Calendar.YEAR) != historyList.get(i+1).getCalendar().get(Calendar.YEAR) ||
                        item.getCalendar().get(Calendar.MONTH) != historyList.get(i+1).getCalendar().get(Calendar.MONTH) ||
                        item.getCalendar().get(Calendar.DAY_OF_MONTH) != historyList.get(i+1).getCalendar().get(Calendar.DAY_OF_MONTH)){
                    historyView.addView(lastItemOfDaySpace);
                } else {
                    historyView.addView(divider);
                }
            }
        }
    }

    public void clearHistory(View v){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        historyDataList = new ArrayList<>();
                        historyList = new ArrayList<>();
                        historyView.removeAllViews();
                        saveSharedPreferences();
                        loadHistory();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //Do nothing
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Are you sure you want to clear all history items?");
        builder.setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show()
        ;

    }
}