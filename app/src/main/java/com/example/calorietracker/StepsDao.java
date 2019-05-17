package com.example.calorietracker;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StepsDao {

    //TODO:possible issues here
    @Query("SELECT * FROM steps")
    List<Steps> getAll();

    @Query("SELECT * FROM steps WHERE steps= :step AND " + "time=:time LIMIT 1")
    Steps findByStepsAndTime(Integer step, String time);

    @Query("SELECT * FROM steps WHERE time=:time LIMIT 1")
    Steps findByTime(String time);

    @Query("SELECT * FROM steps WHERE uid = :stepsId LIMIT 1")
    Steps findByID(int stepsId);

    @Insert
    void insertAll(Steps... steps);

    @Insert
    void insert(Steps steps);

    @Delete
    void deleteUsers(Steps steps);

    @Update(onConflict = REPLACE)
    public void updateUsers(Steps... steps);

    @Query("DELETE FROM steps")
    void deleteAll();
}