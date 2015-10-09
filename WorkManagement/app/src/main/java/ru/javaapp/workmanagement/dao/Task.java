package ru.javaapp.workmanagement.dao;

/**
 * Created by User on 30.09.2015.
 */
public class Task {

    private int idTask;
    private int idOrganizer;
    private int idPerformer;
    private int idWhat;
    private int idMesto;
    private int quantumTask;
    private int idStatus;
    private String dateStart;
    private String dateFinish;
    private String timeStart;
    private String timeFinish;
    private String commentTask;

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public int getIdOrganizer() {
        return idOrganizer;
    }

    public void setIdOrganizer(int idOrganizer) {
        this.idOrganizer = idOrganizer;
    }

    public int getIdPerformer() {
        return idPerformer;
    }

    public void setIdPerformer(int idPerformer) {
        this.idPerformer = idPerformer;
    }

    public int getIdWhat() {
        return idWhat;
    }

    public void setIdWhat(int idWhat) {
        this.idWhat = idWhat;
    }

    public int getIdMesto() {
        return idMesto;
    }

    public void setIdMesto(int idMesto) {
        this.idMesto = idMesto;
    }

    public int getQuantumTask() {
        return quantumTask;
    }

    public void setQuantumTask(int quantumTask) {
        this.quantumTask = quantumTask;
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
