package com.example.f1hub.data;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.content.Context;

import androidx.lifecycle.LiveData;

public class DriverInfoRepo {

    private static DriverInfoRepo INSTANCE;

    private Context context;

    private static DriverInfoDAO driverInfoDAO;

    private static LiveData<List<DriverInfo>> driverInfo;

    private DriverInfoRepo(Context context) {
        super();
        this.context = context;
        driverInfoDAO = DriverDatabase.getDatabase(context).driverInfoDAO();
        driverInfo = null;
    }


    public static DriverInfoRepo getRepository(Context context){
        if (INSTANCE == null){
            synchronized (DriverInfoRepo.class){
                if(INSTANCE == null)
                    INSTANCE = new DriverInfoRepo(context);
                INSTANCE.context = context;
            }
        }
        return INSTANCE;
    }

    public DriverInfo getDriverInfoFor(String dataYear) {
        return (DriverInfo) getDriverInformation(String.valueOf(dataYear));
    }

    public static LiveData<List<DriverInfo>> getDriverInfoFromDB(int dataYear) {
        driverInfo = driverInfoDAO.findByYear(dataYear);
        return driverInfo;
    }

    public void storeInDatabase(List<DriverInfo> driverInfo) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // insert forecasts into the database
                driverInfoDAO.insert(driverInfo);
            }
        });
    }


    public DriverInfo getDriverInformation(String s){
        // create a new obj
        DriverInfo di = new DriverInfo();

        // random number to populate for now
        Random random = new Random();
        int id = random.nextInt(1000);

        String name = "Lewis hammy";
        int points = 1;


        di.setDriverName(name + id);
        di.setDriverPoints(id + " Points");
        di.setDriverWins(id + " Wins");
        di.setDriverPos(id + " Place");

        return di;
    }


}
