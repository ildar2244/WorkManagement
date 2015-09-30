package javaapp.ru.workmanagement.dao;

/**
 * Created by User on 30.09.2015.
 */
public class What {

    private int idWhat;
    private int quantumMade;
    private int quantumPlanned;
    private int quantumCurrent;
    private int idBrak;
    private String nameWhat;

    public int getIdWhat() {
        return idWhat;
    }

    public void setIdWhat(int idWhat) {
        this.idWhat = idWhat;
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

    public int getIdBrak() {
        return idBrak;
    }

    public void setIdBrak(int idBrak) {
        this.idBrak = idBrak;
    }

    public String getNameWhat() {
        return nameWhat;
    }

    public void setNameWhat(String nameWhat) {
        this.nameWhat = nameWhat;
    }
}
