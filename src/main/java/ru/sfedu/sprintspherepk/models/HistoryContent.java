package ru.sfedu.sprintspherepk.models;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Root
public class HistoryContent {

    @Element
    private String id;

    @Element
    private String className;

    @Element
    private Date createdDate;

    @Element
    private String actor;

    @Element
    private String methodName;

    @ElementMap(entry = "item", key = "key", attribute = true, inline = true, required = false)
    private Map<String, String> object;

    @Element
    private Status status;


    public HistoryContent() {
        this.id = UUID.randomUUID().toString();
        this.createdDate = new Date();
    }

    //Геттеры

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getActor() {
        return actor;
    }

    public String getMethodName() {
        return methodName;
    }

    public Map<String, String> getObject() {
        return object;
    }

    public Status getStatus() {
        return status;
    }


    //Сеттеры


    public void setId(String id) {
        this.id = id;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setObject(Map<String, String> object) {
        this.object = object;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}

