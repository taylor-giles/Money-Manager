package c.giles.budgetappv11.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import c.giles.budgetappv11.HistoryData;
import c.giles.budgetappv11.HistoryManager;
import c.giles.budgetappv11.R;

public class TrendsFragment extends Fragment {

    private LineChart chart;
    private ArrayList<HistoryData> totalDataList = new ArrayList<>();
    private List<HistoryData> dataList = new ArrayList<>(HistoryManager.getHistoryDataList());
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
        Map<String, ArrayList<Entry>> entryMap = new HashMap<>();
        Map<String, ArrayList<HistoryData>> valueMap = new HashMap<>();
        ArrayList<HistoryData> currentList = new ArrayList<>();

        if(!dataList.isEmpty()) {
            for (HistoryData data : dataList) {
                //If the current budget does not already have a list associated with it, make one.
                if (entryMap.get(data.getBudget().getBudgetName()) == null) {
                    entryMap.put(data.getBudget().getBudgetName(), new ArrayList<Entry>());
                }
                if (valueMap.get(data.getBudget().getBudgetName()) == null) {
                    valueMap.put(data.getBudget().getBudgetName(), new ArrayList<HistoryData>());
                    Objects.requireNonNull(valueMap.get(data.getBudget().getBudgetName())).add(data);
                }
                currentList = Objects.requireNonNull(valueMap.get(data.getBudget().getBudgetName()));

                //Only add the last value from each day
                if (data.getTime().get(Calendar.YEAR) == currentList.get(currentList.size()-1).getTime().get(Calendar.YEAR) &&
                        data.getTime().get(Calendar.MONTH) == currentList.get(currentList.size()-1).getTime().get(Calendar.MONTH) &&
                        data.getTime().get(Calendar.DAY_OF_MONTH) == currentList.get(currentList.size()-1).getTime().get(Calendar.DAY_OF_MONTH)) {

                    currentList.remove(currentList.size()-1);
                }
                currentList.add(data);
            }

            //Put the values into entries
            for (String key : valueMap.keySet()){
                for(HistoryData data : Objects.requireNonNull(valueMap.get(key))){
                    Objects.requireNonNull(entryMap.get(key)).add(new Entry(data.getTime().getTimeInMillis(), Float.parseFloat(data.getAmount().toString())));
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
        yAxis.setAxisMinimum(yMin - 50);
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setDrawZeroLine(false);
        yAxis.setDrawLimitLinesBehindData(false);

        chart.getAxisLeft().setEnabled(true);
        chart.getAxisRight().setEnabled(false);



        //Make and apply total data set
        LineDataSet totalSet;
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


            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(totalSet);
            LineData data = new LineData(dataSets);
            data.setValueFormatter(new CurrencyValueFormatter());
            chart.setData(data);
        }


        //TODO: This needs to be finished. Figure out how to reference the data set for each particular budget
        //Make and apply all other data sets
        ArrayList<LineDataSet> dataSets = new ArrayList<>();

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