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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import c.giles.budgetappv11.HistoryData;
import c.giles.budgetappv11.HistoryManager;
import c.giles.budgetappv11.R;

public class TrendsFragment extends Fragment {

    private LineChart chart;
    private ArrayList<HistoryData> totalDataList = new ArrayList<>();
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


        //Update values
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<HistoryData> values = new ArrayList<>();

        if(!totalDataList.isEmpty()){
            values.add(totalDataList.get(0));
            for (HistoryData data : totalDataList) {
                //Only make a point on the chart for the final total from each day
                if (data.getTime().get(Calendar.YEAR) == values.get(values.size()-1).getTime().get(Calendar.YEAR) &&
                        data.getTime().get(Calendar.MONTH) == values.get(values.size()-1).getTime().get(Calendar.MONTH) &&
                        data.getTime().get(Calendar.DAY_OF_MONTH) == values.get(values.size()-1).getTime().get(Calendar.DAY_OF_MONTH)) {

                    values.remove(values.size()-1);
                }
                values.add(data);
            }

            for(HistoryData data : values) {
                entries.add(new Entry(data.getTime().getTimeInMillis(), Float.parseFloat(data.getAmount().toString())));
            }
        }


        //Set and edit x-axis
        XAxis xAxis = chart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        //Find max/min x values
        float xMax = Float.MIN_VALUE;
        float xMin = Float.MAX_VALUE;
        for(HistoryData data : values){
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
        for(HistoryData data : values){
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




        //Make and apply data set
        LineDataSet totalSet;
        if(chart.getData() != null && chart.getData().getDataSetCount() > 0){
            totalSet = (LineDataSet)chart.getData().getDataSetByIndex(0);
            totalSet.setValues(entries);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            totalSet = new LineDataSet(entries, "Total Funds");
            totalSet.setDrawIcons(false);
//            totalSet.enableDashedLine(10f, 5f, 0f);
//            totalSet.enableDashedHighlightLine(10f, 5f, 0f);
            totalSet.setColor(Color.BLUE);
            totalSet.setCircleColor(Color.BLUE);
            totalSet.setLineWidth(2f);
            totalSet.setCircleRadius(3f);
            totalSet.setDrawCircleHole(false);
            totalSet.setValueTextSize(9f);
//            totalSet.setDrawFilled(true);
            totalSet.setDrawFilled(false);
            totalSet.setFormLineWidth(1f);
//            totalSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            totalSet.setFormSize(15.f);
//
//            if (Utils.getSDKInt() >= 18) {
//                Drawable drawable = ContextCompat.getDrawable(getContext(), android.R.color.holo_blue_dark);
//                totalSet.setFillDrawable(drawable);
//            } else {
//                totalSet.setFillColor(Color.BLUE);
//            }


            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(totalSet);
            LineData data = new LineData(dataSets);
            data.setValueFormatter(new CurrencyValueFormatter());
            chart.setData(data);
        }
        chart.invalidate();
    }

    //Called by HistoryActivity when history is cleared by user
    public void refresh(){
        totalDataList = new ArrayList<>(HistoryManager.getTotalDataList());
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

