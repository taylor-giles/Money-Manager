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
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

import static android.content.Context.MODE_PRIVATE;

public class TrendsFragment extends Fragment {
    LineChartView chart;
    List<HistoryData> historyDataList = new ArrayList<>();
    List<String> xAxisData = new ArrayList<>();
    List<Float> yAxisData = new ArrayList<>();
    List<AxisValue> xAxisValues = new ArrayList<>();
    List<PointValue> yAxisValues = new ArrayList<>();
    Line line;
    LineChartData data = new LineChartData();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trends, container, false);
        chart = view.findViewById(R.id.chart);

        refresh();
        loadChart();

        return view;
    }


    private void loadChart(){
        line = new Line(yAxisValues);
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        data.setLines(lines);
        chart.setLineChartData(data);
    }

    //Called by HistoryActivity when history is cleared by user
    public void refresh(){
        historyDataList = new ArrayList<>(HistoryManager.getHistoryDataList());
        for(HistoryData data : historyDataList){
            xAxisData.add(data.getTime().toString());
            yAxisData.add(Float.parseFloat(BudgetManager.getTotalFunds().toString()));
        }
        for(int i = 0; i < xAxisData.size(); i++){
            xAxisValues.add(i, new AxisValue(i).setLabel(xAxisData.get(i)));
        }
        for (int i = 0; i < yAxisData.size(); i++){
            yAxisValues.add(new PointValue(i, yAxisData.get(i)));
        }
    }

}