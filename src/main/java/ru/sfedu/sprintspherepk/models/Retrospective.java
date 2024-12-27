package ru.sfedu.sprintspherepk.models;


public class Retrospective {
    private int id;
    private String summary;
    private String improvements;
    private String positives;

    // Пустой конструктор
    public Retrospective() {
    }

    public Retrospective(int id, String summary, String improvements, String positives) {
        this.id = id;
        this.summary = summary;
        this.improvements = improvements;
        this.positives = positives;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImprovements() {
        return improvements;
    }

    public void setImprovements(String improvements) {
        this.improvements = improvements;
    }

    public String getPositives() {
        return positives;
    }

    public void setPositives(String positives) {
        this.positives = positives;
    }

    // Метод toString
    @Override
    public String toString() {
        return "Retrospective{" +
                "id=" + id +
                ", summary='" + summary + '\'' +
                ", improvements=" + improvements +
                ", positives=" + positives +
                '}';
    }
}
