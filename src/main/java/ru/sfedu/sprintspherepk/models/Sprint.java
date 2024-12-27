package ru.sfedu.sprintspherepk.models;

import java.util.Date;

public class Sprint {
    private int id;
    private Date startDate;
    private Date endDate;
    private double progress;

    // Пустой конструктор
    public Sprint() {
    }

    // Конструктор с параметрами
    public Sprint(int id, Date startDate, Date endDate, double progress) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.progress = progress;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    // Метод toString
    @Override
    public String toString() {
        return "Sprint{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", progress=" + progress +
                '}';
    }
}
