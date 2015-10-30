package ru.javaapp.workmanagement.dao;

/**
 * Created by User on 30.09.2015.
 */
public class Defect {

    private int idDefect;
    private int quantumDefect;
    private String nameDefect;
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

    public int getIdDefect() {
        return idDefect;
    }

    public void setIdDefect(int idDefect) {
        this.idDefect = idDefect;
    }

    public int getQuantumDefect() {
        return quantumDefect;
    }

    public void setQuantumDefect(int quantumDefect) {
        this.quantumDefect = quantumDefect;
    }

    public String getNameDefect() {
        return nameDefect;
    }

    public void setNameDefect(String nameDefect) {
        this.nameDefect = nameDefect;
    }
}
