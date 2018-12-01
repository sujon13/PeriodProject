package com.example.sujon4002.periodproject.create_data;

import java.io.Serializable;

public class PeriodData implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    private long id;
    private String startDate;
    private String endDate;
    private String description;

    public PeriodData(long id, String startDate, String endDate, String description) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public PeriodData() {

    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}