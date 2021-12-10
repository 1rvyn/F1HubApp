package com.example.f1hub.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DriverInfo")
public class DriverInfo {
    // http://ergast.com/api/f1/2020/driverStandings.json
    private String driverName;

    private String driverPos;

    private String driverPoints;

    private String driverWins;

    private String dataYear;

    //private int season;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int uid;

    /**
     * default constructor
     */

    public DriverInfo(){
        super();
    }



    public String getDriverName(){
        return driverName;
    }

    public void setDriverName(String driverName){
        this.driverName = driverName;
    }


    public String getDriverPos(){
        return driverPos;
    }

    public void setDriverPos(String driverPos){ this.driverPos = driverPos; }


    public String getDriverPoints(){
        return driverPoints;
    }

    public void setDriverPoints(String driverPoints){
        this.driverPoints = driverPoints;
    }


    public String getDriverWins(){
        return driverWins;
    }

    public void setDriverWins(String driverWins){
        this.driverWins = driverWins;
    }


    public String getDataYear() { return dataYear;}

    public void setDataYear (String dataYear) { this.dataYear = dataYear;}

    //public int getSeason(){ return season; }

    //public void setSeason(int season){ this.season = season;}

    @Override
    public String toString(){
        return "DriverInfo{" +
                "DriverName=" + driverName +
                ", DriverPosition" + driverPos +
                ", DriverPoints" + driverPoints +
                ", DriverWins" + driverWins +
                "}";
    }
}
