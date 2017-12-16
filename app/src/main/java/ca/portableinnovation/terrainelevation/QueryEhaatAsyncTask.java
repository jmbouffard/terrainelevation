package ca.portableinnovation.terrainelevation;

import android.os.AsyncTask;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.List;

class QueryEhaatAsyncTask extends AsyncTask<Double, Void, Integer> {

    private static final String sDebugTag = "TE.QueryProfAsyncTask";
    private QueryAsyncTaskListener mElevationActivityListener;
    List<Double> mListHaat;
    private double mStartLat = 0;
    private double mStartLon = 0;
    private double mStartDist_m = 0;
    private double mEndDist_m = 0;
    private int mNbrProfiles = 0;

    QueryEhaatAsyncTask(QueryAsyncTaskListener listener, int nbrprofiles) {
        mElevationActivityListener = listener;
        mNbrProfiles = nbrprofiles;
    }

    @Override
    protected void onPreExecute() {
        mElevationActivityListener.setProgressBarVisible(true);
    }

    @Override
    protected Integer doInBackground(Double... coord) {
        mStartLat = coord[0];
        mStartLon = coord[1];
        mStartDist_m = coord[2];
        mEndDist_m = coord[3];
        return 0;
    }

    private Double getAverageTerrainProfile (double start_lat, double start_lon, double end_lat, double end_lon) {
        try {
            NRCanElevationAPI api = new NRCanElevationAPI();
            List<Double> resList;
            double resAverage = 0;
            resList =  api.getElevationProfile_10(NRCanElevationAPI.ElevationDB.CDEM,
                    start_lat, start_lon, end_lat, end_lon);
            double sum = 0;
            for (int i=0 ; i < resList.size() ; i++) {
                sum += resList.get(i);
            }
            resAverage = sum / resList.size();
            return resAverage;
        } catch (Exception e) {
            Log.e(sDebugTag, "getAverageTerrainProfile: Exception: " + e.getMessage());
            return -999.0;
        }
    }

    @Override
    protected void onPostExecute(Integer res) {
        DecimalFormat df = new DecimalFormat("#.0000");
        DecimalFormat df_average = new DecimalFormat("#.0");
        String resString = "";
        if (res == 0) {
            resString = "TODO\n";
            /*
            resString = "Starting point:\n"+
                    "L"+df.format(start_lat)+"\n"+
                    "Lo"+df.format(start_lon)+"\n"+
                    "End point:\n"+
                    "L"+df.format(end_lat)+"\n"+
                    "Lo"+df.format(end_lon)+"\n"+
                    "Average: "+df_average.format(profile_average)+"\n"+
                    "Profile: "+mList.toString();
            */
        } else {
            resString = "Error";
        }
        mElevationActivityListener.setResultTextField(resString);
        mElevationActivityListener.setProgressBarVisible(false);
    }

}
