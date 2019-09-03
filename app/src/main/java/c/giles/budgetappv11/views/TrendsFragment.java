package c.giles.budgetappv11.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

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
import java.util.concurrent.TimeUnit;

import c.giles.budgetappv11.Budget;
import c.giles.budgetappv11.BudgetManager;
import c.giles.budgetappv11.HistoryData;
import c.giles.budgetappv11.HistoryManager;
import c.giles.budgetappv11.R;

public class TrendsFragment extends Fragment {

    private LineChart chart;
    private LinearLayout checkBoxLayout;
    private View view;
    private ArrayList<HistoryData> totalDataList = new ArrayList<>();
    private ArrayList<HistoryData> historyList = new ArrayList<>();
    private ArrayList<LineDataSet> dataSets = new ArrayList<>();
    private ArrayList<Budget> budgetList = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxList = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trends, container, false);
        chart = view.findViewById(R.id.chart);
        checkBoxLayout = view.findViewById(R.id.chart_checkbox_layout);

        refresh();
        loadChart();

        return view;
    }


    private void loadChart() {
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

        ArrayList<ArrayList<Entry>> allEntries = new ArrayList<>();
        ArrayList<Budget> entryBudgets = new ArrayList<>();

        //Update values
        for (Budget budget : budgetList){
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<HistoryData> values = new ArrayList<>();
            ArrayList<HistoryData> currentList = new ArrayList<>();

            for(HistoryData data : historyList){
                if(data.getBudget().equals(budget)) {
                    currentList.add(data);
                }
            }

            if (!currentList.isEmpty()) {
                values.add(currentList.get(0));
                for (HistoryData data : currentList) {
                    //Only make a point on the chart for the final total from each day
                    if (data.getTime().get(Calendar.YEAR) == values.get(values.size() - 1).getTime().get(Calendar.YEAR) &&
                            data.getTime().get(Calendar.MONTH) == values.get(values.size() - 1).getTime().get(Calendar.MONTH) &&
                            data.getTime().get(Calendar.DAY_OF_MONTH) == values.get(values.size() - 1).getTime().get(Calendar.DAY_OF_MONTH)) {

                        values.remove(values.size() - 1);
                    }
                    values.add(data);
                }

                for (HistoryData data : values) {
                    entries.add(new Entry(data.getTime().getTimeInMillis(), Float.parseFloat(data.getAmount().toString())));
                }
                allEntries.add(entries);
                entryBudgets.add(budget);
                CheckBox checkBox = new CheckBox(view.getContext());
                checkBox.setText(budget.getBudgetName());
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                });
                checkBoxList.add(checkBox);
                checkBoxLayout.addView(checkBox);
            }
        }


        //Set and edit x-axis
        XAxis xAxis = chart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        //Find max/min x values
        float xMax = Float.MIN_VALUE;
        float xMin = Float.MAX_VALUE;
        for(HistoryData data : historyList){
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
//        xAxis.setLabelCount(2 + (int)((values.get(values.size() - 1).getTime().getTimeInMillis() - values.get(0).getTime().getTimeInMillis()) / TimeUnit.DAYS.toMillis(1)), true);

        //Set and edit y-axis
        YAxis yAxis = chart.getAxisLeft();
        yAxis.removeAllLimitLines();

        //Find max/min y values
        float yMax = Float.MIN_VALUE;
        float yMin = Float.MAX_VALUE;
        for(HistoryData data : historyList){
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


        for(ArrayList<Entry> entries : allEntries) {
            for(CheckBox checkBox : checkBoxList){
                if(checkBox.isChecked()) {
                    if (budgetList.get(allEntries.indexOf(entries)).getBudgetName().equals(checkBox.getText().toString())) {
                        //Make and apply data set
                        LineDataSet dataSet;
                        if (chart.getData() != null && chart.getData().getDataSetByIndex(allEntries.indexOf(entries)) != null) {
                            dataSet = (LineDataSet) chart.getData().getDataSetByIndex(allEntries.indexOf(entries));
                            dataSet.setValues(entries);
                            chart.getData().notifyDataChanged();
                            chart.notifyDataSetChanged();
                        } else {
                            dataSet = new LineDataSet(entries, budgetList.get(allEntries.indexOf(entries)).getBudgetName());
                            dataSet.setDrawIcons(false);
                            dataSet.setColor(Color.BLUE);
                            dataSet.setCircleColor(Color.BLUE);
                            dataSet.setLineWidth(2f);
                            dataSet.setCircleRadius(3f);
                            dataSet.setDrawCircleHole(false);
                            dataSet.setValueTextSize(9f);
                            dataSet.setDrawFilled(false);
                            dataSet.setFormLineWidth(1f);
                            dataSet.setFormSize(15.f);


                            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                            dataSets.add(dataSet);
                            LineData data = new LineData(dataSets);
                            data.setValueFormatter(new CurrencyValueFormatter());
                            chart.setData(data);
                        }
                    }
                }
            }
        }
        chart.invalidate();
    }

    //Called by HistoryActivity when history is cleared by user
    public void refresh(){
        totalDataList = new ArrayList<>(HistoryManager.getTotalDataList());
        historyList = new ArrayList<>(HistoryManager.getHistoryDataList());
        budgetList = new ArrayList<>(BudgetManager.getBudgetList());
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

