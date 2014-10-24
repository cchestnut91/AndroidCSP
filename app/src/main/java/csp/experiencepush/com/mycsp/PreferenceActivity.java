package csp.experiencepush.com.mycsp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class PreferenceActivity extends Activity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);


        List<ListItem> itemList = new ArrayList<ListItem>();
        itemList.add(new ListItem(" " , "header", ""));
        itemList.add(new ListItem("Default Search Preferences", "link", ""));
        itemList.add(new ListItem("Clear Defaults", "button", ""));
        itemList.add(new ListItem("Search preferences will start with defaults, and can then be furthur customized in any search screen", "footer", ""));
        itemList.add(new ListItem(" " , "header", ""));
        itemList.add(new ListItem("Enable Nearby Notifications", "switch", ""));
        itemList.add(new ListItem("Nearby Notifications alert you if there is a nearby listing you may be interested in", "footer", ""));
        itemList.add(new ListItem(" " , "header", ""));
        itemList.add(new ListItem("Clear Favorites", "button", ""));
        itemList.add(new ListItem("This cannot be undone", "footer", ""));

        ListItemAdapter adapter = new ListItemAdapter(this, R.layout.search_settings_row_check, itemList);

        ListView itemListView = (ListView)findViewById(R.id.preferences_list_view);
        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1){
                    Intent defaults = new Intent(context, DefaultSettingsActivity.class);
                    startActivityForResult(defaults, 0);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preference, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
