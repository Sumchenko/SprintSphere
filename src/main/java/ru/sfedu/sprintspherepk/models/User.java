package ru.sfedu.sprintspherepk.models;

import java.util.Date;

public class User {
    private int id;
    private String name;
    private String email;
    private String bio;
    private int countProject;
    private String avatarURL;
    private boolean isActive;
    private Date lastLogin;
    private Date dateJoined;

    // Пустой конструктор
    public User() {
    }

    // Конструктор с параметрами
    public User(int id, String name, String email, String bio, int countProject,
                String avatarURL, boolean isActive, Date lastLogin, Date dateJoined) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.countProject = countProject;
        this.avatarURL = avatarURL;
        this.isActive = isActive;
        this.lastLogin = lastLogin;
        this.dateJoined = dateJoined;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getCountProject() {
        return countProject;
    }

    public void setCountProject(int countProject) {
        this.countProject = countProject;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    // Метод toString
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", bio='" + bio + '\'' +
                ", countProject=" + countProject +
                ", avatarURL='" + avatarURL + '\'' +
                ", isActive=" + isActive +
                ", lastLogin=" + lastLogin +
                ", dateJoined=" + dateJoined +
                '}';
    }
}
