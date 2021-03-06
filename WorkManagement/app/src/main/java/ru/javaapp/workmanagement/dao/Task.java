package ru.javaapp.workmanagement.dao;

import java.io.Serializable;

/**
 * Created by User on 30.09.2015.
 */
public class Task implements Serializable {

    private int idTask;
    private String masterName;
    private String Performer;
    private String whatName;
    private String placeName;
    private int countPlanTask;
    private int countCurrentTask;
    private int idStatus;
    private String dateStart;
    private String dateFinish;
    private String timeStart;
    private String timeFinish;
    private String commentTask;
    private String downtimeName;

    public String getDowntimeName() {
        return downtimeName;
    }

    public void setDowntimeName(String downtimeName) {
        this.downtimeName = downtimeName;
    }

    public String getDefectName() {
        return defectName;
    }

    public void setDefectName(String defectName) {
        this.defectName = defectName;
    }

    public int getDefectCount() {
        return defectCount;
    }

    public void setDefectCount(int defectCount) {
        this.defectCount = defectCount;
    }

    private String defectName;
    private int defectCount;

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getPerformer() {
        return Performer;
    }

    public void setIdPerformer(String Performer) {
        this.Performer = Performer;
    }

    public String getWhatName() {
        return whatName;
    }

    public void setWhatName(String whatName) {
        this.whatName = whatName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public int getCountPlanTask() {
        return countPlanTask;
    }

    public void setCountPlanTask(int countPlanTask) {
        this.countPlanTask = countPlanTask;
    }

    public int getCountCurrentTask() {
        return countCurrentTask;
    }

    public void setCountCurrentTask(int countCurrentTask) {
        this.countCurrentTask = countCurrentTask;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(String dateFinish) {
        this.dateFinish = dateFinish;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(String timeFinish) {
        this.timeFinish = timeFinish;
    }

    public String getCommentTask() {
        return commentTask;
    }

    public void setCommentTask(String commentTask) {
        this.commentTask = commentTask;
    }
}
