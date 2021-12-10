package com.example.f1hub.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object for {@link DriverInfo} entities.
 */
@Dao
public interface DriverInfoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(DriverInfo driverInfo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(List<DriverInfo> driverInfo);

    @Delete
    public void delete(DriverInfo driverInfo);

    @Delete
    public void delete(List<DriverInfo> driverInfo);

    @Query("SELECT * FROM DriverInfo WHERE dataYear = :dataYear")
    public LiveData<List<DriverInfo>> findByYear(int dataYear);



}
