package ca.portableinnovation.terrainelevation;

import android.os.AsyncTask;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.List;

class QueryProfileAsyncTask extends AsyncTask<Double, Void, Integer> {

    private static final String sDebugTag = "TE.QueryProfAsyncTask";
    private QueryAsyncTaskListener mElevationActivityListener;
    List<Double> mList;
    private double start_lat = 0;
    private double start_lon = 0;
    private double end_lat = 0;
    private double end_lon = 0;
    private double profile_average = 0;

    QueryProfileAsyncTask(QueryAsyncTaskListener listener) {
        mElevationActivityListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mElevationActivityListener.setProgressBarVisible(true);
    }

    @Override
    protected Integer doInBackground(Double... coord) {
        start_lat = coord[0];
        start_lon = coord[1];
        end_lat = coord[2];
        end_lon = coord[3];
        try {
        	NRCanElevationAPI api = new NRCanElevationAPI();
            mList =  api.getElevationProfile_10(NRCanElevationAPI.ElevationDB.CDEM,
                    start_lat, start_lon, end_lat, end_lon);
            double sum = 0;
            for (int i=0 ; i < mList.size() ; i++) {
                sum += mList.get(i);
            }
            profile_average = sum / mList.size();
            return 0;
        } catch (Exception e) {
            Log.e(sDebugTag, "doInBackground: Exception: " + e.getMessage());
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer res) {
        DecimalFormat df = new DecimalFormat("#.0000");
        DecimalFormat df_average = new DecimalFormat("#.0");
        String resString = "";
        if (res == 0) {
            resString = "Starting point:\n"+
                    "L"+df.format(start_lat)+"\n"+
                    "Lo"+df.format(start_lon)+"\n"+
                    "End point:\n"+
                    "L"+df.format(end_lat)+"\n"+
                    "Lo"+df.format(end_lon)+"\n"+
                    "Average: "+df_average.format(profile_average)+"\n"+
                    "Profile: "+mList.toString();

        } else {
            resString = "Error";
        }
        mElevationActivityListener.setResultTextField(resString);
        mElevationActivityListener.setProgressBarVisible(false);
    }

}
