package ca.portableinnovation.terrainelevation;

import android.os.AsyncTask;
import android.util.Log;

import java.text.DecimalFormat;

class QueryElevationAsyncTask extends AsyncTask<Double, Void, Double> {

    private static final String sDebugTag = "TE.QueryElevAsyncTask";
    private QueryAsyncTaskListener mElevationActivityListener;
    private double lat = 0;
    private double lon = 0;

    QueryElevationAsyncTask (QueryAsyncTaskListener listener) {
        mElevationActivityListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mElevationActivityListener.setProgressBarVisible(true);
    }

    @Override
    protected Double doInBackground(Double... coord) {
        lat = coord[0];
        lon = coord[1];
        try {
        	NRCanElevationAPI api = new NRCanElevationAPI();
    		return api.getElevation(NRCanElevationAPI.ElevationDB.CDEM,
                    lat, lon);
        } catch (Exception e) {
            Log.e(sDebugTag, "doInBackground: Exception: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Double res) {
        DecimalFormat df = new DecimalFormat("#.0000");
        String resString = "Coordinate:\n"+
                "L"+df.format(lat)+"\n"+
                "Lo"+df.format(lon)+"\n"+
                "Elevation: "+res+"m";
        mElevationActivityListener.setProgressBarVisible(false);
        mElevationActivityListener.setResultTextField(resString);
    }

}
