package com.example.f1hub.data;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Handles converting the JSON returned by the Ergast f1 API into
 * objects(di/driverInfo) usable by the app.
 */
public class DriverDataParser {
    private String TAG2 = "YEEEEEE";

    /**
     * @param jsonString The JSON String returned by the Ergast API
     * @return A {@link List} of {@link DriverInfo} objects parsed from the jsonString.
     * @throws JSONException  if any error occurs with processing the jsonString
     * @throws ParseException if any error occurs with processing the jsonString
     */

    public List<DriverInfo> convertDriverInfoJson(String jsonString) throws JSONException, ParseException{


        List<DriverInfo> driverInfo = new ArrayList<DriverInfo>();

        // process response to get a list of DriverInfo objects
        try {
            JSONObject rootObject = new JSONObject(jsonString);
            JSONObject MRData = rootObject.getJSONObject("MRData");
            JSONObject standingsObj = MRData.getJSONObject("StandingsTable");
            JSONArray standingsArray = standingsObj.getJSONArray("StandingsLists");

            for (int i = 0, j = standingsArray.length(); i < j; i++) {
                JSONObject standingsArrayObj = standingsArray.getJSONObject(i);
                JSONArray driverStandings = standingsArrayObj.getJSONArray("DriverStandings");
                for (int ii = 0, jj = driverStandings.length(); ii < jj; ii++) {
                    JSONObject dStandingsObj = driverStandings.getJSONObject(ii);
                    String wins = dStandingsObj.getString("wins");
                    String points = dStandingsObj.getString("points");
                    String position = dStandingsObj.getString("position");
                    JSONObject driverObj = dStandingsObj.getJSONObject("Driver");
                    String driverName = driverObj.getString("driverId");

                    Log.d(TAG2, "drivers name are as follows ;" + driverName);
                    DriverInfo di = new DriverInfo();
                    di.setDriverName(driverName);
                    di.setDriverPoints(points);
                    di.setDriverWins(wins);
                    di.setDriverPos(position);
                    driverInfo.add(di);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }
        return driverInfo;


    }
}
