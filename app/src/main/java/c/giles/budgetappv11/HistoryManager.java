package c.giles.budgetappv11;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HistoryManager {
    private static boolean historyDeleted = false;
    private static List<HistoryData> historyDataList = new ArrayList<>();
    private static List<HistoryData> totalDataList = new ArrayList<>();

    public static boolean isHistoryDeleted(){
        return historyDeleted;
    }

    public static void setHistoryDeleted(boolean deleted){
        historyDeleted = deleted;
    }

    public static void setHistoryDataList(List<HistoryData> dataList){
        historyDataList = new ArrayList<>(dataList);
    }

    public static void setTotalDataList(List<HistoryData> totalList){
        totalDataList = new ArrayList<>(totalList);
    }

    public static List<HistoryData> getTotalDataList(){
        return new ArrayList<>(totalDataList);
    }

    public static List<HistoryData> getHistoryDataList(){
        return new ArrayList<>(historyDataList);
    }
}
