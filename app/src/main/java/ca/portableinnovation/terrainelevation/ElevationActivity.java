package ca.portableinnovation.terrainelevation;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ElevationActivity extends Activity implements QueryAsyncTaskListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_elevation);
		
		// Set button handler
		final Button button_get = (Button) findViewById(R.id.button_get);
		//final Button button2 = (Button) findViewById(R.id.button2);

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
				try {
					if (v == button_get) {
						runElevationQuery(Integer.parseInt(edittext01.getText().toString()),
								Integer.parseInt(edittext02.getText().toString()),
								Integer.parseInt(edittext03.getText().toString()),
								Integer.parseInt(edittext04.getText().toString()),
								Integer.parseInt(edittext05.getText().toString()),
								Integer.parseInt(edittext06.getText().toString()));
					} /*else if (v == button2) {
					} */
				}
				catch (NumberFormatException e) {
					Toast.makeText(v.getContext(), R.string.toast_wrong_coordinate, Toast.LENGTH_SHORT).show();
				}
			}
		};

		button_get.setOnClickListener(buttonListener);
		//button2.setOnClickListener(buttonListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.elevation, menu);
		return true;
	}

	// Runs NRCan API query for the specified location
	void runElevationQuery(int lat_d, int lat_m, int lat_s, int lon_d, int lon_m, int lon_s) {
		double lat = PointsCalculations.GetDoubleFromDDMMSS(lat_d, lat_m, lat_s);
		double lon = PointsCalculations.GetDoubleFromDDMMSS(lon_d, lon_m, lon_s);
		new QueryElevationAsyncTask(this).execute(lat,lon);
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
