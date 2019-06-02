package c.giles.budgetappv11.views;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

import c.giles.budgetappv11.CustomMarkerView;
import c.giles.budgetappv11.HistoryData;
import c.giles.budgetappv11.HistoryManager;
import c.giles.budgetappv11.R;

public class TrendsFragment extends Fragment {

    private LineChart chart;
    private ArrayList<HistoryData> totalDataList = new ArrayList<>();


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
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);
        CustomMarkerView markerView = new CustomMarkerView(getContext(), R.layout.custom_marker_view);
        markerView.setChartView(chart);
        chart.setMarker(markerView);



        XAxis xAxis = chart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setAxisMaximum(10f);
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawLimitLinesBehindData(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(350f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);

        chart.getAxisRight().setEnabled(false);


        //Update values
        ArrayList<Entry> values = new ArrayList<>();
        for(HistoryData data : totalDataList){
            values.add(new Entry(
                    //data.getTime().getTimeInMillis()
                    totalDataList.indexOf(data), Float.parseFloat(data.getAmount().toString())));
        }

        //Make and apply data set
        LineDataSet totalSet;
        if(chart.getData() != null && chart.getData().getDataSetCount() > 0){
            totalSet = (LineDataSet)chart.getData().getDataSetByIndex(0);
            totalSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            totalSet = new LineDataSet(values, "Sample Data");
            totalSet.setDrawIcons(false);
            totalSet.enableDashedLine(10f, 5f, 0f);
            totalSet.enableDashedHighlightLine(10f, 5f, 0f);
            totalSet.setColor(Color.DKGRAY);
            totalSet.setCircleColor(Color.DKGRAY);
            totalSet.setLineWidth(1f);
            totalSet.setCircleRadius(3f);
            totalSet.setDrawCircleHole(false);
            totalSet.setValueTextSize(9f);
            totalSet.setDrawFilled(true);
            totalSet.setFormLineWidth(1f);
            totalSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            totalSet.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), android.R.color.holo_blue_dark);
                totalSet.setFillDrawable(drawable);
            } else {
                totalSet.setFillColor(Color.DKGRAY);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(totalSet);
            LineData data = new LineData(dataSets);
            chart.setData(data);
        }
    }

    //Called by HistoryActivity when history is cleared by user
    public void refresh(){
        totalDataList = new ArrayList<>(HistoryManager.getTotalDataList());
    }

}