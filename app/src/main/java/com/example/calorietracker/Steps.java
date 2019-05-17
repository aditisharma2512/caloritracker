package com.example.calorietracker;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Steps {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "steps")
    public Integer steps;

    @ColumnInfo(name = "time")
    public String time;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Steps(Integer steps, String time) {
        this.steps = steps;
        this.time = time;
    }
}