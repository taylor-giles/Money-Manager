package c.giles.budgetappv11.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;

import c.giles.budgetappv11.Budget;
import c.giles.budgetappv11.BudgetHandler;
import c.giles.budgetappv11.HistoryItem;
import c.giles.budgetappv11.R;

import static android.content.Context.MODE_PRIVATE;

public class HistoryFragment extends Fragment {

    private List<HistoryItem> historyList = new ArrayList<>();
    private LinearLayout historyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        historyView = (LinearLayout) Objects.requireNonNull(getActivity()).findViewById(R.id.history_list_layout);

        loadSharedPreferences();

        loadHistory();


        return (View) inflater.inflate(R.layout.fragment_history, container, false);
    }


    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("history data", MODE_PRIVATE);
        Gson gson = new Gson();

        //Get history data
        String loadedHistoryList = sharedPreferences.getString("history list", null);
        Type historyType = new TypeToken<List<HistoryItem>>() {}.getType();
        List<HistoryItem> temp = new ArrayList<>();
        temp = gson.fromJson(loadedHistoryList, historyType);
        if(temp == null){
            historyList = new ArrayList<>();
        } else {
            historyList = new ArrayList<>(temp);
        }
    }

    //Fills the historyView with all of the information contained in the historyList
    private void loadHistory(){
        for (int i = 0; i < historyList.size(); i++) {
            HistoryItem item = historyList.get(i);

            //Add a header for each new day
            if(i == 0 ||
                    item.getDate().get(Calendar.YEAR) != historyList.get(i-1).getDate().get(Calendar.YEAR) ||
                    item.getDate().get(Calendar.MONTH) != historyList.get(i-1).getDate().get(Calendar.MONTH) ||
                    item.getDate().get(Calendar.DAY_OF_MONTH) != historyList.get(i-1).getDate().get(Calendar.DAY_OF_MONTH)){


                LinearLayout dateLayout = new LinearLayout(getActivity());
                View leftLine = new View(getActivity());
                View rightLine = new View(getActivity());
                TextView dateView = new TextView(getActivity());
                Space leftSpace = new Space(getActivity());
                Space rightSpace = new Space(getActivity());

                leftLine.setLayoutParams(new LinearLayout.LayoutParams(0,1,1));
                rightLine.setLayoutParams(new LinearLayout.LayoutParams(0,1,1));

                leftLine.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                rightLine.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

                dateView.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(item.getDate()));
                dateView.setTextSize(14);
                dateView.setTextColor(getResources().getColor(android.R.color.darker_gray));

                leftSpace.setLayoutParams(new LinearLayout.LayoutParams(10, LinearLayout.LayoutParams.MATCH_PARENT));
                rightSpace.setLayoutParams(new LinearLayout.LayoutParams(10, LinearLayout.LayoutParams.MATCH_PARENT));


                dateLayout.addView(leftLine);
                dateLayout.addView(leftSpace);
                dateLayout.addView(dateView);
                dateLayout.addView(rightSpace);
                dateLayout.addView(rightLine);

                historyView.addView(dateLayout);
            }

            //Add the item
            View topSpace = new Space(getActivity());
            topSpace.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));

            View bottomSpace = new Space(getActivity());
            bottomSpace.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));

            LinearLayout itemLayout = (LinearLayout)item.asView();

            //Make every other layout gray (for visual separation)
            if(i%2 == 1){
                topSpace.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                bottomSpace.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                itemLayout.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }

            historyView.addView(topSpace);
            historyView.addView(itemLayout);
            historyView.addView(bottomSpace);
        }
    }
}