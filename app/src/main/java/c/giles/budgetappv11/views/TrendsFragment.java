package c.giles.budgetappv11.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import c.giles.budgetappv11.BudgetManager;
import c.giles.budgetappv11.HistoryData;
import c.giles.budgetappv11.HistoryManager;
import c.giles.budgetappv11.R;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.view.LineChartView;

import static android.content.Context.MODE_PRIVATE;

public class TrendsFragment extends Fragment {
    LineChartView chart;
    List<HistoryData> historyDataList = new ArrayList<>();
    List<String> xAxisData = new ArrayList<>();
    List<String> yAxisData = new ArrayList<>();
    List<AxisValue> xAxisValues = new ArrayList<>();
    List<AxisValue> yAxisValues = new ArrayList<>();
    Line line;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        chart = view.findViewById(R.id.chart);

        refresh();
        loadChart();

        return view;
    }


    private void loadChart(){

    }

    //Called by HistoryActivity when history is cleared by user
    public void refresh(){
        historyDataList = new ArrayList<>(HistoryManager.getHistoryDataList());
        for(HistoryData data : historyDataList){
            xAxisData.add(data.getTime().toString());
            yAxisData.add(BudgetManager.getTotalFunds().toString());
        }
    }

}