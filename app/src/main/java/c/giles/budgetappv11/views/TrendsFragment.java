package c.giles.budgetappv11.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import c.giles.budgetappv11.Budget;
import c.giles.budgetappv11.HistoryData;
import c.giles.budgetappv11.HistoryManager;
import c.giles.budgetappv11.R;

public class TrendsFragment extends Fragment {

    private LineChart chart;
    private ArrayList<HistoryData> totalDataList = new ArrayList<>();
    private List<HistoryData> dataList;
    private ArrayList<LineDataSet> dataSets = new ArrayList<>();

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

        //Make HistoryData list
        for(HistoryData data : HistoryManager.getHistoryDataList()){
            if(!data.isPaycheck()){
                dataList.add(data);
            }
        }

        //Edit chart behavior
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);

        //Edit chart appearance
        chart.setDescription(null);
        chart.setNoDataText("Nothing to see here... yet!");
        chart.setNoDataTextColor(Color.DKGRAY);
        chart.setDrawBorders(true);
        CustomMarkerView markerView = new CustomMarkerView(getContext(), R.layout.custom_marker_view);
        markerView.setChartView(chart);
        chart.setMarker(markerView);

        //Set legend appearance
        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.DKGRAY);
        legend.setTextSize(12);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(20);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setXEntrySpace(20);


        //Update total budget values and entries
        ArrayList<Entry> totalEntries = new ArrayList<>();
        ArrayList<HistoryData> totalValues = new ArrayList<>();

        if(!totalDataList.isEmpty()){
            totalValues.add(totalDataList.get(0));
            for (HistoryData data : totalDataList) {
                //Only make a point on the chart for the final total from each day
                if (data.getTime().get(Calendar.YEAR) == totalValues.get(totalValues.size()-1).getTime().get(Calendar.YEAR) &&
                        data.getTime().get(Calendar.MONTH) == totalValues.get(totalValues.size()-1).getTime().get(Calendar.MONTH) &&
                        data.getTime().get(Calendar.DAY_OF_MONTH) == totalValues.get(totalValues.size()-1).getTime().get(Calendar.DAY_OF_MONTH)) {

                    totalValues.remove(totalValues.size()-1);
                }
                totalValues.add(data);
            }

            for(HistoryData data : totalValues) {
                totalEntries.add(new Entry(data.getTime().getTimeInMillis(), Float.parseFloat(data.getAmount().toString())));
            }
        }


        //Update all other budget values and entries
        Map<Budget, ArrayList<Entry>> entryMap = new HashMap<>();
        Map<Budget, ArrayList<HistoryData>> valueMap = new HashMap<>();
        ArrayList<HistoryData> currentList = new ArrayList<>();

        if(!dataList.isEmpty()) {
            for (HistoryData data : dataList) {
                //If the current budget does not already have a list associated with it, make one.
                if (entryMap.get(data.getBudget()) == null) {
                    entryMap.put(data.getBudget(), new ArrayList<Entry>());
                }
                if (valueMap.get(data.getBudget()) == null) {
                    valueMap.put(data.getBudget(), new ArrayList<HistoryData>());
                    Objects.requireNonNull(valueMap.get(data.getBudget())).add(data);
                }
                currentList = Objects.requireNonNull(valueMap.get(data.getBudget()));

                //Only add the last value from each day
                if (data.getTime().get(Calendar.YEAR) == currentList.get(currentList.size()-1).getTime().get(Calendar.YEAR) &&
                        data.getTime().get(Calendar.MONTH) == currentList.get(currentList.size()-1).getTime().get(Calendar.MONTH) &&
                        data.getTime().get(Calendar.DAY_OF_MONTH) == currentList.get(currentList.size()-1).getTime().get(Calendar.DAY_OF_MONTH)) {

                    currentList.remove(currentList.size()-1);
                }
                currentList.add(data);
            }

            //Put the values into entries
            for (Budget key : valueMap.keySet()){
                for(HistoryData data : Objects.requireNonNull(valueMap.get(key))){
                    try {
                        Objects.requireNonNull(entryMap.get(key)).add(new Entry(data.getTime().getTimeInMillis(), Float.parseFloat(data.getTotal().toString())));
                    }catch(NullPointerException e){
                        Log.d("nullpointer1", data.isPaycheck() + " , " + data.isFromPaycheck());
                    }
                }
            }
        }


        //Set and edit x-axis
        XAxis xAxis = chart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        //Find max/min x values
        float xMax = Float.MIN_VALUE;
        float xMin = Float.MAX_VALUE;
        for(HistoryData data : totalValues){
            if(data.getTime().getTimeInMillis() > xMax){
                xMax = data.getTime().getTimeInMillis();
            }
            if(data.getTime().getTimeInMillis() < xMin){
                xMin = data.getTime().getTimeInMillis();
            }
        }
        xAxis.setAxisMaximum(xMax + TimeUnit.DAYS.toMillis(1));
        xAxis.setAxisMinimum(xMin - TimeUnit.DAYS.toMillis(1));
        xAxis.setDrawLimitLinesBehindData(true);
        xAxis.setValueFormatter(new XAxisValueFormatter());

        //Set and edit y-axis
        YAxis yAxis = chart.getAxisLeft();
        yAxis.removeAllLimitLines();

        //Find max/min y values
        float yMax = Float.MIN_VALUE;
        float yMin = Float.MAX_VALUE;
        for(HistoryData data : totalValues){
            if(data.getAmount() > yMax){
                yMax = Float.parseFloat(data.getAmount().toString());
            }
            if(data.getAmount() < yMin){
                yMin = Float.parseFloat(data.getAmount().toString());
            }
        }
        yAxis.setAxisMaximum(yMax + 50);
        //yAxis.setAxisMinimum(yMin - 50);
        yAxis.setAxisMinimum(0);
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setDrawZeroLine(false);
        yAxis.setDrawLimitLinesBehindData(false);

        chart.getAxisLeft().setEnabled(true);
        chart.getAxisRight().setEnabled(false);



        //Make and apply total data set
        LineDataSet totalSet;
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        if(chart.getData() != null && chart.getData().getDataSetCount() > 0){
            totalSet = (LineDataSet)chart.getData().getDataSetByIndex(0);
            totalSet.setValues(totalEntries);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            totalSet = new LineDataSet(totalEntries, "Total Funds");
            totalSet.setDrawIcons(false);
            totalSet.setColor(Color.BLUE);
            totalSet.setCircleColor(Color.BLUE);
            totalSet.setLineWidth(2f);
            totalSet.setCircleRadius(3f);
            totalSet.setDrawCircleHole(false);
            totalSet.setValueTextSize(9f);
            totalSet.setDrawFilled(false);
            totalSet.setFormLineWidth(1f);
            totalSet.setFormSize(15.f);


            dataSets.add(totalSet);
            LineData data = new LineData(dataSets);
            data.setValueFormatter(new CurrencyValueFormatter());
            chart.setData(data);
        }


        //TODO: This needs to be finished. Figure out how to reference the data set for each particular budget
        //Make and apply all other data sets
        //This map tells the index of the corresponding LineDataSet within the chart
        Map<Budget, Integer> indexMap = new HashMap<>();

        //Find the highest current index
        int maxIndex = 0;
        for(Integer index : indexMap.values()){
            if(index > maxIndex){
                maxIndex = index;
            }
        }

        //Assign an index to any budgets that don't currently have one
        for(Budget budget : entryMap.keySet()){
            if(!indexMap.containsKey(budget)){
                indexMap.put(budget, maxIndex + 1);
                maxIndex++;
            }
        }

        //Apply all other data sets
        List<Budget> sortedKeys = new ArrayList<>();
        List<Integer> temp = new ArrayList<>(indexMap.values());
        Collections.sort(temp);

        //Flip the map around so you can access budgets given their index
        Map<Integer, Budget> swappedMap = indexMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        //Make a sorted list of budgets, in order of index
        for(Integer index : temp){
            sortedKeys.add(swappedMap.get(index));
        }
        for(Budget budget : sortedKeys){
            LineDataSet dataSet;
            if(chart.getData() != null && chart.getData().getDataSetCount() > indexMap.get(budget)) {
                dataSet = (LineDataSet) chart.getData().getDataSetByIndex(indexMap.get(budget));
                dataSet.setValues(entryMap.get(budget));
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                dataSet = new LineDataSet(entryMap.get(budget), budget.getBudgetName());
                dataSet.setDrawIcons(false);
                try {
                    dataSet.setColor(
                            budget
                                    .getColor());
                    dataSet.setCircleColor(budget.getColor());
                }catch(NullPointerException e){
                    Log.d("nullpointer2", budget.getBudgetName());
                }
                dataSet.setLineWidth(2f);
                dataSet.setCircleRadius(3f);
                dataSet.setDrawCircleHole(false);
                dataSet.setValueTextSize(9f);
                dataSet.setDrawFilled(false);
                dataSet.setFormLineWidth(1f);
                dataSet.setFormSize(15.f);

            //Add this data set to the list of all data sets
            dataSets.add(dataSet);

        }
            LineData data = new LineData(dataSets);
            data.setValueFormatter(new CurrencyValueFormatter());
            chart.setData(data);
        }

        chart.invalidate();
    }

    //Called by HistoryActivity when history is cleared by user
    public void refresh(){
        totalDataList = new ArrayList<>(HistoryManager.getTotalDataList());
        dataList = new ArrayList<>(HistoryManager.getHistoryDataList());
        chart.clear();
    }






    private class CurrencyValueFormatter extends ValueFormatter{
        private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        public CurrencyValueFormatter() {
            super();
        }

        @Override
        public String getFormattedValue(float value) {
            return currencyFormat.format(value);
        }
    }


    private class XAxisValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float timeInMillis) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(((long)timeInMillis));
            return (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        }
    }
}