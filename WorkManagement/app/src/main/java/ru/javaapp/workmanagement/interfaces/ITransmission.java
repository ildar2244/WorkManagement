package ru.javaapp.workmanagement.interfaces;

import android.content.Context;

/**
 * Created by User on 27.10.2015.
 */
public interface ITransmission {

    void CreateTask(int masterId, int workerId, int whatId, int whereId, String countPlan,
                           String commentEdit, String timeBefore,
                           String timeAfter, String dateBefore, String dateAfter, Context context);

    void UpdateTaskStatus(int taskId, int statusId, Context context);

    void UpdateCountAndStatus(int taskId, int currentCount, int statusId, Context context);

    void UpdateCurrentCount(int taskId, int currentCount, Context context);

    void UpdateDefect(int taskId, int defectId, int defectCount, Context context, String date, String time);

    void UpdateDownTime(int taskId, int stopId, Context context, String date, String time);

    String DoAuthorize(String login, String password, String role);
}
