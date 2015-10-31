package ru.javaapp.workmanagement;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;

import ru.javaapp.workmanagement.workDB.Transmission;

/**
 * Created by User on 27.10.2015.
 */
public class Helper {

    // Метод проверки подключения интернета
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            return cm.getActiveNetworkInfo().isConnected();
        }
        return false;
    }

    // Метод замены даты
    public static String parseDate(String date){
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);

        String newDate = day + "/" + month + "/" + year;
        return newDate;
    }

    // Получение текущей даты
    public static String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        return date;
    }

    // Получение текущего времени
    public static String getCurrentTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = dateFormat.format(new Date(System.currentTimeMillis()));
        return time;
    }

}
