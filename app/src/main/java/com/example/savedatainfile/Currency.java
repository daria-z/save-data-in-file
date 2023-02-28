package com.example.savedatainfile;

import java.time.LocalDate;

public class Currency {
    private String name;
    private float rate;

    private LocalDate updateDate;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }


    public LocalDate getDate() {
        return updateDate;
    }

    public void setDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
}

// добавить дату для каждого объекта