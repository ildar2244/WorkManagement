package ru.javaapp.workmanagement.dao;

/**
 * Created by User on 30.09.2015.
 */
public class Downtime {

    private int idDowntime;
    private int quantumDowntime;
    private String nameDowntime;
    private String date, time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIdDowntime() {
        return idDowntime;
    }

    public void setIdDowntime(int idDowntime) {
        this.idDowntime = idDowntime;
    }

    public int getQuantumDowntime() {
        return quantumDowntime;
    }

    public void setQuantumDowntime(int quantumDowntime) {
        this.quantumDowntime = quantumDowntime;
    }

    public String getNameDowntime() {
        return nameDowntime;
    }

    public void setNameDowntime(String nameDowntime) {
        this.nameDowntime = nameDowntime;
    }
}
