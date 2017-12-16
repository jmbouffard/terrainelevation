package ca.portableinnovation.terrainelevation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jmichel on 2015-12-14.
 */
public class EhaatActivity extends Activity implements QueryAsyncTaskListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ehaat);

        // populate spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.radials_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Set default value to items #1 (8)
        spinner.setSelection(1);

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
                final EditText edittext09 = (EditText) findViewById(R.id.EditText09);
                try {
                    if (v == button_get) {
                        runEhaatQuery(Integer.parseInt(edittext01.getText().toString()),
                                Integer.parseInt(edittext02.getText().toString()),
                                Integer.parseInt(edittext03.getText().toString()),
                                Integer.parseInt(edittext04.getText().toString()),
                                Integer.parseInt(edittext05.getText().toString()),
                                Integer.parseInt(edittext06.getText().toString()),
                                Integer.parseInt(edittext07.getText().toString()),
                                Integer.parseInt(edittext08.getText().toString()),
                                Integer.parseInt(edittext09.getText().toString()),
                                8);
                    }
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

    void runEhaatQuery(int lat_d, int lat_m, int lat_s, int lon_d, int lon_m, int lon_s, int radial_from, int radial_to, int tx_height, int nbr_radials) {
        double start_lat = PointsCalculations.GetDoubleFromDDMMSS(lat_d, lat_m, lat_s);
        double start_lon = PointsCalculations.GetDoubleFromDDMMSS(lon_d, lon_m, lon_s);
        // TODO run Query
        //new QueryEhaatAsyncTask(this).execute(start_lat,start_lon,radial_from,radial_to, tx_height, nbr_radials);
    }

    @Override
    public void setProgressBarVisible(boolean val) {
        final ProgressBar progressbarround = (ProgressBar) findViewById(R.id.progressBar1);
        final ProgressBar progressbarrec = (ProgressBar) findViewById(R.id.progressBar2);
        if (val) {
            progressbarround.setVisibility(View.VISIBLE);
            progressbarrec.setVisibility(View.VISIBLE);
        } else {
            progressbarround.setVisibility(View.INVISIBLE);
            progressbarrec.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setResultTextField(String val) {
        final TextView textview = (TextView) findViewById(R.id.textViewResult);
        textview.setText(val);
    }
}

