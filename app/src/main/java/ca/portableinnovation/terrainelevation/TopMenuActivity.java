package ca.portableinnovation.terrainelevation;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TopMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_menu);

        final Activity mActivity = this;

        final ListView listview = (ListView) findViewById(R.id.listView);
        String[] values = new String[]{"Elevation", "Profile", "EHAAT"};

        final ArrayList<String> list = new ArrayList<String>();

        for(int i = 0 ; i<values.length ; ++i)
        {
            list.add(values[i]);
        }

        listview.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, list));

        // ListView Item Click Listener
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;
                // ListView Clicked item value
                String itemValue = (String) listview.getItemAtPosition(position);
                // Declare Intent used to start next Activity
                Intent intent;
                switch (itemPosition) {
                    case 0:
                        // Start new Activity
                        intent = new Intent(mActivity, ElevationActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        // Start new Activity
                        intent = new Intent(mActivity, ProfileActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        // Start new Activity
                        intent = new Intent(mActivity, EhaatActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        // Not implemeted
                        Toast.makeText(getApplicationContext(),
                                "Error : invalid selection", Toast.LENGTH_LONG)
                                .show();
                        break;
                }

                /*Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();*/
            }

        });
    }
}
