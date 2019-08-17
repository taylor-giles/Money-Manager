package c.giles.budgetappv11.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import c.giles.budgetappv11.HistoryData;
import c.giles.budgetappv11.HistoryItem;
import c.giles.budgetappv11.HistoryManager;
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

        HistoryManager.setHistoryDeleted(false);
        refresh();

        return view;
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



                LinearLayout dateLayout = new LinearLayout(item.asView().getContext());
                View leftLine = new View(getActivity());
                View rightLine = new View(getActivity());
                TextView dateView = new TextView(getActivity());
                Space leftSpace = new Space(getActivity());
                Space rightSpace = new Space(getActivity());

                leftLine.setLayoutParams(new LinearLayout.LayoutParams(0,2,1));
                rightLine.setLayoutParams(new LinearLayout.LayoutParams(0,2,1));

                leftLine.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.darker_gray));
                rightLine.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.darker_gray));

                dateView.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(item.getCalendar().getTime()));
                dateView.setTextSize(14);
                dateView.setTextColor(ContextCompat.getColor(getActivity(),android.R.color.darker_gray));

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

            LinearLayout itemLayout;

            //Add the item
            View topSpace = new View(getActivity());
            topSpace.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 25));

            View bottomSpace = new View(getActivity());
            bottomSpace.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 25));

            View divider = new View(getActivity());
            divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
            divider.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.darker_gray));

            Space lastItemOfDaySpace = new Space(getActivity());
            lastItemOfDaySpace.setLayoutParams(new LinearLayout.LayoutParams(1,75));

            itemLayout = (LinearLayout)item.asView();

            //Make every other layout gray (for visual separation)
//            if(i%2 == 1){
//                topSpace.setBackgroundColor(getResources().getColor(R.color.light_gray));
//                bottomSpace.setBackgroundColor(getResources().getColor(R.color.light_gray));
//                itemLayout.setBackgroundColor(getResources().getColor(R.color.light_gray));
//            }

            historyView.addView(topSpace);
            historyView.addView(itemLayout);
            historyView.addView(bottomSpace);
            //Check if this item is the last item from its day
            if(!(i >= historyDataList.size() -1)){
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

    //Called by HistoryActivity when history is cleared by user
    public void refresh(){
        historyDataList = new ArrayList<>(HistoryManager.getHistoryDataList());
        historyList = new ArrayList<>();
        for (int i = historyDataList.size() - 1; i >= 0; i--) {
            HistoryData data = historyDataList.get(i);

            if(!data.isPaycheck() && !data.isFromPaycheck()) {
                historyList.add(new HistoryItem(getActivity(), data));
            } else if(data.isFromPaycheck()){
                List<HistoryData> paycheckSublist = new ArrayList<>();
                while(!historyDataList.get(i).isPaycheck()){
                    paycheckSublist.add(historyDataList.get(i));
                    i--;
                }

                historyList.add(new HistoryItem(getActivity(), historyDataList.get(i)));
                for(int k = paycheckSublist.size()-1; k >= 0; k--){
                    historyList.add(new HistoryItem(getActivity(), paycheckSublist.get(k)));
                }
            }
        }

        historyView.removeAllViews();
        loadHistory();
    }
}