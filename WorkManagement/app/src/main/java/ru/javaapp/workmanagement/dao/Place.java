package ru.javaapp.workmanagement.dao;

/**
 * Created by User on 30.09.2015.
 */
public class Place {

    private int idPlace;
    private int quantumMade;
    private int quantumPlanned;
    private int quantumCurrent;
    private int idProstoy;
    private String namePlace;

    public int getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(int idPlace) {
        this.idPlace = idPlace;
    }

    public int getQuantumMade() {
        return quantumMade;
    }

    public void setQuantumMade(int quantumMade) {
        this.quantumMade = quantumMade;
    }

    public int getQuantumPlanned() {
        return quantumPlanned;
    }

    public void setQuantumPlanned(int quantumPlanned) {
        this.quantumPlanned = quantumPlanned;
    }

    public int getQuantumCurrent() {
        return quantumCurrent;
    }

    public void setQuantumCurrent(int quantumCurrent) {
        this.quantumCurrent = quantumCurrent;
    }

    public int getIdProstoy() {
        return idProstoy;
    }

    public void setIdProstoy(int idProstoy) {
        this.idProstoy = idProstoy;
    }

    public String getNamePlace() {
        return namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }
}
