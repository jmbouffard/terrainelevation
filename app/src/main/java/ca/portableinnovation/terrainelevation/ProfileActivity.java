package ca.portableinnovation.terrainelevation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jmichel on 2015-12-14.
 */
public class ProfileActivity extends Activity implements QueryAsyncTaskListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set button handler
        //final Button button1 = (Button) findViewById(R.id.button_get);
        final Button button_get = (Button) findViewById(R.id.button_get);

        View.OnClickListener buttonListener = new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                int result = 0;
                final EditText edittext01 = (EditText) findViewById(R.id.EditText01);
                final EditText edittext02 = (EditText) findViewById(R.id.EditText02);
                final EditText edittext03 = (EditText) findViewById(R.id.EditText03);
                final EditText edittext04 = (EditText) findViewById(R.id.EditText04);
                final EditText edittext05 = (EditText) findViewById(R.id.EditText05);
                final EditText edittext06 = (EditText) findViewById(R.id.EditText06);
                final EditText edittext07 = (EditText) findViewById(R.id.EditText07);
                final EditText edittext08 = (EditText) findViewById(R.id.EditText08);
                try {
                    if (v == button_get) {
                        runProfileQuery(Integer.parseInt(edittext01.getText().toString()),
                                Integer.parseInt(edittext02.getText().toString()),
                                Integer.parseInt(edittext03.getText().toString()),
                                Integer.parseInt(edittext04.getText().toString()),
                                Integer.parseInt(edittext05.getText().toString()),
                                Integer.parseInt(edittext06.getText().toString()),
                                Integer.parseInt(edittext07.getText().toString()),
                                Integer.parseInt(edittext08.getText().toString()));
                    } /*else if (v == button2) {
                    } */
                }
                catch (NumberFormatException e) {
                    Toast.makeText(v.getContext(), R.string.toast_wrong_coordinate, Toast.LENGTH_SHORT).show();
                }
            }
        };

        //button1.setOnClickListener(buttonListener);
        button_get.setOnClickListener(buttonListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.elevation, menu);
        return true;
    }

    void runProfileQuery(int lat_d, int lat_m, int lat_s, int lon_d, int lon_m, int lon_s, int azim_deg, int dist_km) {
        double start_lat = PointsCalculations.GetDoubleFromDDMMSS(lat_d, lat_m, lat_s);
        double start_lon = PointsCalculations.GetDoubleFromDDMMSS(lon_d, lon_m, lon_s);
        double end_lat = PointsCalculations.getNewLat(start_lat, start_lon, 1.0 * azim_deg, 1000.0 * dist_km);
        double end_lon = PointsCalculations.getNewLon(start_lat, start_lon, 1.0 * azim_deg, 1000.0 * dist_km);
        new QueryProfileAsyncTask(this).execute(start_lat,start_lon,end_lat,end_lon);
		/*double newlat = PointsCalculations.getNewLat(lat,lon,45,16000);
		double newlon = PointsCalculations.getNewLon(lat,lon,45,16000);
		double newlat2 = PointsCalculations.getNewLat(lat,lon,270,16000);
		double newlon2 = PointsCalculations.getNewLon(lat, lon, 270, 16000);
		setResultTextField("New point @ 45deg/16km is: "+newlat+" N - "+newlon+" W"+"\n"+"New point @ 270deg/16km is: " + newlat2 + " N - " + newlon2 + " W");*/
    }

    @Override
    public void setProgressBarVisible(boolean val) {
        final ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressBar1);
        if (val) {
            progressbar.setVisibility(View.VISIBLE);
        } else {
            progressbar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setResultTextField(String val) {
        final TextView textview = (TextView) findViewById(R.id.textViewResult);
        textview.setText(val);
    }
}

